package org.davidparada.modelo.enums;

public enum OrdenarJuegosBibliotecaEnum {

    ALFABETICO("Alfabético"),
    TIEMPO_DE_JUEGO("Tiempo de Juego"),
    ULTIMA_SESION("Última sesión"),
    FECHA_DE_ADQUISICION("Fecha de Adquisición"),
    ;

    private final String descripcion;

    OrdenarJuegosBibliotecaEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String descripcion() {
        return descripcion;
    }
}
