package org.davidparada.modelo.dto;

import org.davidparada.modelo.enums.MetodoPagoEnum;

import java.time.Instant;

public record FacturaDto(String numeroFactura,
                         Long idCompra,
                         String tituloJuego,
                         String nombreReal,
                         String email,
                         Instant fechaEmision,
                         Double importe,
                         Integer descuento,
                         MetodoPagoEnum metodoPago) {
}
