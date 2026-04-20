package org.davidparada.vista.componente;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public abstract class BotonBase extends JButton {

    public BotonBase(String texto, int ancho, int alto, Consumer<ActionEvent> accion) {
        super(texto);

        setPreferredSize(new Dimension(ancho, alto));
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (accion != null) {
            addActionListener(accion::accept);
        }
    }
}
