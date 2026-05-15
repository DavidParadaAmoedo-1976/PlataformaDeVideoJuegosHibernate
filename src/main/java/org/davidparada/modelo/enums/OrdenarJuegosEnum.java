package org.davidparada.modelo.enums;

public enum OrdenarJuegosEnum {

    ALFABETICO("Alfabético"),
    PRECIO("Precio"),
    FECHA("Fecha"),
    ;

    private final String descripcion;

    OrdenarJuegosEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String descripcion() {
        return descripcion;
    }
}

