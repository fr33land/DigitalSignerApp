package lt.freeland.DigitalSignerApp.Utils;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

public class NullSelectionModel implements ListSelectionModel {

    @Override
    public boolean isSelectionEmpty() {
        return true;
    }

    @Override
    public boolean isSelectedIndex(int index) {
        return false;
    }

    @Override
    public int getMinSelectionIndex() {
        return -1;
    }

    @Override
    public int getMaxSelectionIndex() {
        return -1;
    }

    @Override
    public int getLeadSelectionIndex() {
        return -1;
    }

    @Override
    public int getAnchorSelectionIndex() {
        return -1;
    }

    @Override
    public void setSelectionInterval(int index0, int index1) {
    }

    @Override
    public void setLeadSelectionIndex(int index) {
    }

    @Override
    public void setAnchorSelectionIndex(int index) {
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {
    }

    @Override
    public void insertIndexInterval(int index, int length, boolean before) {
    }

    @Override
    public void clearSelection() {
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
    }

    @Override
    public void removeIndexInterval(int index0, int index1) {
    }

    @Override
    public void setSelectionMode(int selectionMode) {
    }

    @Override
    public int getSelectionMode() {
        return SINGLE_SELECTION;
    }

    @Override
    public void addListSelectionListener(ListSelectionListener lsl) {
    }

    @Override
    public void removeListSelectionListener(ListSelectionListener lsl) {
    }

    @Override
    public void setValueIsAdjusting(boolean valueIsAdjusting) {
    }

    @Override
    public boolean getValueIsAdjusting() {
        return false;
    }
}
