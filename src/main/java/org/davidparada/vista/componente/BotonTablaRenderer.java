package org.davidparada.vista.componente;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class BotonTablaRenderer extends JButton implements TableCellRenderer {

    public BotonTablaRenderer() {
        setOpaque(true);
        // Puedes darle un estilo similar a tus otros botones
        setBackground(new Color(0, 120, 115));
        setForeground(Color.WHITE);
        setFont(new Font("Arial", Font.BOLD, 12));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());
        return this;
    }
}
