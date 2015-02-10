package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import model.QueryHandler;
import model.WOWCharacter;

@SuppressWarnings("serial")
public class MyClassColorRenderer extends DefaultTableCellRenderer {

    private List<WOWCharacter> characters_;

    private static MyClassColorRenderer instance_ = null;

    private MyClassColorRenderer(List<WOWCharacter> characters) {
        super();
        characters_ = characters;
    }

    public static MyClassColorRenderer getInstance() {
        if (instance_ == null) {
            instance_ = new MyClassColorRenderer(
                    QueryHandler.getAllCharacters());
        }
        return instance_;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int col) {
        Component c = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, col);
        Color color = Color.decode(characters_.get(row).getWOWCharacterClass().getColor());
        c.setBackground(color);
        c.setForeground(getContrastColor(color));
        if (isSelected) {
            c.setFont(new Font("Arial Black", Font.BOLD, 15));
        }
        return c;
    }

    public static void setCharacters(List<WOWCharacter> cList) {
        MyClassColorRenderer.getInstance().characters_ = cList;
    }

    // I took the following code from
    // http://stackoverflow.com/questions/4672271/reverse-opposing-colors
    // This function uses YIQ color system instead of RBG which is more efficient
    // on computing contrast coler.
    private static Color getContrastColor(Color color) {
        double y = (299 * color.getRed() + 587 * color.getGreen() + 114 * color
                .getBlue()) / 1000;
        return y >= 128 ? Color.black : Color.white;
    }

}
