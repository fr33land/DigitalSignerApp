package lt.freeland.DigitalSignerApp.Tasks;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.Provider;
import java.security.Security;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import lt.freeland.DigitalSignerApp.C_Functions;
import lt.freeland.DigitalSignerApp.SignerObjects.ReaderDeviceObject;
import lt.freeland.DigitalSignerApp.SignerObjects.TokenObject;
import lt.freeland.DigitalSignerApp.Utils.HelperUtils;
import sun.security.pkcs11.wrapper.CK_SLOT_INFO;
import sun.security.pkcs11.wrapper.CK_TOKEN_INFO;
import sun.security.pkcs11.wrapper.PKCS11Exception;

public class CardDetectionTask extends SwingWorker<Void, long[]> {

    private DefaultTreeModel jTreeModel;
    private C_Functions c_func;
    private int i;
    private String providerPath;

    public CardDetectionTask(DefaultTreeModel treeModel, String providerPath) {
        this.jTreeModel = treeModel;
        this.c_func = new C_Functions(providerPath);
        this.providerPath = providerPath;
        i = 0;
    }

    @Override
    protected Void doInBackground() throws Exception {
        while (true) {
            publish(c_func.C_GetSlotList(true));
            Thread.sleep(100);
        }
    }

    @Override
    protected void process(List<long[]> chunks) {
        long[] slots = chunks.get(chunks.size() - 1);

        for (long slot : slots) {
            try {
                CK_SLOT_INFO slotNfo = c_func.C_GetSlotInfo(slot);

                String slotDesc = new String(slotNfo.slotDescription).trim() + " (ID:" + String.valueOf(slot) + ")";

                Enumeration<DefaultMutableTreeNode> cardReadersNodes = ((DefaultMutableTreeNode) jTreeModel.getRoot()).children();
                long found = 0L;

                found = Collections.list(cardReadersNodes).stream().filter(y -> slotDesc.compareTo(y.getUserObject().toString()) == 0).count();

                if (found == 0L) {
                    ReaderDeviceObject rdo = new ReaderDeviceObject(slotDesc);
                    DefaultMutableTreeNode root = (DefaultMutableTreeNode) jTreeModel.getRoot();
                    DefaultMutableTreeNode slotNode = new DefaultMutableTreeNode(rdo);

                    SwingUtilities.invokeLater(() -> {
                        try {
                            CK_TOKEN_INFO tokenNfo = c_func.C_GetTokenInfo(slot);

                            byte[] reconv = new String(tokenNfo.label).getBytes(Charset.forName("ISO-8859-1"));
                            String decoded = URLDecoder.decode(new String(reconv), "UTF-8");

                            Provider pkcs11Provider = HelperUtils.buildProvider("PKCS11_" + Long.toString(slot), providerPath, slot);
                            Security.addProvider(pkcs11Provider);

                            TokenObject to = new TokenObject(slot, decoded.trim(), c_func, pkcs11Provider);
                            DefaultMutableTreeNode tokenObjNode = new DefaultMutableTreeNode(to);
                            slotNode.add(tokenObjNode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        jTreeModel.insertNodeInto(slotNode, root, root.getChildCount());
                    });
                    jTreeModel.reload(root);
                }
            } catch (PKCS11Exception e) {
                e.printStackTrace();
            }
        }

        Enumeration<DefaultMutableTreeNode> cardReadersNodes = ((DefaultMutableTreeNode) jTreeModel.getRoot()).children();
        while (cardReadersNodes.hasMoreElements()) {
            DefaultMutableTreeNode currentNode = cardReadersNodes.nextElement();
            String slotCurrentDescription = currentNode.getUserObject().toString();

            boolean found = false;

            for (long slot : slots) {
                CK_SLOT_INFO slotNfo;
                try {
                    slotNfo = c_func.C_GetSlotInfo(slot);
                    String slotDesc = new String(slotNfo.slotDescription).trim() + " (ID:" + String.valueOf(slot) + ")";

                    if (slotCurrentDescription.compareTo(slotDesc) == 0) {
                        found = true;
                    }
                } catch (PKCS11Exception e) {
                    e.printStackTrace();
                }
            }

            if (!found) {
                DefaultMutableTreeNode root = (DefaultMutableTreeNode) jTreeModel.getRoot();
                jTreeModel.removeNodeFromParent(currentNode);
                jTreeModel.reload(root);
            }
        }
    }
}
