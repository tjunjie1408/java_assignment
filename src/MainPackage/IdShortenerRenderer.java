package MainPackage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
public class IdShortenerRenderer extends DefaultTableCellRenderer {

    private static final int PREFIX_LEN = 8;

    @Override
    public Component getTableCellRendererComponent(JTable table,
                                                   Object value,
                                                   boolean isSelected,
                                                   boolean hasFocus,
                                                   int row,
                                                   int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String fullId = value == null ? "" : value.toString();
        String text;
        if (fullId.length() > PREFIX_LEN) {
            text = fullId.substring(0, PREFIX_LEN) + "...";
        } else {
            text = fullId;
        }
        setText(text);
        setToolTipText(fullId);
        return this;
    }
}