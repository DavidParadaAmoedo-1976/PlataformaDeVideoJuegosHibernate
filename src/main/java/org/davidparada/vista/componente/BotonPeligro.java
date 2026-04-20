package org.davidparada.vista.componente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class BotonPeligro extends BotonBase {

    public BotonPeligro(String texto, int ancho, int alto, Consumer<ActionEvent> accion) {
        super(texto, ancho, alto, accion);

        setBackground(new Color(200, 50, 50));
        setForeground(Color.WHITE);
    }
}
