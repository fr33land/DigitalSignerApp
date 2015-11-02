package lt.freeland.DigitalSignerApp.DialogWindows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStore.Builder;
import java.security.KeyStore.CallbackHandlerProtection;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.util.Enumeration;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.lang3.exception.ExceptionUtils;

import lt.freeland.DigitalSignerApp.SignerObjects.TokenObject;
import lt.freeland.DigitalSignerApp.Tasks.CardDetectionTask;
import lt.freeland.DigitalSignerApp.Tasks.DocumentSigningTask;
import lt.freeland.DigitalSignerApp.Utils.CustomCallbackHandler;
import lt.freeland.Signers.PDFSigner;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

public class FileListDialog  extends JDialog implements ActionListener, MouseListener
{
	private final JPanel contentPanel = new JPanel();
	private TokenObject to;
	private JButton addFile;
	private JButton remFile;
	private JButton signFile;
	private BorderLayout bLayout;
	
	private JTable jTable;
	private DefaultTableModel tableModel;
	
	private JScrollPane jScrollPane;
	
	private JFileChooser fileChooser = new JFileChooser();
	private DocumentSigningTask c_DocumentSigningTask;
	
	public FileListDialog(TokenObject to)
		{
			bLayout = new BorderLayout();
			this.getContentPane().setLayout(bLayout);
			this.setBounds(0, 0, 450, 300);		
			this.setLocationRelativeTo(null);
			getContentPane().add(contentPanel, BorderLayout.PAGE_START);
			
			addFile = new JButton("Pridėti failą");
			addFile.addActionListener(this);
			
			remFile = new JButton("Pašalinti failą");
			remFile.addActionListener(this);
			
			signFile = new JButton("Pasirašyti failus");
			signFile.addActionListener(this);
			
			contentPanel.add(addFile);
			contentPanel.add(remFile);
			contentPanel.add(signFile);
			
			jTable = new JTable();
			tableModel = new DefaultTableModel() 
				{
					@Override
					public boolean isCellEditable(int row, int column) 
						{
							return false;
						}
				};
				
			tableModel.addColumn("Failas");
			tableModel.addColumn("Statusas");
			jTable.setModel(tableModel);
			jTable.setRowHeight(28);
			jTable.getColumnModel().getColumn(0).setPreferredWidth(300);
			jTable.setShowGrid(false);
			jTable.setIntercellSpacing(new Dimension(0, 0));
			jTable.setRowSelectionAllowed(true);
			jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			jTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
			jTable.addMouseListener(this);
			
			jScrollPane = new JScrollPane();
			jScrollPane.setViewportView(jTable);	
			jScrollPane.getViewport().setBackground(Color.WHITE);
			getContentPane().add(jScrollPane, BorderLayout.CENTER);
							
			this.to = to;				
			this.setModal(true);
			this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			
			this.setTitle(this.to.getProvider().getName());
		}

	@Override
	public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == addFile)
				{
					int returnVal = fileChooser.showOpenDialog(this);
					
					if (returnVal == JFileChooser.APPROVE_OPTION) 
						{
							File file = fileChooser.getSelectedFile();				
							
							int row = jTable.getRowCount();
							boolean found = false;
							
							for (int i=0; i<row; i++) 
								{
								    String str = jTable.getValueAt(i, 0).toString().trim();
								    if (file.getAbsolutePath().compareToIgnoreCase(str) == 0) 
								    	{
								    		JOptionPane.showMessageDialog(null, "Pasirinktas dokumentas jau yra sąraše","Pranešimas",JOptionPane.INFORMATION_MESSAGE);
								    		found = true;
								    		break; 
								    	} 
								}
							
							if(!found)tableModel.addRow(new Object[]{file.getAbsolutePath()});
			            } 
				}
			else if(e.getSource() == remFile)
				{
					if(jTable.getSelectedRow() > -1)
						{
							tableModel.removeRow(jTable.getSelectedRow());
							
							if(jTable.getRowCount() > 0) jTable.changeSelection(0, 0, false, false);
						}
				}
			else if(e.getSource() == signFile)
				{		
					if(tableModel.getRowCount() > 0)
						{
							signData();
						}
					else
						{
							JOptionPane.showMessageDialog(this, "Pasirašymui pridėkite bent vieną dokumentą", "Klaida", JOptionPane.ERROR_MESSAGE);
						}
				}
		}

	@Override
	public void mouseClicked(MouseEvent arg0) 
		{
			
		}

	@Override
	public void mouseEntered(MouseEvent arg0) 
		{
		
		}

	@Override
	public void mouseExited(MouseEvent arg0) 
		{
		
		}

	@Override
	public void mousePressed(MouseEvent e) 
		{
			JTable table =(JTable) e.getSource();
	        Point p = e.getPoint();
	        int row = table.rowAtPoint(p);
	        
	        if (e.getClickCount() == 2) 
	        	{
	
	        	}	
		}

	@Override
	public void mouseReleased(MouseEvent arg0) 
		{
		
		}
	
	private void signData() 
		{
			Provider p = this.to.getProvider();
			Builder builder = null;
			KeyStore ks = null;
			
			try 
				{
					builder = KeyStore.Builder.newInstance("PKCS11", p, new CallbackHandlerProtection(new CustomCallbackHandler(this.to.getObjectId())));	
					ks = builder.getKeyStore();				
					Enumeration<String> aliases = ks.aliases();	
					
					if(aliases.hasMoreElements())
						{
							PrivateKeySelectDialog pksd = new PrivateKeySelectDialog(aliases);
							if(pksd.getAlias() != null)
								{
									PrivateKey pk = (PrivateKey) ks.getKey(pksd.getAlias(), PinInputDialog.getPin());
									documentSignProcess(ks, pk, pksd.getAlias());
								}
						}
					else
						{
							PrivateKey pk = (PrivateKey) ks.getKey(aliases.nextElement(), PinInputDialog.getPin());
							documentSignProcess(ks, pk, aliases.nextElement());
						}
				} 
			catch (KeyStoreException | UnrecoverableKeyException | NoSuchAlgorithmException | InvalidKeyException | IOException ex) 
				{
					Throwable rootCause = ExceptionUtils.getRootCause(ex);
					
					if(((PKCS11Exception)rootCause).getErrorCode() == PKCS11Constants.CKR_PIN_INCORRECT)
						{
							JOptionPane.showMessageDialog(this, "Neteisingas PIN kodas", "Klaida", JOptionPane.ERROR_MESSAGE);
							this.signData();
						}		
				}
		}

	private void documentSignProcess(KeyStore ks, PrivateKey privateKey, String alias) throws InvalidKeyException, IOException
		{
			JFileChooser f = new JFileChooser();
			f.setCurrentDirectory(new java.io.File("."));
	        f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
	        f.setDialogTitle("Pasirinkite katalogą");
	        f.setAcceptAllFileFilterUsed(false);
	        
	        if (f.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) 
	        	{	           
	        		String saveDir = f.getCurrentDirectory().getPath()+File.separator+f.getSelectedFile().getName();
					c_DocumentSigningTask = new DocumentSigningTask(tableModel, ks, privateKey, saveDir, alias);
					c_DocumentSigningTask.execute();
	        	}
	        else
				{
					JOptionPane.showMessageDialog(this, "Pasirašinėjimas nutrauktas. Nepasirinkta saugojimo direktorija.", "Pranešimas", JOptionPane.WARNING_MESSAGE);
				}
		}
	
}
