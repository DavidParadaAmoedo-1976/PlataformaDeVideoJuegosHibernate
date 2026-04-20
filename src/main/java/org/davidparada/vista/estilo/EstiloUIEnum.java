package org.davidparada.vista.estilo;

import java.awt.*;

public enum EstiloUIEnum {

    TITULO(new Font("Arial", Font.BOLD | Font.ITALIC, 30)),
    SUBTITULO(new Font("Arial", Font.BOLD, 25)),
    TEXTO_FORM(new Font("Arial", Font.PLAIN, 18)),
    BOTON(new Font("Arial", Font.BOLD, 16));

    private final Font font;

    EstiloUIEnum(Font font) {
        this.font = font;
    }

    public Font font() {
        return font;
    }
}
