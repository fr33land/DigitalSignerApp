package lt.freeland.DigitalSignerApp.Tasks;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import lt.freeland.Signers.PDFSigner;

public class DocumentSigningTask extends SwingWorker<Void, Integer> 
	{	
		private DefaultTableModel tableModel;
		private KeyStore ks;
		private String saveDir;
		private PrivateKey privateKey;
		private String alias;
		
		public DocumentSigningTask(DefaultTableModel tableModel, KeyStore ks, PrivateKey privateKey, String saveDir, String alias) 
			{
				this.tableModel = tableModel;
				this.ks = ks;
				this.saveDir = saveDir;
				this.privateKey = privateKey;
				this.alias = alias;
			}
	
		@Override
		protected Void doInBackground() throws Exception 
			{
				for(int i = 0; i < tableModel.getRowCount(); i++)
					{
						String filePath = (String) tableModel.getValueAt(i, 0);							
						Path path = Paths.get(filePath);
			
						if (Files.exists(path)) 
							{											
								if(Files.probeContentType(path).compareToIgnoreCase("application/pdf") == 0)
									{
										Path savePath = Paths.get(saveDir+File.separator+"signed_"+path.getFileName().toString());
										if(Files.exists(savePath))
											{
												tableModel.setValueAt(new String("FILE EXITS"), i, 1);
											}
										else
											{
												PDFSigner pdfSigner = new PDFSigner(ks, privateKey);
												pdfSigner.signDocument(path.toString(), saveDir+File.separator+"signed_"+path.getFileName().toString(), alias, "SHA1");
												pdfSigner.verifyDocumentSignature(saveDir+File.separator+"signed_"+path.getFileName().toString(), alias);
												publish(i);
											}
									}
								else
									{
										tableModel.setValueAt(new String("NOT IMPLEMENTED"), i, 1);
									}
							}
						else
							{
								tableModel.setValueAt(new String("FAILAS NEEGZISTUOJA"), i, 1);
							}
					}
				
				return null;
			}		
		
		protected void process(List<Integer> chunks) 
			{
				chunks.forEach(x -> tableModel.setValueAt(new String("PASIRAŠĖ"), x, 1));				
			}
	}
