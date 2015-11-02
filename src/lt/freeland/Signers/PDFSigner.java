package lt.freeland.Signers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.BouncyCastleDigest;
import com.itextpdf.text.pdf.security.CertificateInfo;
import com.itextpdf.text.pdf.security.CertificateVerification;
import com.itextpdf.text.pdf.security.ExternalDigest;
import com.itextpdf.text.pdf.security.ExternalSignature;
import com.itextpdf.text.pdf.security.MakeSignature;
import com.itextpdf.text.pdf.security.MakeSignature.CryptoStandard;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import com.itextpdf.text.pdf.security.PrivateKeySignature;
import com.itextpdf.text.pdf.security.VerificationException;

public class PDFSigner 
	{
		private KeyStore ks;
		private PrivateKey pk;
		
		public static final String RESOURCE = "resources/images/certificate.png";

		public PDFSigner(KeyStore ks, PrivateKey pk)
			{
				this.ks = ks;
				this.pk = pk;
			}
	
		public void signDocument(String src, String dest, String alias, String algorithmName)
			{
				try 
					{
						Certificate[] chain;
						chain = ks.getCertificateChain(alias);					
				        PdfReader reader = new PdfReader(src);
				        FileOutputStream os = new FileOutputStream(dest);
				        PdfStamper stamper = PdfStamper.createSignature(reader, os, '\0');
				        // appearance
				        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
				        appearance.setVisibleSignature(new Rectangle(72, 732, 144, 780), 1,null);
				        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.DESCRIPTION);
				        // digital signature
				        ExternalSignature es = new PrivateKeySignature(pk, algorithmName, this.ks.getProvider().getName());
				        ExternalDigest digest = new BouncyCastleDigest();
				        MakeSignature.signDetached(appearance, digest, es, chain, null, null, null, 0, CryptoStandard.CMS);
					} 
				catch (IOException | DocumentException | GeneralSecurityException e) 
					{
						e.printStackTrace();
					}
		    }
		
		public void verifyDocumentSignature(String src, String alias) throws NoSuchAlgorithmException, CertificateException, IOException, KeyStoreException
			{
				//Certificate[] cchain = ks.getCertificateChain(alias);
				
				KeyStore verificationKs = KeyStore.getInstance("JKS");
				verificationKs.load(null, null);
				//verificationKs.setCertificateEntry("cacert", cchain[1]);
		        
		        PdfReader reader = new PdfReader(src);
		        AcroFields af = reader.getAcroFields();
		        ArrayList<String> names = af.getSignatureNames();
		        
		        for (String name : names) 
		        	{
		            	PdfPKCS7 pk = af.verifySignature(name);
		            	Calendar cal = pk.getSignDate();
		            	Certificate[] pkc = pk.getSignCertificateChain();
		            	List<VerificationException> errors = CertificateVerification.verifyCertificates(pkc, verificationKs, null, cal); 		            	
		            	errors.forEach(x -> System.out.println(x.getMessage()));
		        	}
		    }
	}
