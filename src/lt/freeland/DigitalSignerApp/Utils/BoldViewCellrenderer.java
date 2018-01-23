package lt.freeland.DigitalSignerApp.Utils;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

public class BoldViewCellrenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component superRenderer = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        superRenderer.setFont(superRenderer.getFont().deriveFont(Font.BOLD));
        super.setHorizontalAlignment(SwingConstants.RIGHT);
        return superRenderer;
    }
}
