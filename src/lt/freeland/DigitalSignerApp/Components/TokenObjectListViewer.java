package lt.freeland.DigitalSignerApp.Components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.bouncycastle.cert.X509CertificateHolder;

import lt.freeland.DigitalSignerApp.C_Functions;
import lt.freeland.DigitalSignerApp.DigitalSignerApplet;
import lt.freeland.DigitalSignerApp.DialogWindows.ObjectDataDialog;
import lt.freeland.DigitalSignerApp.SignerObjects.TokenDataCertificateObject;
import lt.freeland.DigitalSignerApp.SignerObjects.TokenObject;
import lt.freeland.DigitalSignerApp.Utils.IconTextCellRenderer;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

public class TokenObjectListViewer extends JScrollPane implements MouseListener
	{
		private JTable jTable;
		private DefaultTableModel tableModel;
		private TokenObject to_;
		private C_Functions c_func;
		private long slot_;
		
		private ImageIcon certIcon = new ImageIcon(DigitalSignerApplet.class.getResource("/images/certificate.png"));
		
		public TokenObjectListViewer(TokenObject to) 
			{
				super();				
				
				jTable = new JTable();	
				
				this.to_ = to;
				c_func = this.to_.getC_func();
				
				this.slot_ = to.getObjectId();

				tableModel = new DefaultTableModel() 
					{
						@Override
						public boolean isCellEditable(int row, int column) 
							{
								return false;
							}
					};
					
				tableModel.addColumn("Objekto tipas");
				tableModel.addColumn("Sertifikato CA");
				tableModel.addColumn("Objekto būsena");
				jTable.setModel(tableModel);
				jTable.setRowHeight(28);
				jTable.setShowGrid(false);
				jTable.setIntercellSpacing(new Dimension(0, 0));
				jTable.setRowSelectionAllowed(true);
				jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				jTable.getColumnModel().getColumn(0).setPreferredWidth(40);
				jTable.getColumnModel().getColumn(1).setPreferredWidth(320);
				jTable.getColumnModel().getColumn(0).setCellRenderer(new IconTextCellRenderer());
				jTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
				jTable.addMouseListener(this);
				this.setViewportView(jTable);	
				this.getViewport().setBackground(Color.WHITE);
				
				showTokenObjects();
			}
		
		private void showTokenObjects()
			{				
				try
					{
						long[] certObjs;
						c_func.C_OpenSession(this.slot_);
						certObjs = c_func.C_FindCertificateObjects(this.slot_);
						
						for(long certObj:certObjs)
							{
								CK_ATTRIBUTE[] attr = c_func.C_GetAttributeValue(certObj, PKCS11Constants.CKA_VALUE);	
								X509CertificateHolder x509certificate = new X509CertificateHolder((byte[])attr[0].pValue);
							    TokenDataCertificateObject tdco = new TokenDataCertificateObject(certObj, x509certificate);
							    
								String validCert = "NEGALIOJA";
								
								if(x509certificate.isValidOn(new Date()))
									{
										validCert = "GALIOJA";
									}
							    
							    tableModel.addRow(new Object[]{"Sertifikatas", tdco, validCert});
							}
						
						c_func.C_CloseSession();
					}
				catch ( PKCS11Exception | IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) 
			{
				JTable table =(JTable) e.getSource();
		        Point p = e.getPoint();
		        int row = table.rowAtPoint(p);
		        
		        if (e.getClickCount() == 2) 
		        	{
		        		TokenDataCertificateObject tdco = (TokenDataCertificateObject) table.getValueAt(row, 1);
		        		ObjectDataDialog odd = new ObjectDataDialog(tdco);
		        	}			
			}

		@Override
		public void mouseReleased(MouseEvent e) 
			{
				
			}
	}
