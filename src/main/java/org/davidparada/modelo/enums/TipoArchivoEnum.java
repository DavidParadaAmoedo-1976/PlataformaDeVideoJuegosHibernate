package org.davidparada.modelo.enums;

public enum TipoArchivoEnum {
    FACTURA ("Factura."),
    FACTURA_REEMBOLSADA ("Factura reembolsada."),
    HISTORIAL_DE_COMPRA ("Historial de compra."),
    ESTADISTICAS_DE_RESENAS ("Estadisticas de reseñas."),
    REPORTE_DE_VENTAS ("Reporte de ventas."),
    REPORTE_DE_USUARIOS ("Reporte de usuarios."),
    JUEGOS_MAS_POPULARES ("Juegos más populares.");

    private final String descripcion;

    TipoArchivoEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String descripcion() {
        return descripcion;
    }
}
