/**
 *
 */
package muon.app.ssh;

import lombok.Getter;
import lombok.Setter;
import net.schmizz.sshj.sftp.RemoteFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author subhro
 *
 */
public class SSHRemoteFileInputStream extends InputStream {

    private final RemoteFile remoteFile;
    private final InputStream in;
    @Setter
    @Getter
    private int bufferCapacity;

    /**
     * @param remoteFile
     */
    public SSHRemoteFileInputStream(RemoteFile remoteFile, int localMaxPacketSize) {
        this.remoteFile = remoteFile;
        this.bufferCapacity = localMaxPacketSize;
        this.in = this.remoteFile.new ReadAheadRemoteFileInputStream(16);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return this.in.read(b, off, len);
    }

    @Override
    public int read() throws IOException {
        return this.in.read();
    }

    @Override
    public void close() throws IOException {
        try {
            this.remoteFile.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        try {
            this.in.close();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
