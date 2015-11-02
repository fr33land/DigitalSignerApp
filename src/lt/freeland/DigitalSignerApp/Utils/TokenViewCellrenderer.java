package lt.freeland.DigitalSignerApp.Utils;

import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import lt.freeland.DigitalSignerApp.DigitalSignerApplet;
import lt.freeland.DigitalSignerApp.SignerObjects.ReaderDeviceObject;
import lt.freeland.DigitalSignerApp.SignerObjects.TokenDataObject;
import lt.freeland.DigitalSignerApp.SignerObjects.TokenObject;

public class TokenViewCellrenderer extends DefaultTreeCellRenderer
	{
		private ImageIcon readerIcon;
		private ImageIcon tokenIcon;
		private ImageIcon computerIcon;
		private ImageIcon smartCardIcon;
		
		public TokenViewCellrenderer()
			{
				readerIcon = new ImageIcon(DigitalSignerApplet.class.getResource("/images/reader.png"));
				tokenIcon = new ImageIcon(DigitalSignerApplet.class.getResource("/images/certificate.png"));
				computerIcon = new ImageIcon(DigitalSignerApplet.class.getResource("/images/computer.png"));
				smartCardIcon = new ImageIcon(DigitalSignerApplet.class.getResource("/images/smartcard.png"));
			}
		
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
			{
				super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
				Object o = ((DefaultMutableTreeNode) value).getUserObject();	
				
				if(o instanceof ReaderDeviceObject)
					{
						setIcon(readerIcon);
					}
				else if(o instanceof TokenDataObject)
					{
						setIcon(tokenIcon);
					}
				else if(o instanceof TokenObject)
					{
						setIcon(smartCardIcon);
					}
				else
					{
						setIcon(computerIcon);
					}
				
				return this;
			}
	}
