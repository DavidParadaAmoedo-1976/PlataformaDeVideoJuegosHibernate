package org.davidparada.vista.componente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class BotonPrincipal extends BotonBase {

    public BotonPrincipal(String texto, int ancho, int alto, Consumer<ActionEvent> accion) {
        super(texto, ancho, alto, accion);

        setBackground(new Color(0, 120, 115));
        setForeground(Color.WHITE);
    }
}

