package gui;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class UnmodifiableTableModel extends DefaultTableModel {
    
    public UnmodifiableTableModel(String[][] data, String[] columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
