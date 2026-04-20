package org.davidparada.vista.componente;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

public class BotonSecundario extends BotonBase {

    public BotonSecundario(String texto, int ancho, int alto, Consumer<ActionEvent> accion) {
        super(texto, ancho, alto, accion);

        setBackground(new Color(70, 70, 70));
        setForeground(Color.WHITE);
    }
}