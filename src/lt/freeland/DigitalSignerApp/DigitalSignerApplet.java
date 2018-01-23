package lt.freeland.DigitalSignerApp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyStore;
import java.security.Security;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EtchedBorder;

import javax.swing.JMenuBar;

import lt.freeland.DigitalSignerApp.Components.TokenListViewer;
import lt.freeland.DigitalSignerApp.DialogWindows.FileListDialog;
import lt.freeland.DigitalSignerApp.SignerObjects.TokenObject;
import lt.freeland.DigitalSignerApp.Tasks.CardDetectionTask;
import lt.freeland.DigitalSignerApp.Utils.HelperUtils;

import javax.swing.JLabel;
import javax.swing.JMenu;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class DigitalSignerApplet extends JFrame implements ActionListener, PropertyChangeListener {

    private static final long serialVersionUID = 2269886423056017750L;
    private JFileChooser fileChooser;

    private JTextField textField;
    private JPanel fileSelectPanel;
    private JPanel statusBarPanel;
    private JButton btnBrowse;
    private JButton btnSign;
    private JMenuBar menuBar;
    private JLabel infoLabel;
    private TokenListViewer tokenList;

    private KeyStore.CallbackHandlerProtection chp;

    private FileListDialog fileListDialog;

    private InputStream in;

    private CardDetectionTask c_DetectionTask;

    private BoxLayout bLayout;

    public static String providerPath;

    public static void main(String args[]) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    new DigitalSignerApplet().initGUI();
                }
            }
            );
        } catch (InvocationTargetException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void initGUI() {
        fileChooser = new JFileChooser();

        menuBar = new JMenuBar();
        menuBar.add(new JMenu("File"));
        menuBar.add(new JMenu("Help"));

        setJMenuBar(menuBar);

        BorderLayout bl = new BorderLayout();
        fileSelectPanel = new JPanel(bl);

        textField = new JTextField(40);
        textField.setEditable(false);
        fileSelectPanel.add(textField, BorderLayout.CENTER);

        btnBrowse = new JButton("Browse");
        btnBrowse.addActionListener(this);
        fileSelectPanel.add(btnBrowse, BorderLayout.EAST);

        btnSign = new JButton("Sign document");
        btnSign.addActionListener(this);
        btnSign.setEnabled(false);
        fileSelectPanel.add(btnSign, BorderLayout.WEST);

        this.getContentPane().add(fileSelectPanel, BorderLayout.BEFORE_FIRST_LINE);

        tokenList = new TokenListViewer(this);
        this.getContentPane().add(tokenList);

        statusBarPanel = new JPanel(new BorderLayout());
        statusBarPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        infoLabel = new JLabel("");
        statusBarPanel.add(infoLabel);
        this.getContentPane().add(statusBarPanel, BorderLayout.AFTER_LAST_LINE);

        /*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				this.setLocation(((screenSize.width / 2) - (this.getWidth() / 2)), ((screenSize.height / 2) - (this.getHeight() / 2)) );*/
        this.setLocationRelativeTo(null);

        //this.setResizable(false);
        this.setSize(650, 400);
        this.setTitle("Digital signer app v0.1");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);

        Security.addProvider(new BouncyCastleProvider());
        //this.pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBrowse) {
            int returnVal = fileChooser.showOpenDialog(DigitalSignerApplet.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                providerPath = HelperUtils.getProviderLibraryPath(file.getAbsolutePath());

                textField.setEditable(true);
                textField.setText(file.getAbsolutePath());
                textField.setEditable(false);

                if (c_DetectionTask != null && !c_DetectionTask.isCancelled()) {
                    c_DetectionTask.cancel(true);
                    c_DetectionTask.removePropertyChangeListener(this);
                }

                c_DetectionTask = new CardDetectionTask(tokenList.getTreeModel(), providerPath);
                c_DetectionTask.addPropertyChangeListener(this);
                c_DetectionTask.execute();
            }
        } else if (e.getSource() == btnSign) {
            TokenObject to = tokenList.getSelectedTokenObject();

            if (tokenList.getSelectedTokenObject() != null) {
                fileListDialog = new FileListDialog(to);
                fileListDialog.setVisible(true);
            }
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        System.out.println(evt.getPropertyName() + ":" + evt.getNewValue().toString());
    }

    public void enableSignButton(boolean enable) {
        this.btnSign.setEnabled(enable);
    }
}
