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

import javax.swing.JComponent;
import javax.swing.JDialog;
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
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

import model.Constants;
import model.QueryHandler;

public class MainGUI implements ActionListener, PropertyChangeListener {

    private JPanel mainPanel_;
    private JFrame motherFrame_;
    private JTabbedPane tabbedPane_;
    private JProgressBar progressBar_;
    private JDialog progressDialog_;

    private MainGUI(JFrame frame) {
        mainPanel_ = new JPanel();
        motherFrame_ = frame;
        mainPanel_.setLayout(new BorderLayout());
        mainPanel_.setBounds(0, 0, 500, 500);
        setMenuBar();
        setTabbedPane();
        setProgressDialog();
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

        // File = Load
        mi = new JMenuItem("Load");
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
        JFrame mainDKPFrame = new JFrame("DKP Viewer (" + Constants.GUILD_NAME
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
                ButtonTask bt = new ButtonTask(MenuItemType.LOAD);
                bt.addPropertyChangeListener(this);
                bt.execute();
                progressDialog_.setVisible(true);
                JOptionPane.showMessageDialog(null, "Data loaded.");
            }
        }
    }

    private class ButtonTask extends SwingWorker<Void, Void> {

        MenuItemType type;

        private ButtonTask(MenuItemType t) {
            type = t;
        }

        @Override
        protected Void doInBackground() throws Exception {
            if (type == MenuItemType.LOAD) {
                setProgress(0);
                QueryHandler.loadFromDatabase();
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
