package muon.app.ui.components.session.terminal;

import lombok.extern.slf4j.Slf4j;
import muon.app.App;
import muon.app.ui.components.common.ClosableTabbedPanel;
import muon.app.ui.components.session.Page;
import muon.app.ui.components.session.SessionContentPanel;
import muon.app.ui.components.session.SessionInfo;
import muon.app.ui.components.session.terminal.snippets.SnippetPanel;
import muon.app.util.FontAwesomeContants;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class TerminalHolder extends Page implements AutoCloseable {
    private final ClosableTabbedPanel tabs;
    private JPopupMenu snippetPopupMenu;
    private final SnippetPanel snippetPanel;
    private final AtomicBoolean init = new AtomicBoolean(false);
    private int c = 1;
    private final JButton btn;
    private final SessionContentPanel sessionContentPanel;

    public TerminalHolder(SessionInfo info, SessionContentPanel sessionContentPanel) {
        this.sessionContentPanel = sessionContentPanel;
        this.tabs = new ClosableTabbedPanel(e -> openNewTerminal(null));

        btn = new JButton();
        btn.setToolTipText("Snippets");
        btn.addActionListener(e -> showSnippets());
        btn.setFont(App.getContext().getSkin().getIconFont().deriveFont(16.0f));
        btn.setText(FontAwesomeContants.FA_BOOKMARK);
        btn.putClientProperty("Nimbus.Overrides", App.getContext().getSkin().createTabButtonSkin());
        btn.setForeground(App.getContext().getSkin().getInfoTextForeground());
        tabs.getButtonsBox().add(btn);

        long t1 = System.currentTimeMillis();
        TerminalComponent tc = new TerminalComponent(info, c + "", null, sessionContentPanel);
        this.tabs.addTab(tc.getTabTitle(), tc);
        long t2 = System.currentTimeMillis();
        log.debug("Terminal init in: {} ms", t2 - t1);

        snippetPanel = new SnippetPanel(e -> {
            TerminalComponent tc1 = (TerminalComponent) tabs.getSelectedContent();
            tc1.sendCommand(e + "\n");
        }, e -> this.snippetPopupMenu.setVisible(false));
        snippetPopupMenu = new JPopupMenu();
        snippetPopupMenu.add(snippetPanel);
        this.add(tabs);

        addAncestorListener(new AncestorListener() {

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                

            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
                

            }

            @Override
            public void ancestorAdded(AncestorEvent event) {
                log.debug("Terminal ancestor component shown");
                focusTerminal();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                focusTerminal();
            }
        });
    }

    private void focusTerminal() {
        tabs.requestFocusInWindow();
        log.debug("Terminal component shown");
        TerminalComponent comp = (TerminalComponent) tabs.getSelectedContent();
        if (comp != null) {
            comp.requestFocusInWindow();
            comp.getTerm().requestFocusInWindow();
            comp.getTerm().requestFocus();
        }
    }

    @Override
    public void onLoad() {
        if (init.get()) {
            return;
        }
        init.set(true);
        TerminalComponent tc = (TerminalComponent) this.tabs.getSelectedContent();
        tc.getTabTitle().getCallback().accept(tc.toString());
        tc.start();
    }

    private void showSnippets() {
        this.snippetPanel.loadSnippets();
        this.snippetPopupMenu.setLightWeightPopupEnabled(true);
        this.snippetPopupMenu.setOpaque(true);
        this.snippetPopupMenu.pack();
        this.snippetPopupMenu.setInvoker(this.btn);
        this.snippetPopupMenu.show(this.btn, this.btn.getWidth() - this.snippetPopupMenu.getPreferredSize().width,
                this.btn.getHeight());
    }

    public void close() {
        Component[] components = tabs.getTabContents();
        for (Component c : components) {
            if (c instanceof TerminalComponent) {
                log.info("Closing terminal: {}", c);
                ((TerminalComponent) c).close();
            }
        }
        revalidate();
        repaint();
    }

    @Override
    public String getIcon() {
        return FontAwesomeContants.FA_TELEVISION;
    }

    @Override
    public String getText() {
        return App.getContext().getBundle().getString("terminal");
    }

    public void openNewTerminal(String command) {
        c++;
        TerminalComponent tc = new TerminalComponent(this.sessionContentPanel.getInfo(), c + "", command,
                this.sessionContentPanel);
        this.tabs.addTab(tc.getTabTitle(), tc);
        tc.getTabTitle().getCallback().accept(tc.toString());
        tc.start();
    }
}
