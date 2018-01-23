package lt.freeland.DigitalSignerApp.DialogWindows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;

import lt.freeland.DigitalSignerApp.Components.TokenObjectListViewer;
import lt.freeland.DigitalSignerApp.SignerObjects.TokenObject;

public class TokenObjectListDialog extends JDialog implements ActionListener {

    private final JPanel contentPanel = new JPanel();
    private TokenObjectListViewer tolv;
    private TokenObject to;

    private BoxLayout bLayout;

    public TokenObjectListDialog(TokenObject to) {
        bLayout = new BoxLayout(getContentPane(), BoxLayout.Y_AXIS);
        this.getContentPane().setLayout(bLayout);
        this.setTitle("File list");
        this.setBounds(0, 0, 650, 400);
        getContentPane().add(contentPanel);

        tolv = new TokenObjectListViewer(to);
        getContentPane().add(tolv);

        this.to = to;
        this.setModal(true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
