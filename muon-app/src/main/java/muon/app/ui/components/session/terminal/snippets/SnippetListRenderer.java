package muon.app.ui.components.session.terminal.snippets;

import muon.app.App;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SnippetListRenderer extends JPanel
        implements ListCellRenderer<SnippetItem> {
    private final JLabel lblName;
    private final JLabel lblCommand;

    public SnippetListRenderer() {
        super(new BorderLayout(5, 5));
        setBorder(new EmptyBorder(5, 10, 5, 10));
        lblName = new JLabel();
        lblName.setFont(lblName.getFont().deriveFont(Font.PLAIN, 14.0f));
        lblCommand = new JLabel();
        add(lblName);
        add(lblCommand, BorderLayout.SOUTH);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends SnippetItem> list, SnippetItem value, int index,
            boolean isSelected, boolean cellHasFocus) {
        setBackground(isSelected ? new Color(3, 155, 229)
                : list.getBackground());
        lblName.setForeground(
                isSelected ? App.getContext().getSkin().getDefaultSelectionForeground()
                        : App.getContext().getSkin().getDefaultForeground());
        lblCommand.setForeground(App.getContext().getSkin().getInfoTextForeground());
        lblName.setText(value.getName());
        lblCommand.setText(value.getCommand());
        return this;
    }
}
