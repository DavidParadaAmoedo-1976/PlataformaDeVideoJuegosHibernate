package org.davidparada.modelo.dto;

import org.davidparada.modelo.enums.ClasificacionJuegoEnum;
import org.davidparada.modelo.enums.EstadoJuegoEnum;

import java.time.LocalDate;
import java.util.List;

public record JuegoDto(Long idJuego,
                       String titulo,
                       String descripcion,
                       String desarrollador,
                       LocalDate fechaLanzamiento,
                       Double precioBase,
                       Integer descuento,
                       String categoria,
                       ClasificacionJuegoEnum clasificacionPorEdad,
                       List<String> idiomas,
                       EstadoJuegoEnum estado) {
}