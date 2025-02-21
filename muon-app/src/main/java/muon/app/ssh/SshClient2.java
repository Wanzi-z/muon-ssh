package muon.app.ssh;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import muon.app.App;
import muon.app.ui.components.SkinnedTextField;
import muon.app.ui.components.session.HopEntry;
import muon.app.ui.components.session.SessionInfo;
import net.schmizz.keepalive.KeepAliveProvider;
import net.schmizz.sshj.DefaultConfig;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.DirectConnection;
import net.schmizz.sshj.connection.channel.direct.LocalPortForwarder;
import net.schmizz.sshj.connection.channel.direct.Parameters;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.connection.channel.forwarded.RemotePortForwarder;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.Transport;
import net.schmizz.sshj.userauth.keyprovider.KeyProvider;
import net.schmizz.sshj.userauth.method.AuthKeyboardInteractive;
import net.schmizz.sshj.userauth.method.AuthNone;
import muon.app.util.enums.JumpType;

import javax.swing.*;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author subhro
 */
@Slf4j
public class SshClient2 implements Closeable {
    private static final int CONNECTION_TIMEOUT = App.getGlobalSettings().getConnectionTimeout() * 1000;
    private final AtomicBoolean closed = new AtomicBoolean(false);

    @Getter
    private final SessionInfo info;
    private final PasswordFinderDialog passwordFinder;
    private final InputBlocker inputBlocker;
    private final CachedCredentialProvider cachedCredentialProvider;
    private SSHClient sshj;
    private SshClient2 previousHop;
    private ServerSocket ss;

    public SshClient2(SessionInfo info, InputBlocker inputBlocker, CachedCredentialProvider cachedCredentialProvider) {
        this.info = info;
        this.inputBlocker = inputBlocker;
        this.cachedCredentialProvider = cachedCredentialProvider;
        passwordFinder = new PasswordFinderDialog(cachedCredentialProvider);
    }

    private void setupProxyAndSocketFactory() {
        String proxyHost = info.getProxyHost();
        int proxyType = info.getProxyType();
        String proxyUser = info.getProxyUser();
        String proxyPass = info.getProxyPassword();
        int proxyPort = info.getProxyPort();

        Proxy.Type proxyType1 = Proxy.Type.DIRECT;

        if (proxyType == 1) {
            proxyType1 = Proxy.Type.HTTP;
        } else if (proxyType > 1) {
            proxyType1 = Proxy.Type.SOCKS;
        }

        sshj.setSocketFactory(new CustomSocketFactory(proxyHost, proxyPort, proxyUser, proxyPass, proxyType1));
    }

    private void getAuthMethods(AtomicBoolean authenticated, List<String> allowedMethods)
            throws OperationCancelledException {
        log.info("Trying to get allowed authentication methods...");
        try {
            String user = promptUser();
            if (user == null || user.isEmpty()) {
                throw new OperationCancelledException();
            }
            sshj.auth(user, new AuthNone());
            authenticated.set(true); // Surprise! no authentication!!!
        } catch (OperationCancelledException e) {
            throw e;
        } catch (Exception e) {
            allowedMethods.addAll(sshj.getUserAuth().getAllowedMethods());
            log.info("List of allowed authentications: {}", allowedMethods);
        }
    }

    private void authPublicKey() throws Exception {
        KeyProvider provider = null;
        if (info.getPrivateKeyFile() != null && !info.getPrivateKeyFile().isEmpty()) {
            File keyFile = new File(info.getPrivateKeyFile());
            if (keyFile.exists()) {
                provider = sshj.loadKeys(info.getPrivateKeyFile(), passwordFinder);
                log.info("Key provider: {}", provider);
                log.info("Key type: {}", provider.getType());
            }
        }

        if (closed.get()) {
            disconnect();
            throw new OperationCancelledException();
        }

        if (provider == null) {
            throw new Exception("No suitable key providers");
        }

        sshj.authPublickey(promptUser(), provider);
    }

    private void authPassoword() throws Exception {
        String user = getUser();
        char[] password = getPassword();
        if (user == null || user.isEmpty()) {
            password = null;
        }
        // keep on trying with password
        while (!closed.get()) {
            if (password == null || password.length < 1) {
                JTextField txtUser = new SkinnedTextField(30);
                JPasswordField txtPassword = new JPasswordField(30);
                JCheckBox chkUseCache = new JCheckBox(App.bundle.getString("remember_session"));
                txtUser.setText(user);
                int ret = JOptionPane.showOptionDialog(App.getAppWindow(),
                                                       new Object[]{"User", txtUser, "Password", txtPassword, chkUseCache}, App.bundle.getString("authentication"),
                                                       JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
                if (ret == JOptionPane.OK_OPTION) {
                    user = txtUser.getText();
                    password = txtPassword.getPassword();
                    if (chkUseCache.isSelected()) {
                        cachedCredentialProvider.setCachedUser(user);
                        cachedCredentialProvider.cachePassword(password);
                    }
                } else {
                    throw new OperationCancelledException();
                }
            }
            try {
                sshj.authPassword(user, password); // provide
                // password
                // updater
                // PasswordUpdateProvider
                // net.schmizz.sshj.userauth.password.PasswordUpdateProvider
                return;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                password = null;
            }
        }
    }

    public void connect() throws IOException, OperationCancelledException {
        Deque<HopEntry> hopStack = new ArrayDeque<>(this.info.getJumpHosts());
        this.connect(hopStack);
    }

    private void connect(Deque<HopEntry> hopStack) throws IOException, OperationCancelledException {
        this.inputBlocker.blockInput();
        try {
            DefaultConfig defaultConfig = new DefaultConfig();
            if (App.getGlobalSettings().isShowMessagePrompt()) {
                log.info("enabled KeepAliveProvider");
                defaultConfig.setKeepAliveProvider(KeepAliveProvider.KEEP_ALIVE);
            }
            sshj = new SSHClient(defaultConfig);

            sshj.setConnectTimeout(CONNECTION_TIMEOUT);
            sshj.setTimeout(CONNECTION_TIMEOUT);
            if (hopStack.isEmpty()) {
                this.setupProxyAndSocketFactory();
                this.sshj.addHostKeyVerifier(App.HOST_KEY_VERIFIER);
                sshj.connect(info.getHost(), info.getPort());
            } else {
                try {
                    log.info("Tunneling through...");
                    tunnelThrough(hopStack);
                    log.info("adding host key verifier");
                    this.sshj.addHostKeyVerifier(App.HOST_KEY_VERIFIER);
                    log.info("Host key verifier added");
                    if (this.info.getJumpType() == JumpType.TCP_FORWARDING) {
                        log.info("tcp forwarding...");
                        this.connectViaTcpForwarding();
                    } else {
                        log.info("port forwarding...");
                        this.connectViaPortForwarding();
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    disconnect();
                    throw new IOException(e);
                }
            }

            sshj.getConnection().getKeepAlive().setKeepAliveInterval(5);
            if (closed.get()) {
                disconnect();
                throw new OperationCancelledException();
            }

            // Connection established, now find out supported authentication
            // methods
            AtomicBoolean authenticated = new AtomicBoolean(false);
            List<String> allowedMethods = new ArrayList<>();

            this.getAuthMethods(authenticated, allowedMethods);

            if (authenticated.get()) {
                return;
            }

            if (closed.get()) {
                disconnect();
                throw new OperationCancelledException();
            }

            // loop over servers preferred authentication methods in the same
            // order sent by server
            for (String authMethod : allowedMethods) {
                if (closed.get()) {
                    disconnect();
                    throw new OperationCancelledException();
                }

                log.info("Trying auth method: {}", authMethod);

                switch (authMethod) {
                    case "publickey":
                        try {
                            this.authPublicKey();
                            authenticated.set(true);
                        } catch (OperationCancelledException e) {
                            disconnect();
                            throw e;
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        break;

                    case "keyboard-interactive":
                        try {
                            sshj.auth(promptUser(), new AuthKeyboardInteractive(new InteractiveResponseProvider()));
                            authenticated.set(true);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        break;

                    case "password":
                        try {
                            this.authPassoword();
                            authenticated.set(true);
                        } catch (OperationCancelledException e) {
                            disconnect();
                            throw e;
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                        break;
                }

                if (authenticated.get()) {
                    return;
                }
            }

            throw new IOException("Authentication failed");

        } catch (Exception e) {
            if (this.sshj != null) {
                this.sshj.close();
            }
            throw e;
        } finally {
            this.inputBlocker.unblockInput();
        }
    }

    private boolean isPasswordSet() {
        return info.getPassword() != null && !info.getPassword().isEmpty();
    }

    private String getUser() {
        String user = cachedCredentialProvider.getCachedUser();
        if (user == null || user.isEmpty()) {
            user = this.info.getUser();
        }
        return user;
    }

    private char[] getPassword() {
        char[] password = cachedCredentialProvider.getCachedPassword();
        if (password == null && (this.info.getPassword() != null && !this.info.getPassword().isEmpty())) {
            password = this.info.getPassword().toCharArray();
        }
        return password;
    }

    private String promptUser() {
        String user = getUser();
        if (user == null || user.isEmpty()) {
            JTextField txtUser = new SkinnedTextField(30);
            JCheckBox chkCacheUser = new JCheckBox(App.bundle.getString("remember_username"));
            int ret = JOptionPane.showOptionDialog(null, new Object[]{App.bundle.getString("username"), txtUser, chkCacheUser}, App.bundle.getString("user"),
                                                   JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
            if (ret == JOptionPane.OK_OPTION) {
                user = txtUser.getText();
                if (chkCacheUser.isSelected()) {
                    cachedCredentialProvider.setCachedUser(user);
                }
            }
        }
        return user;
    }

    public Session openSession() throws Exception {
        if (closed.get()) {
            disconnect();
            throw new IOException("Closed by user");
        }
        Session session = sshj.startSession();
        if (closed.get()) {
            disconnect();
            throw new IOException("Closed by user");
        }
        return session;
    }

    public boolean isConnected() {
        return sshj != null && sshj.isConnected();
    }

    @Override
    public void close() throws IOException {
        try {
            log.info("Wrapper closing for: {}", info);
            disconnect();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void disconnect() {
        if (closed.get()) {
            log.info("Already closed: {}", info);
            return;
        }
        closed.set(true);
        try {
            if (sshj != null) {
                sshj.disconnect();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        try {
            if (previousHop != null) {
                previousHop.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        try {
            if (this.ss != null) {
                this.ss.close();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return info.getName();
    }

    public SessionInfo getSource() {
        return info;
    }

    public SSHClient getSession() {
        return sshj;
    }

    public SFTPClient createSftpClient() throws IOException {
        return sshj.newSFTPClient();
    }

    // recursively
    private void tunnelThrough(Deque<HopEntry> hopStack) throws Exception {
        HopEntry ent = hopStack.poll();
        SessionInfo hopInfo = new SessionInfo();
        hopInfo.setHost(Objects.requireNonNull(ent).getHost());
        hopInfo.setPort(ent.getPort());
        hopInfo.setUser(ent.getUser());
        hopInfo.setPassword(ent.getPassword());
        hopInfo.setPrivateKeyFile(ent.getKeypath());
        previousHop = new SshClient2(hopInfo, inputBlocker, cachedCredentialProvider);
        previousHop.connect(hopStack);
    }

    private DirectConnection newDirectConnection(String host, int port) throws Exception {
        return sshj.newDirectConnection(host, port);
    }

    private void connectViaTcpForwarding() throws Exception {
        this.sshj.connectVia(this.previousHop.newDirectConnection(info.getHost(), info.getPort()), info.getHost(),
                             info.getPort());
    }

    private void connectViaPortForwarding() throws Exception {
        ss = new ServerSocket();
        ss.setReuseAddress(true);
        ss.bind(new InetSocketAddress("127.0.0.1", 0));
        int port = ss.getLocalPort();
        new Thread(() -> {
            try {
                this.previousHop
                        .newLocalPortForwarder(
                                new Parameters("127.0.0.1", port, this.info.getHost(), this.info.getPort()), ss)
                        .listen();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }).start();
        while (!ss.isBound()) {
            Thread.sleep(100);
        }
        this.sshj.connect("127.0.0.1", port);
    }

    public LocalPortForwarder newLocalPortForwarder(Parameters parameters, ServerSocket serverSocket) {
        return this.sshj.newLocalPortForwarder(parameters, serverSocket);
    }


    public RemotePortForwarder getRemotePortForwarder() {
        this.sshj.getConnection().getKeepAlive().setKeepAliveInterval(30);
        return this.sshj.getRemotePortForwarder();
    }

    public Transport getTransport() {
        return this.sshj.getTransport();
    }
}
