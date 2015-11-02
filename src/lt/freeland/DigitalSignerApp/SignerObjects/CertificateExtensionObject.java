package lt.freeland.DigitalSignerApp.SignerObjects;

import org.bouncycastle.asn1.x509.Extension;

import lt.freeland.DigitalSignerApp.Utils.OIDmappingUtils;

public class CertificateExtensionObject
	{
		private Extension tokenExtensionValue;
		private String oid; 
		private boolean critical;

		public Extension getTokenExtensionValue() 
			{
				return tokenExtensionValue;
			}

		public void setTokenExtensionValue(Extension tokenExtensionValue) 
			{
				this.tokenExtensionValue = tokenExtensionValue;
			}
		
		@Override
		public String toString()
			{
				return OIDmappingUtils.getAttributeValue(oid);
			}

		public String getOid() 
			{
				return oid;
			}

		public void setOid(String oid) 
			{
				this.oid = oid;
			}

		public void setCritical(boolean critical) 
			{
				this.critical = critical;
			}
		
		public boolean getCritical() 
			{
				return this.critical;
			}
		
		public String getCriticalCaption() 
			{
				return this.critical ? new String("CRITICAL") : new String("NOT CRITICAL");
			}		
	}
