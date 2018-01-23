package lt.freeland.DigitalSignerApp.Components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collections;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import lt.freeland.DigitalSignerApp.DigitalSignerApplet;
import lt.freeland.DigitalSignerApp.DialogWindows.TokenObjectListDialog;
import lt.freeland.DigitalSignerApp.SignerObjects.TokenObject;
import lt.freeland.DigitalSignerApp.Utils.TokenViewCellrenderer;

public class TokenListViewer extends JScrollPane implements TreeModelListener, TreeSelectionListener, MouseListener {

    private JTree jTreeList;
    private DefaultTreeModel treeModel;
    private TokenObjectListDialog tokenObjectListDialog;
    private DigitalSignerApplet parentApplet;

    public TokenListViewer(DigitalSignerApplet digitalSignerApplet) {

        treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Card readers"));
        treeModel.addTreeModelListener(this);

        jTreeList = new JTree(treeModel);
        jTreeList.putClientProperty("JTree.lineStyle", "None");
        jTreeList.setShowsRootHandles(false);
        jTreeList.setRootVisible(false);
        TokenViewCellrenderer tokenViewCellrenderer = new TokenViewCellrenderer();
        jTreeList.setCellRenderer(tokenViewCellrenderer);
        jTreeList.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        jTreeList.addTreeSelectionListener(this);
        jTreeList.addMouseListener(this);

        this.setViewportView(jTreeList);

        this.parentApplet = digitalSignerApplet;
    }

    @Override
    public void treeNodesChanged(TreeModelEvent arg0) {

    }

    @Override
    public void treeNodesInserted(TreeModelEvent arg0) {
        expandAll(jTreeList, new TreePath((TreeNode) jTreeList.getModel().getRoot()));
    }

    @Override
    public void treeNodesRemoved(TreeModelEvent arg0) {
        expandAll(jTreeList, new TreePath((TreeNode) jTreeList.getModel().getRoot()));
    }

    @Override
    public void treeStructureChanged(TreeModelEvent arg0) {
        expandAll(jTreeList, new TreePath((TreeNode) jTreeList.getModel().getRoot()));
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    private void expandAll(JTree tree, TreePath parent) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            Collections.list(node.children()).forEach(e -> expandAll(tree, parent.pathByAddingChild((TreeNode) e)));
        }
        tree.expandPath(parent);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int selRow = jTreeList.getRowForLocation(e.getX(), e.getY());

        if (selRow != -1) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeList.getLastSelectedPathComponent();
            if (node == null) {
                return;
            }

            if (e.getClickCount() == 1) {
                if (node.getUserObject() instanceof TokenObject) {
                    parentApplet.enableSignButton(true);
                } else {
                    parentApplet.enableSignButton(false);
                }
            } else if (e.getClickCount() == 2) {
                if (node.getUserObject() instanceof TokenObject) {
                    TokenObject to = (TokenObject) node.getUserObject();
                    tokenObjectListDialog = new TokenObjectListDialog(to);
                    tokenObjectListDialog.setTitle(to.getObjectData().toString() + " objektų sąra�?as");
                    tokenObjectListDialog.setVisible(true);
                    tokenObjectListDialog.setLocationRelativeTo(null);

                    parentApplet.enableSignButton(true);
                } else {
                    parentApplet.enableSignButton(false);
                }
            }
        } else {
            parentApplet.enableSignButton(false);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    public TokenObject getSelectedTokenObject() {
        if (!jTreeList.isSelectionEmpty()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTreeList.getLastSelectedPathComponent();
            if (node == null) {
                return null;
            }

            if (node.getUserObject() instanceof TokenObject) {
                return (TokenObject) node.getUserObject();
            }
        }

        return null;
    }
}
