package lt.freeland.DigitalSignerApp.Utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Provider;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;

import sun.security.pkcs11.SunPKCS11;

public class HelperUtils
	{
		final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
		public static String getProviderLibraryPath(String path)
			{
				try
					{
						FileInputStream in = new FileInputStream(new File(path));
						BufferedReader reader = new BufferedReader(new InputStreamReader(in));
						List<String> result = new ArrayList<String>();
				        String line;
				        
				        while ((line = reader.readLine()) != null) 
				        	{
				        		line = line.trim().replace(" ", "");
				        		
				        		if(!line.matches("(#).*") && line.matches("(library=).*"))
				        			{
				        				String[] splitted = line.split("=");
				        				if(splitted.length > 1)
				        					{
				        						return splitted[1];
				        					}				        				
				        			}
				        	}
					}
				catch (IOException ex)
					{
						ex.printStackTrace();
					}
				
				return null;
			}
		
		public static String bytesToHexString(byte[] bytes)
			{
				char[] hexChars = new char[bytes.length * 2];
				
				for ( int i = 0; i < bytes.length; i++ ) 
					{
				        int v = bytes[i] & 0xFF;
				        hexChars[i * 2] = hexArray[v >>> 4];
					    hexChars[i * 2 + 1] = hexArray[v & 0x0F];
					}
				
				return new String(hexChars);
			}
		
		public static ASN1Primitive toASN1Object(byte[] data) throws IOException
			{
			    ByteArrayInputStream inStream = new ByteArrayInputStream(data);
			    ASN1InputStream asnInputStream = new ASN1InputStream(inStream);	
			    return asnInputStream.readObject();
			}
		
		public static Provider buildProvider(String name, String library, Long slot)
			{
				String slotN = Long.toString(slot);
				String providerCfg = "name = " + name + "\r\nlibrary = " + library + "\r\nslot = "+slotN+"\r\n";
				return new SunPKCS11(new ByteArrayInputStream(providerCfg.getBytes()));
			}
	}
