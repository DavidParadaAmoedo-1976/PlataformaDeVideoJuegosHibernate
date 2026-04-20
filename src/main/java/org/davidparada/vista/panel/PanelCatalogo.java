package org.davidparada.vista.panel;

import org.davidparada.controlador.interfaceControlador.IJuegoControlador;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.JuegoDto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCatalogo extends JPanel {

    private IJuegoControlador juegoControlador;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PanelCatalogo(IJuegoControlador juegoControlador) throws ValidationException {
        this.juegoControlador = juegoControlador;

        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(
                new String[]{"Título", "Precio", "Estado"}, 0);

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarDatos();
    }

    private void cargarDatos() throws ValidationException {
        List<JuegoDto> juegos =
                juegoControlador.consultarCatalogo(null);

        for (JuegoDto j : juegos) {
            modelo.addRow(new Object[]{
                    j.titulo(),
                    j.precioBase(),
                    j.estado()
            });
        }
    }
}
