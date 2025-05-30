package CarPackage;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.net.URL;
public class CarTableCellRenderer extends JLabel implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText("");
        setIcon(null);
        if (column == 5) {
            String photoPath = value.toString();
            URL imgUrl = getClass().getClassLoader().getResource(photoPath);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image scaled = icon.getImage()
                        .getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                setIcon(new ImageIcon(scaled));
            } else {
                ImageIcon icon = new ImageIcon(photoPath);
                if (icon.getIconWidth() == -1) {
                    System.out.println("Image not found: " + photoPath);
                    setText("Image not found");
                } else {
                    Image scaled = icon.getImage()
                            .getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    setIcon(new ImageIcon(scaled));
                }
            }
        } else {setText(value.toString());}
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());}
        setOpaque(true);
        return this;
    }
}
