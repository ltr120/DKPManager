package gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;

import model.Boss;
import model.Constants;
import model.Constants.DKPEventType;
import model.Constants.Difficulty;
import model.DKPEvent;
import model.Item;
import model.NotEnoughDKPException;
import model.QueryHandler;
import model.WOWCharacter;

@SuppressWarnings("serial")
public class ManipulationPanel extends JPanel {

    private LayoutManager primeLM_;
    private List<WOWCharacter> characters_;

    private JButton addCharButton_;
    private JButton delCharButton_;
    private JButton decayButton_;
    private JButton addEventButton_;

    private JTable table_;
    private JTextField score_;
    private JTextField itemName_;
    private JTextField desc_;

    private JComboBox<String> eventTypeList_;
    private JComboBox<String> bossList_;
    private JComboBox<String> difficultyList_;

    public ManipulationPanel() {
        primeLM_ = new BorderLayout();
        this.setLayout(primeLM_);
        setCharacterTable();
        setSouthPanel();
    }

    private void setSouthPanel() {
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(4, 4));
        JLabel l;

        // row 0
        l = new JLabel("Action: ", SwingConstants.RIGHT);
        southPanel.add(l);
        String[] jlistData = DKPEventType.names();
        eventTypeList_ = new JComboBox<String>(jlistData);
        southPanel.add(eventTypeList_);
        l = new JLabel("Score: ", SwingConstants.RIGHT);
        southPanel.add(l);
        NumberFormat nf = NumberFormat.getNumberInstance();
        score_ = new JFormattedTextField(nf);
        southPanel.add(score_);

        // row 1
        l = new JLabel("Boss: ", SwingConstants.RIGHT);
        southPanel.add(l);
        Set<Entry<String, Boss>> entrySet = Constants.bossMap.entrySet();
        Iterator<Entry<String, Boss>> it = entrySet.iterator();
        String[] bossNameData = new String[entrySet.size()];
        for (int i = 0; i < entrySet.size(); ++i) { // how to handle ordering?
            Entry<String, Boss> e = it.next();
            bossNameData[i] = e.getKey() + " - " + e.getValue().getRaid();
        }
        bossList_ = new JComboBox<String>(bossNameData);
        southPanel.add(bossList_);
        l = new JLabel("Difficulty: ", SwingConstants.RIGHT);
        southPanel.add(l);
        difficultyList_ = new JComboBox<String>(Constants.Difficulty.names());
        southPanel.add(difficultyList_);

        // row 2
        l = new JLabel("Item: ", SwingConstants.RIGHT);
        southPanel.add(l);
        itemName_ = new JTextField();
        southPanel.add(itemName_);
        l = new JLabel("Event Description: ", SwingConstants.RIGHT);
        southPanel.add(l);
        desc_ = new JTextField();
        southPanel.add(desc_);

        // row 3
        addCharButton_ = new JButton(Constants.MANIPULATION_ADD_CHAR_BUTT_NAME);
        addCharButton_.addActionListener(new MyButtonListener());
        southPanel.add(addCharButton_);
        delCharButton_ = new JButton(Constants.MANIPULATION_DEL_CHAR_BUTT_NAME);
        delCharButton_.addActionListener(new MyButtonListener());
        southPanel.add(delCharButton_);
        decayButton_ = new JButton(Constants.MANIPULATION_DECAY_BUTT_NAME);
        decayButton_.addActionListener(new MyButtonListener());
        southPanel.add(decayButton_);
        addEventButton_ = new JButton(
                Constants.MANIPULATION_ADD_EVENT_BUTT_NAME);
        addEventButton_.addActionListener(new MyButtonListener());
        southPanel.add(addEventButton_);

        // Add to mother panel
        this.add(southPanel, BorderLayout.SOUTH);
    }

    private void setCharacterTable() {
        characters_ = QueryHandler.getAllCharacters();
        table_ = new JTable(MyTableModel.getInstance());
        table_.setDefaultRenderer(Object.class, MyClassColorRenderer.getInstance());

        // formatting
        TableColumnModel tcm = table_.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(300);
        tcm.getColumn(1).setPreferredWidth(40);
        // I don't know if there is other ways to preserve the
        // column width after table model changes. So I would
        // just go with this way -- adding a table model listener.
        table_.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent arg0) {
                characters_ = QueryHandler.getAllCharacters();
                MyClassColorRenderer.setCharacters(characters_);
                TableColumnModel tcm = table_.getColumnModel();
                tcm.getColumn(0).setPreferredWidth(300);
                tcm.getColumn(1).setPreferredWidth(40);
            }

        });

        // disallow dragging. enable multi-selection
        table_.getTableHeader().setReorderingAllowed(false);
        table_.getTableHeader().setResizingAllowed(false);
        table_.setRowSelectionAllowed(true);
        table_.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // add to panel
        JScrollPane sp = new JScrollPane(table_);
        this.add(sp, BorderLayout.CENTER);

        // add hint label to panel
        JLabel hint = new JLabel("Select Character(s) for DKP manipulation "
                + "(Hint: hold SHIFT or CTRL key to "
                + "select multiple characters.)");
        this.add(hint, BorderLayout.NORTH);
    }

    private class MyButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (arg0.getSource() instanceof JButton) {
                JButton b = (JButton) arg0.getSource();
                if (Constants.MANIPULATION_ADD_CHAR_BUTT_NAME.equals(b
                        .getText())) {
                    JPanel newCharPanel = new JPanel(new GridLayout(0, 1));
                    newCharPanel.add(new JLabel("Character Name: "));
                    JTextField name = new JTextField();
                    newCharPanel.add(name);
                    newCharPanel.add(new JLabel("Character Realm: "));
                    JTextField realm = new JTextField(Constants.DEFAULT_REALM);
                    newCharPanel.add(realm);
                    newCharPanel.add(new JLabel("Character Class:"));
                    JComboBox<String> classList = new JComboBox<String>(
                            Constants.WOWCharacterClass.names());
                    newCharPanel.add(classList);
                    int result = JOptionPane.showConfirmDialog(null,
                            newCharPanel,
                            Constants.MANIPULATION_ADD_CHAR_BUTT_NAME,
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION) {
                        String nameString = name.getText();
                        String realmString = realm.getText();
                        if (nameString.length() == 0) {
                            JOptionPane.showMessageDialog(null,
                                    "Character name cannot be blank!");
                            return;
                        }
                        if (realmString.length() == 0) {
                            realmString = Constants.DEFAULT_REALM;
                        }
                        WOWCharacter newChar = new WOWCharacter(nameString,
                                realmString, Constants.WOWCharacterClass.valueOf((String) classList.getSelectedItem()));
                        QueryHandler.addCharacter(newChar);
                        // update tables
                        MyTableModel.refreshTableData();
                    }
                } else if (Constants.MANIPULATION_DEL_CHAR_BUTT_NAME.equals(b
                        .getText())) {
                    int[] rows = table_.getSelectedRows();
                    StringBuilder sb = new StringBuilder();
                    List<WOWCharacter> toDelete = new LinkedList<WOWCharacter>();
                    for (int i : rows) {
                        toDelete.add(characters_.get(i));
                        sb.append(characters_.get(i).getName() + " - "
                                + characters_.get(i).getRealm() + "\n");
                    }
                    int result = JOptionPane.showConfirmDialog(null,
                            "Are you sure to delete the following characters?\n"
                                    + sb.toString());
                    if (result == JOptionPane.OK_OPTION) {
                        for (WOWCharacter c : toDelete) {
                            QueryHandler.deleteCharacter(c);
                        }
                        // update tables
                        MyTableModel.refreshTableData();
                    }
                } else if (Constants.MANIPULATION_DECAY_BUTT_NAME.equals(b
                        .getText())) {
                    QueryHandler.weeklyDecay();
                    // update tables
                    MyTableModel.refreshTableData();
                } else if (Constants.MANIPULATION_ADD_EVENT_BUTT_NAME.equals(b
                        .getText())) {
                    int[] rows = table_.getSelectedRows();
                    DKPEvent e;
                    List<WOWCharacter> exceptionList = new LinkedList<WOWCharacter>();
                    for (int i : rows) {
                        String itemName = itemName_.getText();
                        if ("n/a".equals(itemName) || "-".equals(itemName)) {
                            itemName = "";
                        }
                        e = new DKPEvent(
                                Integer.parseInt(score_.getText().replaceAll(
                                        ",", "")),
                                DKPEventType
                                        .getDKPEventTypeByString((String) eventTypeList_
                                                .getSelectedItem()),
                                desc_.getText().length() == 0 ? Constants.DEFAULT_EVENT_DESCRIPTION
                                        : desc_.getText(),
                                characters_.get(i),
                                // TODO: Item should be fancier
                                itemName.length() == 0 ? null : new Item(
                                        Constants.DEFAULT_ITEM_ID, itemName),
                                Constants.bossMap.get(((String) bossList_
                                        .getSelectedItem()).substring(0,
                                        ((String) bossList_.getSelectedItem())
                                                .indexOf(" - "))),
                                Difficulty
                                        .getDifficultyString((String) difficultyList_
                                                .getSelectedItem()));
                        try {
                            characters_.get(i).addDKPEvent(e);
                        } catch (NotEnoughDKPException e1) {
                            exceptionList.add(characters_.get(i));
                        }
                    }
                    if (!exceptionList.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        for (WOWCharacter c : exceptionList) {
                            sb.append(c.getName() + " - " + c.getRealm() + "\n");
                        }
                        JOptionPane.showMessageDialog(null,
                                "Not enough DKP for following characters:\n"
                                        + sb.toString());
                    }

                    // update tables
                    MyTableModel.refreshTableData();
                }
            }
        }

    }
}
