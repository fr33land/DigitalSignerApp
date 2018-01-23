package lt.freeland.DigitalSignerApp.DialogWindows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Optional;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.misc.NetscapeCertType;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLDistPoint;
import org.bouncycastle.asn1.x509.CertificatePolicies;
import org.bouncycastle.asn1.x509.ExtendedKeyUsage;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.NameConstraints;
import org.bouncycastle.asn1.x509.PrivateKeyUsagePeriod;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.qualified.QCStatement;
import org.bouncycastle.cert.X509CertificateHolder;

import lt.freeland.DigitalSignerApp.SignerObjects.CertificateExtensionObject;
import lt.freeland.DigitalSignerApp.SignerObjects.TokenDataCertificateObject;
import lt.freeland.DigitalSignerApp.Utils.BoldViewCellrenderer;
import lt.freeland.DigitalSignerApp.Utils.KeyUsageLocal;
import lt.freeland.DigitalSignerApp.Utils.NullSelectionModel;
import lt.freeland.DigitalSignerApp.Utils.OIDmappingUtils;
import lt.freeland.DigitalSignerApp.Utils.TAGmappingUtils;

public class ObjectDataDialog extends JDialog implements ItemListener {

    private final JPanel contentPanel = new JPanel();

    private BoxLayout bLayout;
    private JTabbedPane tabbedPane = new JTabbedPane();

    private JTable subjectTable = new JTable();
    private DefaultTableModel subjectTableModel = new DefaultTableModel();
    private JScrollPane subjectScrollPane = new JScrollPane(subjectTable);

    private JTable issuerTable = new JTable();
    private DefaultTableModel issuerTableModel = new DefaultTableModel();
    private JScrollPane issuerScrollPane = new JScrollPane(issuerTable);

    private JTable certificateTable = new JTable();
    private DefaultTableModel certificateTableModel = new DefaultTableModel();
    private JScrollPane certificateScrollPane = new JScrollPane(certificateTable);

    private JTextArea otherInfoArea = new JTextArea();
    private JScrollPane otherInfoScrollPane = new JScrollPane(otherInfoArea);

    private JPanel subjMainPanel;
    private BoxLayout bPanelLayout;

    private JPanel ktMainPanel;
    private BorderLayout bBorderLayout;
    private JComboBox extensionList = new JComboBox();

    private TokenDataCertificateObject tdco;

    private JPanel extensionsSelectPanel;

    private JLabel extensionListCaption;

    private X509CertificateHolder cert;

    private SimpleDateFormat dt = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

    public ObjectDataDialog(TokenDataCertificateObject tdco) {
        bLayout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        this.getContentPane().setLayout(bLayout);
        this.setTitle("Certificate data");
        this.setBounds(0, 0, 650, 400);
        this.tdco = tdco;
        this.cert = this.tdco.getX509Certificate();

        JPanel subjMainPanel = new JPanel();
        bPanelLayout = new BoxLayout(subjMainPanel, BoxLayout.Y_AXIS);
        subjMainPanel.setLayout(bPanelLayout);

        tabbedPane.addTab("Certificate sides", subjMainPanel);

        subjectTableModel.addColumn("Identificator");
        subjectTableModel.addColumn("Value");
        subjectTable.setModel(subjectTableModel);
        subjectTable.setTableHeader(null);
        subjectTable.setRowHeight(32);
        subjectTable.setShowGrid(false);
        subjectTable.setIntercellSpacing(new Dimension(15, 0));
        subjectTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        subjectTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        subjectTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        subjectTable.setSelectionModel(new NullSelectionModel());
        subjectTable.setShowGrid(false);
        subjectTable.getColumnModel().getColumn(0).setCellRenderer(new BoldViewCellrenderer());
        subjectScrollPane.getViewport().setBackground(Color.WHITE);
        TitledBorder b1 = BorderFactory.createTitledBorder("Certificate subject");
        b1.setBorder(new EtchedBorder());
        subjectScrollPane.setBorder(b1);
        subjMainPanel.add(subjectScrollPane);

        addRDNList(subjectTableModel, this.tdco.getxSubject500Principles().getRDNs());

        issuerTableModel.addColumn("Identificator");
        issuerTableModel.addColumn("Value");
        issuerTable.setModel(issuerTableModel);
        issuerTable.setTableHeader(null);
        issuerTable.setRowHeight(32);
        issuerTable.setShowGrid(false);
        issuerTable.setIntercellSpacing(new Dimension(15, 0));
        issuerTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        issuerTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        issuerTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        issuerTable.setSelectionModel(new NullSelectionModel());
        issuerTable.setShowGrid(false);
        issuerTable.getColumnModel().getColumn(0).setCellRenderer(new BoldViewCellrenderer());
        issuerScrollPane.getViewport().setBackground(Color.WHITE);
        TitledBorder b2 = BorderFactory.createTitledBorder("Certificate authority");
        b2.setBorder(new EtchedBorder());
        issuerScrollPane.setBorder(b2);
        subjMainPanel.add(issuerScrollPane);

        addRDNList(issuerTableModel, this.tdco.getxIssuer500Principles().getRDNs());

        tabbedPane.addTab("Certificate data", certificateScrollPane);
        certificateTableModel.addColumn("Identificator");
        certificateTableModel.addColumn("Value");
        certificateTable.setModel(certificateTableModel);
        certificateTable.setTableHeader(null);
        certificateTable.setRowHeight(32);
        certificateTable.setShowGrid(false);
        certificateTable.setIntercellSpacing(new Dimension(15, 0));
        certificateTable.getColumnModel().getColumn(0).setPreferredWidth(35);
        certificateTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        certificateTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        certificateTable.setSelectionModel(new NullSelectionModel());
        certificateTable.setShowGrid(false);
        certificateTable.getColumnModel().getColumn(0).setCellRenderer(new BoldViewCellrenderer());
        certificateScrollPane.getViewport().setBackground(Color.WHITE);

        certificateTableModel.addRow(new Object[]{"Version", "v" + String.valueOf(cert.getVersionNumber())});
        certificateTableModel.addRow(new Object[]{"Serial number", cert.getSerialNumber().toString(16).toUpperCase()});
        certificateTableModel.addRow(new Object[]{"Certificate signature algorithm", OIDmappingUtils.getAttributeValue(cert.getSignatureAlgorithm().getAlgorithm().getId())});
        certificateTableModel.addRow(new Object[]{"VALIDITY", ""});
        certificateTableModel.addRow(new Object[]{"Not before", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cert.getNotBefore())});
        certificateTableModel.addRow(new Object[]{"Not after", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cert.getNotAfter())});
        certificateTableModel.addRow(new Object[]{"PUBLIC KEY INFO", ""});
        certificateTableModel.addRow(new Object[]{"Public key algorithm", OIDmappingUtils.getAttributeValue(cert.getSubjectPublicKeyInfo().getAlgorithm().getAlgorithm().getId())});
        certificateTableModel.addRow(new Object[]{"FINGERPRINTS", ""});
        certificateTableModel.addRow(new Object[]{"SHA1 fingerprint", tdco.getSHA1Fingerprint()});
        certificateTableModel.addRow(new Object[]{"SHA-256 fingerprint", tdco.getSHA256Fingerprint()});
        certificateTableModel.addRow(new Object[]{"MD5 fingerprint", tdco.getMD5Fingerprint()});

        ktMainPanel = new JPanel();
        bBorderLayout = new BorderLayout();
        ktMainPanel.setLayout(bBorderLayout);

        extensionsSelectPanel = new JPanel(new BorderLayout());
        extensionList.addItemListener(this);

        fillExtensionList(cert);

        tabbedPane.addTab("Kt. informacija", ktMainPanel);
        otherInfoScrollPane.getViewport().setBackground(Color.WHITE);
        otherInfoArea.setLineWrap(true);
        otherInfoArea.setMargin(new Insets(10, 10, 10, 10));

        ktMainPanel.add(extensionList, BorderLayout.PAGE_START);
        ktMainPanel.add(otherInfoScrollPane, BorderLayout.CENTER);

        getContentPane().add(tabbedPane);

        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }

    private void addRDNList(DefaultTableModel tableModel, RDN[] rdns) {
        Arrays.stream(rdns).forEach(rdn -> tableModel.addRow(new Object[]{OIDmappingUtils.getAttributeValue(rdn.getTypesAndValues()[0].getType().toString()) + " (" + rdn.getTypesAndValues()[0].getType().toString() + ")", rdn.getTypesAndValues()[0].getValue().toString()}));
    }

    private void fillExtensionList(X509CertificateHolder cert) {
        this.extensionList.addItem("Select extension...");

        for (Object ext : cert.getCriticalExtensionOIDs()) {
            CertificateExtensionObject teo = new CertificateExtensionObject();
            ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) ext;
            //teo.setTokenExtensionValue(ext);
            teo.setOid(oid.getId());
            teo.setCritical(true);
            this.extensionList.addItem(teo);
        }

        for (Object ext : cert.getNonCriticalExtensionOIDs()) {
            CertificateExtensionObject teo = new CertificateExtensionObject();
            ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) ext;
            //teo.setTokenExtensionValue(ext);
            teo.setOid(oid.getId());
            teo.setCritical(false);
            this.extensionList.addItem(teo);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
            otherInfoArea.setText("");

            if (itemEvent.getItem().toString().compareToIgnoreCase("Select extension...") != 0) {
                if (itemEvent.getItem() instanceof CertificateExtensionObject) {
                    CertificateExtensionObject ceo = (CertificateExtensionObject) itemEvent.getItem();

                    if (ceo.getOid().compareToIgnoreCase("2.5.29.14") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        SubjectKeyIdentifier ski = SubjectKeyIdentifier.fromExtensions(this.cert.getExtensions());
                        otherInfoArea.append(new DEROctetString(ski.getKeyIdentifier()).toString());
                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.15") == 0) {
                        KeyUsage ku = KeyUsage.fromExtensions(this.cert.getExtensions());
                        String keyUsageCaption = "";

                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");

                        if (ku.hasUsages(KeyUsage.digitalSignature)) {
                            otherInfoArea.append(KeyUsageLocal.DigitalSignature.toString() + "\n");
                        }
                        if (ku.hasUsages(KeyUsage.nonRepudiation)) {
                            otherInfoArea.append(KeyUsageLocal.NonRepudiation.toString() + "\n");
                        }
                        if (ku.hasUsages(KeyUsage.keyEncipherment)) {
                            otherInfoArea.append(KeyUsageLocal.KeyEncipherment.toString() + "\n");
                        }
                        if (ku.hasUsages(KeyUsage.dataEncipherment)) {
                            otherInfoArea.append(KeyUsageLocal.DataEncipherment.toString() + "\n");
                        }
                        if (ku.hasUsages(KeyUsage.keyAgreement)) {
                            otherInfoArea.append(KeyUsageLocal.KeyAgreement.toString() + "\n");
                        }
                        if (ku.hasUsages(KeyUsage.keyCertSign)) {
                            otherInfoArea.append(KeyUsageLocal.KeyCertSign.toString() + "\n");
                        }
                        if (ku.hasUsages(KeyUsage.cRLSign)) {
                            otherInfoArea.append(KeyUsageLocal.CRLSign.toString() + "\n");
                        }
                        if (ku.hasUsages(KeyUsage.encipherOnly)) {
                            otherInfoArea.append(KeyUsageLocal.EncipherOnly.toString() + "\n");
                        }
                        if (ku.hasUsages(KeyUsage.decipherOnly)) {
                            otherInfoArea.append(KeyUsageLocal.DecipherOnly.toString() + "\n");
                        }
                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.16") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");

                        ASN1Encodable ato = this.cert.getExtensions().getExtensionParsedValue(Extension.privateKeyUsagePeriod);
                        PrivateKeyUsagePeriod pkup = PrivateKeyUsagePeriod.getInstance(ato);

                        try {
                            otherInfoArea.append(dt.format(pkup.getNotBefore().getDate()) + "\n");
                            otherInfoArea.append(dt.format(pkup.getNotAfter().getDate() + "\n"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.17") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        GeneralName gn = null;

                        ASN1Encodable ato = ((DLSequence) this.cert.getExtensions().getExtensionParsedValue(Extension.subjectAlternativeName)).getObjectAt(0);
                        gn = GeneralName.getInstance(ato);

                        otherInfoArea.append(TAGmappingUtils.getAttributeValue(gn.getTagNo()) + ": " + gn.getName().toString() + "\n");
                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.18") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        GeneralName gn = null;

                        ASN1Encodable ato = ((DLSequence) this.cert.getExtensions().getExtensionParsedValue(Extension.issuerAlternativeName)).getObjectAt(0);
                        gn = GeneralName.getInstance(ato);

                        otherInfoArea.append(TAGmappingUtils.getAttributeValue(gn.getTagNo()) + ": " + gn.getName().toString() + "\n");
                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.19") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        BasicConstraints bc = BasicConstraints.fromExtensions(this.cert.getExtensions());

                        if (bc.isCA()) {
                            otherInfoArea.append("Certificate is CA\n");
                        } else {
                            otherInfoArea.append("Certificate is not CA\n");
                        }

                        Optional<BigInteger> statement1 = Optional.ofNullable(bc.getPathLenConstraint());
                        statement1.ifPresent(x -> otherInfoArea.append("Path length: " + bc.getPathLenConstraint().toString() + "\n"));

                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.30") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");

                        DLSequence objs = ((DLSequence) this.cert.getExtensions().getExtensionParsedValue(Extension.nameConstraints));
                        NameConstraints nc = NameConstraints.getInstance(objs);

                        Arrays.stream(nc.getPermittedSubtrees()).forEach(pst -> otherInfoArea.append(TAGmappingUtils.getAttributeValue(pst.getBase().getTagNo()) + ": " + pst.getBase().getName().toString() + "(min=" + pst.getMinimum().toString() + " max=" + pst.getMaximum() + ")" + "\n"));
                        Arrays.stream(nc.getPermittedSubtrees()).forEach(est -> otherInfoArea.append(TAGmappingUtils.getAttributeValue(est.getBase().getTagNo()) + ": " + est.getBase().getName().toString() + "(min=" + est.getMinimum().toString() + " max=" + est.getMaximum() + ")" + "\n"));
                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.31") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");

                        DLSequence objs = ((DLSequence) this.cert.getExtensions().getExtensionParsedValue(Extension.cRLDistributionPoints));
                        CRLDistPoint crlDistPonts = CRLDistPoint.getInstance(objs);

                        Arrays.stream(crlDistPonts.getDistributionPoints()).forEach(crlPoint
                                -> {
                            GeneralNames gNames = ((GeneralNames) crlPoint.getDistributionPoint().getName());
                            Arrays.stream(gNames.getNames()).forEach(gname -> otherInfoArea.append(TAGmappingUtils.getAttributeValue(gname.getTagNo()) + ": " + gname.getName().toString() + "\n"));
                        });
                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.32") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        CertificatePolicies cp = CertificatePolicies.fromExtensions(this.cert.getExtensions());

                        Arrays.stream(cp.getPolicyInformation()).forEach(pi
                                -> {
                            otherInfoArea.append(pi.getPolicyIdentifier().getId() + "\n");
                            Enumeration pobj = pi.getPolicyQualifiers().getObjects();

                            while (pobj.hasMoreElements()) {
                                DLSequence el = (DLSequence) pobj.nextElement();
                                Enumeration seqobj = el.getObjects();
                                ASN1ObjectIdentifier oid = (ASN1ObjectIdentifier) seqobj.nextElement();
                                otherInfoArea.append("\n" + oid.getId() + "\n");
                                ASN1Primitive pol = (ASN1Primitive) seqobj.nextElement();
                                otherInfoArea.append(pol.toASN1Primitive().toString() + "\n\n");
                            }
                        });
                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.35") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        AuthorityKeyIdentifier aki = AuthorityKeyIdentifier.fromExtensions(this.cert.getExtensions());

                        Optional<BigInteger> statement1 = Optional.ofNullable(aki.getAuthorityCertSerialNumber());
                        statement1.ifPresent(x -> otherInfoArea.append(aki.getAuthorityCertSerialNumber().toString() + "\n"));

                        Optional<GeneralNames> statement2 = Optional.ofNullable(aki.getAuthorityCertIssuer());
                        statement2.ifPresent(x -> Arrays.stream(aki.getAuthorityCertIssuer().getNames()).forEach(gname -> otherInfoArea.append(gname.getName().toString() + "\n")));

                        otherInfoArea.append(new DEROctetString(aki.getKeyIdentifier()).toString());

                    } else if (ceo.getOid().compareToIgnoreCase("2.5.29.37") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        ExtendedKeyUsage eku = ExtendedKeyUsage.fromExtensions(this.cert.getExtensions());
                        Arrays.stream(eku.getUsages()).forEach(usages -> otherInfoArea.append(OIDmappingUtils.getAttributeValue(usages.getId()) + " (" + usages.getId() + ")" + "\n"));
                    } else if (ceo.getOid().compareToIgnoreCase("1.3.6.1.4.1.311.21.10") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        Extension ext = this.cert.getExtensions().getExtension(new ASN1ObjectIdentifier(ceo.getOid()));
                        ASN1OctetString extVal = ext.getExtnValue();
                        otherInfoArea.append(extVal.toString() + "\n");

                    } else if (ceo.getOid().compareToIgnoreCase("1.3.6.1.4.1.311.21.7") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        Extension ext = this.cert.getExtensions().getExtension(new ASN1ObjectIdentifier(ceo.getOid()));
                        ASN1OctetString extVal = ext.getExtnValue();
                        otherInfoArea.append(extVal.toString() + "\n");
                    } else if (ceo.getOid().compareToIgnoreCase("1.3.6.1.5.5.7.1.1") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        DLSequence objs = ((DLSequence) this.cert.getExtensions().getExtensionParsedValue(new ASN1ObjectIdentifier(ceo.getOid())));
                        AuthorityInformationAccess aia = AuthorityInformationAccess.getInstance(objs);
                        Arrays.stream(aia.getAccessDescriptions()).forEach(x -> otherInfoArea.append(TAGmappingUtils.getAttributeValue(x.getAccessLocation().getTagNo()) + ": " + x.getAccessLocation().getName().toString() + "n"));
                    } else if (ceo.getOid().compareToIgnoreCase("2.16.840.1.113730.1.1") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");
                        ASN1ObjectIdentifier ncto = new ASN1ObjectIdentifier(ceo.getOid());
                        DERBitString derBits = ((DERBitString) this.cert.getExtensions().getExtensionParsedValue(ncto));
                        NetscapeCertType nst = new NetscapeCertType(derBits);

                        if ((nst.intValue() & nst.objectSigning) == nst.objectSigning) {
                            otherInfoArea.append("Object Signing\n");
                        }

                        if ((nst.intValue() & nst.objectSigningCA) == nst.objectSigningCA) {
                            otherInfoArea.append("Object Signing CA\n");
                        }

                        if ((nst.intValue() & nst.reserved) == nst.reserved) {
                            otherInfoArea.append("RFU\n");
                        }

                        if ((nst.intValue() & nst.smime) == nst.smime) {
                            otherInfoArea.append("S/MIME\n");
                        }

                        if ((nst.intValue() & nst.smimeCA) == nst.smimeCA) {
                            otherInfoArea.append("S/MIME CA\n");
                        }

                        if ((nst.intValue() & nst.sslCA) == nst.sslCA) {
                            otherInfoArea.append("SSL CA\n");
                        }

                        if ((nst.intValue() & nst.sslClient) == nst.sslClient) {
                            otherInfoArea.append("SSL client\n");
                        }

                        if ((nst.intValue() & nst.sslServer) == nst.sslServer) {
                            otherInfoArea.append("SSL server\n");
                        }
                    } else if (ceo.getOid().compareToIgnoreCase("1.3.6.1.5.5.7.1.3") == 0) {
                        otherInfoArea.append(ceo.getOid() + " " + ceo.getCriticalCaption() + "\n");

                        DLSequence objs = ((DLSequence) this.cert.getExtensions().getExtensionParsedValue(Extension.qCStatements));

                        Enumeration objEnums = objs.getObjects();

                        while (objEnums.hasMoreElements()) {
                            DLSequence objEl = (DLSequence) objEnums.nextElement();
                            QCStatement qc = QCStatement.getInstance(objEl);

                            Optional<ASN1ObjectIdentifier> statement1 = Optional.ofNullable(qc.getStatementId());
                            statement1.ifPresent(x -> otherInfoArea.append("\n" + qc.getStatementId().toString() + "\n"));

                            Optional<ASN1Encodable> statement2 = Optional.ofNullable(qc.getStatementInfo());
                            statement2.ifPresent(x -> otherInfoArea.append(qc.getStatementInfo().toString() + "\n"));
                        }
                    }
                }
            } else {
                System.out.println("nepasirinktas");
            }
        }
    }
}
