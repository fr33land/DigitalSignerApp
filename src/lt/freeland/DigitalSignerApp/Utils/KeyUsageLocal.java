package lt.freeland.DigitalSignerApp.Utils;

import org.bouncycastle.asn1.x509.KeyUsage;

public enum KeyUsageLocal 
	{
		DigitalSignature(KeyUsage.digitalSignature, "Digital signature"),
		NonRepudiation(KeyUsage.nonRepudiation, "Non repudiation"),
		KeyEncipherment(KeyUsage.keyEncipherment, "Key encipherment"),
		DataEncipherment(KeyUsage.dataEncipherment, "Data encipherment"),
		KeyAgreement(KeyUsage.keyAgreement, "Key agreement"),
		KeyCertSign(KeyUsage.keyCertSign, "Key certSign"),
		CRLSign(KeyUsage.cRLSign, "CRL sign"),		
		EncipherOnly(KeyUsage.encipherOnly, "Encipher only"),
		DecipherOnly(KeyUsage.decipherOnly, "Decipher only");
	
		String caption; 
		int bitMask;
	
		KeyUsageLocal(int bitMask, String caption)
			{
				this.caption = caption;
				this.bitMask = bitMask;
			}
		
		@Override
		public String toString()
			{
				return this.caption;
			}
	}
