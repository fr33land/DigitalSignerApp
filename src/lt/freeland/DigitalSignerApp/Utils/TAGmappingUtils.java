package lt.freeland.DigitalSignerApp.Utils;

import java.util.HashMap;

import org.bouncycastle.asn1.x509.GeneralName;

public class TAGmappingUtils 
	{
		static HashMap<Integer, String> tagMap = new HashMap<Integer,String>();
		
		static
			{				
				tagMap.put(GeneralName.directoryName,				"Directory name");
				tagMap.put(GeneralName.dNSName,						"DNS name");
				tagMap.put(GeneralName.ediPartyName,				"EDI party name");
				tagMap.put(GeneralName.iPAddress,					"IP address");
				tagMap.put(GeneralName.otherName,					"Other name");
				tagMap.put(GeneralName.registeredID,				"Registered ID");
				tagMap.put(GeneralName.rfc822Name,					"RFC822 name");
				tagMap.put(GeneralName.uniformResourceIdentifier,	"URI");
				tagMap.put(GeneralName.x400Address,					"X400 address");
			}
		
		public static String getAttributeValue(Integer attr)
			{
				return tagMap.containsKey(attr) ? tagMap.get(attr) : attr.toString();
			}
	}
