package org.davidparada.vista.componente.uiState;

import java.util.function.Consumer;

public class TarjetaPanelUIState {

    // =========================
    // Datos principales
    // =========================
    private Long id;
    private String titulo;
    private String descripcion;
    private String estado;
    private String lenguaje;
    private Double precio;
    private Integer descuento; // porcentaje
    private String rutaImagen;

    // =========================
    // Textos de botones
    // =========================
    private String textoBotonPrincipal;
    private String textoBotonSecundario;
    private String textoBotonPeligro;

    // =========================
    // Acciones
    // =========================
    private Consumer<Long> accionPrincipal;
    private Consumer<Long> accionSecundario;
    private Consumer<Long> accionPeligro;

    // =========================
    // Constructor completo
    // =========================
    public TarjetaPanelUIState(
            Long id,
            String titulo,
            String descripcion,
            String estado,
            String lenguaje,
            Double precio,
            Integer descuento,
            String rutaImagen,
            String textoBotonPrincipal,
            String textoBotonSecundario,
            String textoBotonPeligro,
            Consumer<Long> accionPrincipal,
            Consumer<Long> accionSecundario,
            Consumer<Long> accionPeligro) {

        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.lenguaje = lenguaje;
        this.precio = precio;
        this.descuento = descuento;
        this.rutaImagen = rutaImagen;

        this.textoBotonPrincipal = textoBotonPrincipal;
        this.textoBotonSecundario = textoBotonSecundario;
        this.textoBotonPeligro = textoBotonPeligro;

        this.accionPrincipal = accionPrincipal;
        this.accionSecundario = accionSecundario;
        this.accionPeligro = accionPeligro;
    }

    // =========================
    // Getters
    // =========================

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public String getLenguaje() {
        return lenguaje;
    }

    public Double getPrecio() {
        return precio;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public String getTextoBotonPrincipal() {
        return textoBotonPrincipal;
    }

    public String getTextoBotonSecundario() {
        return textoBotonSecundario;
    }

    public String getTextoBotonPeligro() {
        return textoBotonPeligro;
    }

    public Consumer<Long> getAccionPrincipal() {
        return accionPrincipal;
    }

    public Consumer<Long> getAccionSecundario() {
        return accionSecundario;
    }

    public Consumer<Long> getAccionPeligro() {
        return accionPeligro;
    }

    // =========================
    // Métodos útiles para UI
    // =========================

    public boolean tieneDescuento() {
        return descuento != null && descuento > 0;
    }

    public double getPrecioConDescuento() {
        if (!tieneDescuento()) return precio;
        return precio - (precio * descuento / 100.0);
    }

    public String getDescuentoFormateado() {
        if (!tieneDescuento()) return "";
        return "-" + descuento + "%";
    }

    public String getDescuentoTarjeta() {
        return "0";
    }
}
