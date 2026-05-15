package org.davidparada.modelo.enums;

public enum TipoErrorEnum {

    OBLIGATORIO("Campo obligatorio"),
    FORMATO_INVALIDO("Formato inválido"),
    VALOR_NEGATIVO("Valor negativo"),
    LONGITUD_EXCEDIDA("Longitud excedida"),
    VALOR_EXCEDIDO("Valor excedido"),
    RANGO_INVALIDO("Fuera de rango"),
    DUPLICADO("Duplicado"),
    NO_ENCONTRADO("No encontrado"),
    ESTADO_INCORRECTO("Estado incorrecto"),
    NO_PERMITIDO("No permitido"),
    SALDO_INSUFICIENTE("Saldo insuficiente"),
    NO_COINCIDE("No coincide"),
    NO_DISPONIBLE("No disponible"),
    LISTA_VACIA("Lista vacía"),
    ARCHIVO_NO_ENCONTRADO("Archivo no encontrado"),
    ARCHIVO_NO_CREADO("Archivo no creado"),
    NO_SE_PUEDE_LEER_ARCHIVO("No se puede leer archivo"),
    NO_SE_PUEDE_CREAR_PDF("No se puede crear PDF"),
    NO_SE_PUEDE_MOVER_ARCHIVO("No se puede mover archivo"),
    OTRO("Otro");

    private final String descripcion;

    TipoErrorEnum(String descripcion) {
        this.descripcion = descripcion;
    }

    public String descripcion() {
        return descripcion;
    }
}

