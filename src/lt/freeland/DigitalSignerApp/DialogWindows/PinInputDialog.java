package lt.freeland.DigitalSignerApp.DialogWindows;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Panel;
import javax.swing.JCheckBox;
import javax.swing.border.EtchedBorder;

import lt.freeland.DigitalSignerApp.C_Functions;
import lt.freeland.DigitalSignerApp.DigitalSignerApplet;
import sun.security.pkcs11.wrapper.CK_TOKEN_INFO;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.pkcs11.wrapper.PKCS11Constants;

public class PinInputDialog extends JDialog implements ActionListener {

    private final JPanel contentPanel = new JPanel();
    private JButton okButton;
    private JPasswordField passwordField;
    private JLabel lblPinKodas;
    private JLabel lblLikoNBandymai;
    private JButton cancelButton;
    private JCheckBox chckbxAtsimintiPin;
    private CK_TOKEN_INFO nfo;

    public static final int PIN_OK = 0x00;
    public static final int PIN_CANCEL = 0x01;
    public static final int PIN_LOCKED = 0x02;

    int result;
    private static char[] pin;

    public PinInputDialog(long slot_) {
        this.getContentPane().setLayout(null);
        this.setTitle("Enter PIN code");
        this.setBounds(100, 100, 293, 200);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        contentPanel.setBounds(12, 0, 267, 34);
        FlowLayout fl_contentPanel = new FlowLayout();
        fl_contentPanel.setHgap(10);
        contentPanel.setLayout(fl_contentPanel);
        contentPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        getContentPane().add(contentPanel);

        lblPinKodas = new JLabel("Pin code ");
        contentPanel.add(lblPinKodas);

        passwordField = new JPasswordField();
        passwordField.setColumns(15);
        contentPanel.add(passwordField);

        JPanel buttonPane = new JPanel();
        buttonPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
        buttonPane.setBounds(12, 103, 267, 39);
        buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        getContentPane().add(buttonPane);

        okButton = new JButton("OK");
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setActionCommand("Cancel");
        cancelButton.addActionListener(this);
        buttonPane.add(cancelButton);

        Panel panel = new Panel();
        panel.setBounds(22, 40, 246, 57);
        getContentPane().add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));

        lblLikoNBandymai = new JLabel("3 times left");
        panel.add(lblLikoNBandymai);

        chckbxAtsimintiPin = new JCheckBox("Remember PIN");
        panel.add(chckbxAtsimintiPin);

        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        C_Functions cfe = new C_Functions(DigitalSignerApplet.providerPath);

        try {
            nfo = cfe.C_GetTokenInfo(slot_);
        } catch (PKCS11Exception e) {
            e.printStackTrace();
        }

        if ((nfo.flags & PKCS11Constants.CKF_USER_PIN_LOCKED) == PKCS11Constants.CKF_USER_PIN_LOCKED) {
            JOptionPane.showMessageDialog(this, "CARD IS BLOCKED", "Error: signing operation is not possible", JOptionPane.ERROR_MESSAGE);
            lblLikoNBandymai.setText("<html><font color='red'>CARD IS BLOCKED !!!</font></html>");
            result = PIN_LOCKED;
        } else if ((nfo.flags & PKCS11Constants.CKF_USER_PIN_FINAL_TRY) == PKCS11Constants.CKF_USER_PIN_FINAL_TRY) {
            lblLikoNBandymai.setText("<html><font color='red'>1 time left !!!</font></html>");
            this.setVisible(true);
        } else if ((nfo.flags & PKCS11Constants.CKF_USER_PIN_COUNT_LOW) == PKCS11Constants.CKF_USER_PIN_COUNT_LOW) {
            lblLikoNBandymai.setText("2 times left");
            this.setVisible(true);
        } else {
            this.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            if (passwordField.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, "Please enter PIN code", "Error", JOptionPane.WARNING_MESSAGE);
            } else if (passwordField.getPassword().length < nfo.ulMinPinLen) {
                JOptionPane.showMessageDialog(this, "Min. PIN code length is " + Long.toString(nfo.ulMinPinLen), "Error", JOptionPane.WARNING_MESSAGE);
            } else if (passwordField.getPassword().length > nfo.ulMaxPinLen) {
                JOptionPane.showMessageDialog(this, "Max. PIN code length is " + Long.toString(nfo.ulMaxPinLen), "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                this.result = PIN_OK;
                this.pin = passwordField.getPassword();
                this.dispose();
            }
        } else if (e.getSource() == cancelButton) {
            this.result = PIN_CANCEL;
            this.dispose();
        }
    }

    public static char[] getPin() {
        return pin;
    }

    public int getStatus() {
        return this.result;
    }
}
