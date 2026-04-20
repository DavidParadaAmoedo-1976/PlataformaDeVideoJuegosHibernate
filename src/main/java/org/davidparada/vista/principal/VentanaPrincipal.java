package org.davidparada.vista.principal;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private CardLayout layout;
    private JPanel contenedor;

    public VentanaPrincipal() {

        setTitle("Steam - Proyecto");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        layout = new CardLayout();
        contenedor = new JPanel(layout);

        add(contenedor);
    }

    public void agregarPanel(String nombre, JPanel panel) {
        contenedor.add(panel, nombre);
    }

    public void mostrar(String nombre) {
        layout.show(contenedor, nombre);
    }
}
