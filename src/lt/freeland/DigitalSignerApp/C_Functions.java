package lt.freeland.DigitalSignerApp;

import java.io.IOException;

import sun.security.pkcs11.wrapper.CK_INFO;
import sun.security.pkcs11.wrapper.CK_SLOT_INFO;
import sun.security.pkcs11.wrapper.CK_TOKEN_INFO;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.security.pkcs11.wrapper.PKCS11Exception;

public class C_Functions
	{
		private PKCS11 pkcs11;
		protected long sessionHandle_ = -1L;
		protected long tokenHandle_ = -1L;

		public C_Functions(String provider)
			{
				try
					{
						pkcs11 = PKCS11.getInstance(provider, "C_GetFunctionList", null, false);
					}
				catch (IOException | PKCS11Exception e)
					{
						e.printStackTrace();
					}
			}
				
		public CK_INFO C_GetInfo() throws PKCS11Exception
			{
				return pkcs11.C_GetInfo();
			}
		
		public long[] C_GetSlotList(boolean b) throws PKCS11Exception
			{
				return pkcs11.C_GetSlotList(b);
			}
		
		public CK_SLOT_INFO C_GetSlotInfo(long slot) throws PKCS11Exception
			{
				return pkcs11.C_GetSlotInfo(slot);
			}
		
		public CK_TOKEN_INFO C_GetTokenInfo(long slot) throws PKCS11Exception
			{
				return pkcs11.C_GetTokenInfo(slot);
			}
		
		public long[] C_FindCertificateObjects(long slot_) throws PKCS11Exception
			{
				long[] certObjList = null;
				CK_ATTRIBUTE[] attributeTemplateList = new CK_ATTRIBUTE[1];
				
				attributeTemplateList[0] = new CK_ATTRIBUTE();
		        attributeTemplateList[0].type = PKCS11Constants.CKA_CLASS;
		        attributeTemplateList[0].pValue = new Long(PKCS11Constants.CKO_CERTIFICATE);
		        
				pkcs11.C_FindObjectsInit(sessionHandle_, attributeTemplateList);
				certObjList = pkcs11.C_FindObjects(sessionHandle_, 100);
				pkcs11.C_FindObjectsFinal(sessionHandle_);
				
				return certObjList;
			}
		
		public CK_ATTRIBUTE[] C_GetAttributeValue(long object_, long attrType_) throws PKCS11Exception
			{
				long[] certObjList = null;
				CK_ATTRIBUTE[] attributeTemplateList = new CK_ATTRIBUTE[1];
				
				attributeTemplateList[0] = new CK_ATTRIBUTE();
		        attributeTemplateList[0].type = attrType_;
			        
				pkcs11.C_GetAttributeValue(sessionHandle_, object_, attributeTemplateList);
				
				return attributeTemplateList;
			}
		
		public void C_OpenSession(long slot_) throws PKCS11Exception
			{
				sessionHandle_ = pkcs11.C_OpenSession(slot_, PKCS11Constants.CKF_SERIAL_SESSION, null, null);
			}
		
		public void C_CloseSession() throws PKCS11Exception
			{
				pkcs11.C_CloseSession(sessionHandle_);
			}
	}
