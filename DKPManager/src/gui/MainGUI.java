package gui;

import gui.GUIConstants.MenuItemType;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import model.Constants;
import model.QueryHandler;

public class MainGUI implements ActionListener, PropertyChangeListener {

    private JPanel mainPanel_;
    private JFrame motherFrame_;
    private JTabbedPane tabbedPane_;
    private JProgressBar progressBar_;
    private JDialog progressDialog_;
    private JFileChooser fileChooser_;

    private MainGUI(JFrame frame) {
        mainPanel_ = new JPanel();
        motherFrame_ = frame;
        mainPanel_.setLayout(new BorderLayout());
        mainPanel_.setBounds(0, 0, 500, 500);
        setMenuBar();
        setTabbedPane();
        setProgressDialog();
        setFileChooser();
    }

    private void setFileChooser() {
        fileChooser_ = new JFileChooser(new File("."));
    }

    private void setProgressDialog() {
        progressBar_ = new JProgressBar(0, 100);
        progressBar_.setStringPainted(true);
        progressDialog_ = new JDialog(motherFrame_, "Progress...", true);
        progressDialog_.add(progressBar_, BorderLayout.CENTER);
        progressDialog_.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        progressDialog_.setSize(300, 75);
        progressDialog_.setLocationRelativeTo(motherFrame_);
    }

    private void setTabbedPane() {
        tabbedPane_ = new JTabbedPane();
        tabbedPane_.add("DKP Table", new TableDisplay());
        tabbedPane_.add("Add DKP event", new ManipulationPanel());
        tabbedPane_.add("DKP Rules", new DKPRulesPanel());

        // Add tabbedPane to main panel
        tabbedPane_.setPreferredSize(new Dimension(1000, 500));
        mainPanel_.add(tabbedPane_, BorderLayout.CENTER);
    }

    private void setMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenuItem mi;

        // File
        JMenu menu = new JMenu("File");

        // File - Load
        mi = new JMenuItem("Load");
        mi.addActionListener(this);
        menu.add(mi);

        // File - Load from File
        mi = new JMenuItem("Load from File");
        mi.addActionListener(this);
        menu.add(mi);

        // File - Save
        mi = new JMenuItem("Save");
        mi.addActionListener(this);
        menu.add(mi);

        // File - Save to File
        mi = new JMenuItem("Save to File");
        mi.addActionListener(this);
        menu.add(mi);

        // File - Clear
        mi = new JMenuItem("Clear");
        mi.addActionListener(this);
        menu.add(mi);

        // File - Exit
        mi = new JMenuItem("Exit");
        mi.addActionListener(this);
        menu.add(mi);
        menuBar.add(menu);
        motherFrame_.setJMenuBar(menuBar);
    }

    private JComponent getMainComponent() {
        return mainPanel_;
    }

    private static void createAndShowGui() {
        JFrame mainDKPFrame = new JFrame("DKP Manager (" + Constants.GUILD_NAME
                + ") " + "Version: " + Constants.VERSION_STRING);
        MainGUI mainGUI = new MainGUI(mainDKPFrame);
        mainDKPFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainDKPFrame.getContentPane().add(mainGUI.getMainComponent());
        mainDKPFrame.pack();
        mainDKPFrame.setLocationByPlatform(true);
        mainDKPFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() instanceof JMenuItem) {
            JMenuItem mi = (JMenuItem) arg0.getSource();
            if ("Exit".equals(mi.getText())) {
                motherFrame_.dispatchEvent(new WindowEvent(motherFrame_,
                        WindowEvent.WINDOW_CLOSING));
            } else if ("Load".equals(mi.getText())) {
                int r = JOptionPane
                        .showConfirmDialog(null,
                                "Are you sure to discard all possible changes and reload data from database?\n");
                if (r == JOptionPane.OK_OPTION) {
                    ButtonTask bt = new ButtonTask(MenuItemType.LOAD);
                    bt.addPropertyChangeListener(this);
                    bt.execute();
                    progressDialog_.setVisible(true);
                    JOptionPane.showMessageDialog(null, "Data loaded.");
                }
            } else if ("Load from File".equals(mi.getText())) {
                int r = JOptionPane
                        .showConfirmDialog(null,
                                "Are you sure to discard all possible changes and reload data from file?\n");
                if (r == JOptionPane.OK_OPTION) {
                    r = fileChooser_.showOpenDialog(mainPanel_);
                    if (r == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser_.getSelectedFile();
                        ButtonTask bt = new ButtonTask(
                                MenuItemType.LOAD_FROM_FILE, selectedFile);
                        bt.addPropertyChangeListener(this);
                        bt.execute();
                        progressDialog_.setVisible(true);
                        JOptionPane.showMessageDialog(null, "Data loaded.");
                    } else {
                        return;
                    }
                }
            } else if ("Save to File".equals(mi.getText())) {
                DateFormat df = new SimpleDateFormat("yy-MM-dd-HH-mm", Locale.US);
                File tmpFile = new File("./DKP-" + (df.format(new Date()) + ".xml"));
                ButtonTask bt = new ButtonTask(MenuItemType.SAVE_TO_FILE,
                        tmpFile);
                bt.addPropertyChangeListener(this);
                bt.execute();
                progressDialog_.setVisible(true);
                JOptionPane.showMessageDialog(null, "Data saved to " + tmpFile.getAbsolutePath());
            } else if ("Save".equals(mi.getText())) {
                ButtonTask bt = new ButtonTask(MenuItemType.SAVE);
                bt.addPropertyChangeListener(this);
                bt.execute();
                progressDialog_.setVisible(true);
                JOptionPane.showMessageDialog(null, "Data saved.");
            } else if ("Clear".equals(mi.getText())) {
                int r = JOptionPane
                        .showConfirmDialog(null,
                                "Are you sure to delete all data from database and this application?");
                if (r == JOptionPane.OK_OPTION) {
                    ButtonTask bt = new ButtonTask(MenuItemType.CLEAR);
                    bt.addPropertyChangeListener(this);
                    bt.execute();
                    progressDialog_.setVisible(true);
                    JOptionPane.showMessageDialog(null, "Database cleared.");
                }
            }
        }
    }

    private class ButtonTask extends SwingWorker<Void, Void> {

        MenuItemType type;
        File file_;

        private ButtonTask(MenuItemType t) {
            type = t;
        }

        private ButtonTask(MenuItemType t, File f) {
            type = t;
            file_ = f;
        }

        @Override
        protected Void doInBackground() throws Exception {
            if (type == MenuItemType.LOAD) {
                setProgress(0);
                QueryHandler.loadFromDatabase();
                setProgress(50);
                MyTableModel.refreshTableData();
                setProgress(100);
            } else if (type == MenuItemType.SAVE) {
                setProgress(0);
                QueryHandler.saveToDatabase();
                setProgress(100);
            } else if (type == MenuItemType.CLEAR) {
                setProgress(0);
                QueryHandler.clearDatabase();
                setProgress(50);
                QueryHandler.loadFromDatabase();
                setProgress(90);
                MyTableModel.refreshTableData();
                setProgress(100);
            } else if (type == MenuItemType.SAVE_TO_FILE) {
                setProgress(0);
                QueryHandler.saveToFile(file_);
                setProgress(100);
            } else if (type == MenuItemType.LOAD_FROM_FILE) {
                setProgress(0);
                QueryHandler.loadFromFile(file_);
                setProgress(50);
                MyTableModel.refreshTableData();
                setProgress(100);
            }
            return null;
        }

        @Override
        protected void done() {
            super.done();
            progressDialog_.setVisible(false);
            progressBar_.setValue(0);
            progressBar_.setString(0 + "%");
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent arg0) {
        if ("progress".equals(arg0.getPropertyName())) {
            int progress = (int) arg0.getNewValue();
            progressBar_.setValue(progress);
            progressBar_.setString(progress + "%");
        }
    }

    public static void main(String[] args) {
        // Set look and feel
        try {
            for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                System.out.println(laf.getClassName());
                if (laf.getClassName().toLowerCase().contains("nimbus")) {
                    UIManager.setLookAndFeel(laf.getClassName());
                }
            }
        } catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGui();
            }
        });
    }
}
