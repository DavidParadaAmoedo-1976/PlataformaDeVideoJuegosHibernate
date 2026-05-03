package org.davidparada.vista.componente;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class BotonTablaEditor extends DefaultCellEditor {

    protected JButton button;
    private String label;
    private int row;
    private final Consumer<Integer> accion;

    public BotonTablaEditor(JCheckBox checkBox, Consumer<Integer> accion) {
        super(checkBox);
        this.accion = accion;
        button = new JButton();
        button.setOpaque(true);

        // Estilo igual al renderer para que no "parpadee" al clicar
        button.setBackground(new Color(0, 120, 115));
        button.setForeground(Color.WHITE);

        button.addActionListener(e -> {
            fireEditingStopped(); // Detiene la edición para que la tabla no se quede "bloqueada"
            if (this.accion != null) {
                this.accion.accept(row);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.row = row;
        label = (value == null) ? "" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return label;
    }
}
