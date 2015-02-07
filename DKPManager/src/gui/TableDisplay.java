package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import model.Constants;
import model.DKPEvent;
import model.QueryHandler;
import model.WOWCharacter;

@SuppressWarnings("serial")
public class TableDisplay extends JPanel {

    private CardLayout cards_;
    private LayoutManager lmTable_;
    private JTable table_;
    private List<WOWCharacter> characters_;
    private JPanel tablePanel_;
    private CharacterPanel characterPanel_;

    public TableDisplay() {
        cards_ = new CardLayout();
        this.setLayout(cards_);

        // Card 1: Table panel
        tablePanel_ = new JPanel();
        lmTable_ = new BorderLayout();
        tablePanel_.setLayout(lmTable_);
        setTableHeader();
        setDKPTable();

        // Card 2: Character panel
        characterPanel_ = new CharacterPanel(null);

        // Add cards for card panel
        this.add(tablePanel_, Constants.TABLEDISPLAY_CARD1);
        this.add(characterPanel_, Constants.TABLEDISPLAY_CARD2);

        // display table panel
        cards_.show(this, Constants.TABLEDISPLAY_CARD1);
    }

    private void setTableHeader() {
        JLabel label = new JLabel("DKP Summary:");
        label.setBounds(10, 10, 100, 20);
        tablePanel_.add(label, BorderLayout.NORTH);
    }

    private void setDKPTable() {
        characters_ = QueryHandler.getAllCharacters();
        table_ = new JTable(MyTableModel.getInstance());

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
                TableColumnModel tcm = table_.getColumnModel();
                tcm.getColumn(0).setPreferredWidth(300);
                tcm.getColumn(1).setPreferredWidth(40);
            }

        });

        // add double click listener and disallow dragging and resizing
        table_.addMouseListener(new MyTableMouseAdapter());
        table_.getTableHeader().setReorderingAllowed(false);
        table_.getTableHeader().setResizingAllowed(false);
        table_.setRowSelectionAllowed(true);
        table_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // add to panel
        JScrollPane sp = new JScrollPane(table_);
        tablePanel_.add(sp, BorderLayout.CENTER);
    }

    private class MyTableMouseAdapter extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                if (e.getSource() instanceof JTable) {
                    JTable target = (JTable) e.getSource();
                    characterPanel_.setCharacter(characters_.get(target
                            .getSelectedRow()));
                    characterPanel_.reloadContent();
                    cards_.show(TableDisplay.this, Constants.TABLEDISPLAY_CARD2);
                }
            }
        }
    }

    private class MyButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            if (arg0.getSource() instanceof JButton) {
                JButton b = (JButton) arg0.getSource();
                if ("<= Back to Table".equals(b.getText())) {
                    cards_.show(TableDisplay.this, Constants.TABLEDISPLAY_CARD1);
                }
            }
        }

    }

    private class CharacterPanel extends JPanel {

        private JLabel name;
        private JLabel realm;
        private JLabel dkp;
        private JTable eventTable;
        private UnmodifiableTableModel mtm;
        private WOWCharacter c;

        private JPanel northPanel;

        private CharacterPanel(WOWCharacter c) {
            this.c = c;
            preSet();
            if (c != null) {
                reloadContent();
            }
        }

        private void setCharacter(WOWCharacter c) {
            this.c = c;
        }

        private void preSet() {
            northPanel = new JPanel();
            this.setLayout(new BorderLayout());
            northPanel.setLayout(new GridLayout(4, 2));

            // Go back button and empty label
            JButton b = new JButton("<= Back to Table");
            b.addActionListener(new MyButtonListener());
            northPanel.add(b);
            JLabel l = new JLabel(" ");
            northPanel.add(l);

            // Character name
            l = new JLabel("Name: ", SwingConstants.RIGHT);
            northPanel.add(l);
            name = new JLabel();
            northPanel.add(name);

            // Character Realm
            l = new JLabel("Realm: ", SwingConstants.RIGHT);
            northPanel.add(l);
            realm = new JLabel();
            northPanel.add(realm);

            // Character total DKP
            l = new JLabel("Total DKP: ", SwingConstants.RIGHT);
            northPanel.add(l);
            dkp = new JLabel();
            northPanel.add(dkp);

            // DKPEvent Table
            String[][] d = {};
            String[] cn = { "Score", "Type", "Difficulty", "Boss", "Item",
                    "Description" };
            mtm = new UnmodifiableTableModel(d, cn);
            eventTable = new JTable(mtm);

            // format table
            TableColumnModel cm = eventTable.getColumnModel();
            cm.getColumn(0).setPreferredWidth(100);
            cm.getColumn(1).setPreferredWidth(300);
            cm.getColumn(2).setPreferredWidth(100);
            cm.getColumn(3).setPreferredWidth(200);
            cm.getColumn(4).setPreferredWidth(500);
            cm.getColumn(5).setPreferredWidth(500);

            // Add DKPEvent table to panel
            this.add(northPanel, BorderLayout.NORTH);
            this.add(new JScrollPane(eventTable), BorderLayout.CENTER);
        }

        private void reloadContent() {
            if (c == null) {
                return;
            }
            name.setText(c.getName());
            realm.setText(c.getRealm());
            dkp.setText(String.valueOf(c.getTotalDKP()));
            while (mtm.getRowCount() > 0) {
                mtm.removeRow(0);
            }
            Iterator<DKPEvent> it = c.getEventList().iterator();
            while (it.hasNext()) {
                DKPEvent e = it.next();
                String[] rowData = {
                        String.valueOf(e.getScore()),
                        e.getType().name(),
                        e.getDifficulty() == null ? "-" : e.getDifficulty()
                                .name(),
                        e.getBoss() == null ? "-" : e.getBoss().getName(),
                        e.getItem() == null ? "-" : e.getItem().getName(),
                        e.getDescription() };
                mtm.addRow(rowData);
            }
        }
    }
}
