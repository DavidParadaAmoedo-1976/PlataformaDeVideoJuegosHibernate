//package org.davidparada.vista.panel;
//
//import org.davidparada.controlador.interfaceControlador.IJuegoControlador;
//import org.davidparada.excepcion.ValidationException;
//import org.davidparada.modelo.dto.JuegoDto;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableModel;
//import java.awt.*;
//import java.util.List;
//
//public class PanelCatalogo extends JPanel {
//
//    private IJuegoControlador juegoControlador;
//    private JTable tabla;
//    private DefaultTableModel modelo;
//    private List<JuegoDto> juegos;
//
//    public PanelCatalogo(IJuegoControlador juegoControlador) throws ValidationException {
//        this.juegoControlador = juegoControlador;
//
//        setLayout(new BorderLayout());
//
//        modelo = new DefaultTableModel(
//                new String[]{"Título", "Precio", "Estado",""}, 0);
//
//        tabla = new JTable(modelo);
//
//        tabla.getColumn("Acción").setCellRenderer(new ButtonRenderer());
//        tabla.getColumn("Acción").setCellEditor(new ButtonEditor(new JCheckBox()));
//
//        add(new JScrollPane(tabla), BorderLayout.CENTER);
//
//        cargarDatos();
//    }
//
//    private void cargarDatos() throws ValidationException {
//        juegos = juegoControlador.consultarCatalogo(null);
//
//        for (JuegoDto j : juegos) {
//            modelo.addRow(new Object[]{
//                    j.titulo(),
//                    j.precioBase(),
//                    j.estado(),
//                    "Ver"
//            });
//        }
//    }
//}

package org.davidparada.vista.panel;

import org.davidparada.controlador.interfaceControlador.IJuegoControlador;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.JuegoDto;
import org.davidparada.vista.componente.BotonTablaEditor;
import org.davidparada.vista.componente.BotonTablaRenderer;
import org.davidparada.vista.util.Navegador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelCatalogo extends JPanel {

    private final IJuegoControlador juegoControlador;
    private final DefaultTableModel modelo;
    private List<JuegoDto> juegos;

    public PanelCatalogo(IJuegoControlador juegoControlador, Navegador nav, PanelDetalleJuego panelDetalle) {
        this.juegoControlador = juegoControlador;
        setLayout(new BorderLayout());

        modelo = new DefaultTableModel(new String[]{"Título", "Precio", "Estado", "Acción"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return column == 3; }
        };

        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(35);

        tabla.getColumn("Acción").setCellRenderer(new BotonTablaRenderer());

        // --- LÓGICA DE NAVEGACIÓN ---
        tabla.getColumn("Acción").setCellEditor(new BotonTablaEditor(new JCheckBox(), (fila) -> {
            JuegoDto seleccionado = juegos.get(fila);

            // 1. Cargamos el juego en el panel de detalles
            panelDetalle.cargarJuego(seleccionado);

            // 2. Navegamos a esa pantalla
            nav.irA("detalleJuego");
        }));

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        try { cargarDatos(); } catch (ValidationException e) { e.printStackTrace(); }
    }

    private void cargarDatos() throws ValidationException {
        juegos = juegoControlador.consultarCatalogo(null);
        modelo.setRowCount(0);
        for (JuegoDto j : juegos) {
            modelo.addRow(new Object[]{j.titulo(), j.precioBase() + " €", j.estado(), "Ver Detalles"});
        }
    }
}
