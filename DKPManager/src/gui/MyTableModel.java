package gui;

import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import model.QueryHandler;
import model.WOWCharacter;

@SuppressWarnings("serial")
public class MyTableModel extends DefaultTableModel {

    private static MyTableModel instance_ = null;
    private static List<WOWCharacter> characters_;
    private static String[] columnNames = { "Character", "DKP" };
    private static String[][] data;
    
    private MyTableModel(String[][] data, String[] columnNames) {
        super(data, columnNames);
    }
    
    public static MyTableModel getInstance() {
        if (instance_ == null) {
            characters_ = QueryHandler.getAllCharacters();
            data = new String[characters_.size()][2];
            int i = 0;
            for (WOWCharacter c : characters_) {
                data[i][0] = c.getName() + "-" + c.getRealm();
                data[i][1] = String.valueOf(c.getTotalDKP());
                ++i;
            }
            instance_ = new MyTableModel(data, columnNames);
        }
        return instance_;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
    
    public static void refreshTableData() {
        characters_ = QueryHandler.getAllCharacters();
        data = new String[characters_.size()][2];
        int i = 0;
        for (WOWCharacter c : characters_) {
            data[i][0] = c.getName() + "-" + c.getRealm();
            data[i][1] = String.valueOf(c.getTotalDKP());
            ++i;
        }
        MyTableModel.getInstance().setDataVector(data, columnNames);
        TableModelEvent e;
        e = new TableModelEvent(MyTableModel.getInstance());
        MyTableModel.getInstance().fireTableChanged(e);;
    }
}