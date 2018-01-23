package lt.freeland.DigitalSignerApp.Utils;

import java.util.HashMap;

public class OIDmappingUtils {

    static HashMap<String, String> oidMap = new HashMap<String, String>();

    static {
        oidMap.put("2.5.4.6", "Country");
        oidMap.put("ST", "State or province");
        oidMap.put("L", "Locality");
        oidMap.put("2.5.4.10", "Organization");
        oidMap.put("2.5.4.3", "Common name");
        oidMap.put("2.5.4.11", "Organizational unit");
        oidMap.put("2.5.4.5", "Serial number");
        oidMap.put("2.5.29.14", "Subject Key Identifier");
        oidMap.put("2.5.29.15", "Key Usage");
        oidMap.put("2.5.29.16", "Private Key Usage");
        oidMap.put("2.5.29.17", "Subject Alternative Name");
        oidMap.put("2.5.29.18", "Issuer Alternative Name");
        oidMap.put("2.5.29.19", "Basic Constraints");
        oidMap.put("2.5.29.30", "Name Constraints");
        oidMap.put("2.5.29.31", "CRL Distribution Points");
        oidMap.put("2.5.29.32", "Certificate Policies");
        oidMap.put("2.5.29.33", "Policy Mapping");
        oidMap.put("2.5.29.35", "Authority Key Identifier");
        oidMap.put("2.5.29.36", "Policy Constraints");
        oidMap.put("2.5.29.37", "Extended Key Usage");
        oidMap.put("2.16.840.1.113730.1.1", "Netscape certificate type");
        oidMap.put("1.2.840.113549.1.9.1", "E-mail address");
        oidMap.put("1.2.840.113549.1.1.5", "SHA-1 with RSA Encryption");
        oidMap.put("1.2.840.113549.1.1.1", "RSA encryption");
        oidMap.put("1.3.6.1.4.1.311.21.10", "Application Policies");
        oidMap.put("1.3.6.1.4.1.311.21.7", "Certificate Template");
        oidMap.put("1.3.6.1.5.5.7.1.1", "Authority Information Access");
        oidMap.put("1.3.6.1.5.5.7.1.3", "QC Statement");
        oidMap.put("1.3.6.1.4.1.311.10.3.12", "Document signing");
        oidMap.put("1.3.6.1.5.5.7.3.2", "Client Authentication");
        oidMap.put("1.3.6.1.5.5.7.3.4", "Email protection");
        oidMap.put("1.3.6.1.4.1.6449.1.3.5.2", "Email protection");
        oidMap.put("0.4.0.1862.1.1", "QC EU Compliance");
        oidMap.put("0.4.0.1862.1.2", "Qc EU Limit Value");
        oidMap.put("0.4.0.1862.1.3", "Certificates Conservation");
        oidMap.put("0.4.0.1862.1.4", "Secure Signature Creation Device");
    }

    public static String getAttributeValue(String attr) {
        return oidMap.containsKey(attr) ? oidMap.get(attr) : attr;
    }
}
