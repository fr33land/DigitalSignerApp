package lt.freeland.DigitalSignerApp.SignerObjects;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.X509CertificateHolder;

import lt.freeland.DigitalSignerApp.Utils.HelperUtils;

public class TokenDataCertificateObject extends TokenDataObject {

    private X509CertificateHolder x509Certificate;

    private X500Name xSubject500Principles;
    private X500Name xIssuer500Principles;

    private MessageDigest sha1;
    private MessageDigest sha256;
    private MessageDigest md5;

    public TokenDataCertificateObject(long objectId, Object data) {
        super(objectId, data);
        this.x509Certificate = (X509CertificateHolder) data;

        this.xSubject500Principles = x509Certificate.getSubject();
        this.xIssuer500Principles = x509Certificate.getIssuer();
    }

    @Override
    public String toString() {
        RDN cn = this.xIssuer500Principles.getRDNs(BCStyle.CN)[0];
        return IETFUtils.valueToString(cn.getFirst().getValue());
    }

    public X509CertificateHolder getX509Certificate() {
        return x509Certificate;
    }

    public void setX509Certificate(X509CertificateHolder x509Certificate) {
        this.x509Certificate = x509Certificate;
    }

    public X500Name getxSubject500Principles() {
        return xSubject500Principles;
    }

    public void setxSubject500Principles(X500Name xSubject500Principles) {
        this.xSubject500Principles = xSubject500Principles;
    }

    public X500Name getxIssuer500Principles() {
        return xIssuer500Principles;
    }

    public void setxIssuer500Principles(X500Name xIssuer500Principles) {
        this.xIssuer500Principles = xIssuer500Principles;
    }

    public String getSHA1Fingerprint() {
        try {
            sha1 = MessageDigest.getInstance("SHA1");
            sha1.update(x509Certificate.getEncoded());
            return HelperUtils.bytesToHexString(sha1.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getSHA256Fingerprint() {
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(x509Certificate.getEncoded());
            return HelperUtils.bytesToHexString(sha256.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getMD5Fingerprint() {
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(x509Certificate.getEncoded());
            return HelperUtils.bytesToHexString(md5.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
