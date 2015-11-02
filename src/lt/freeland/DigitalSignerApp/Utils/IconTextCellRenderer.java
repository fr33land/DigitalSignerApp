package lt.freeland.DigitalSignerApp.Utils;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import lt.freeland.DigitalSignerApp.DigitalSignerApplet;

public class IconTextCellRenderer extends DefaultTableCellRenderer 
	{
		private ImageIcon certIcon = new ImageIcon(DigitalSignerApplet.class.getResource("/images/certificate.png"));
		
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
			{
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        setText(value.toString());
		        setIcon(certIcon);
		        return this;
			}
}