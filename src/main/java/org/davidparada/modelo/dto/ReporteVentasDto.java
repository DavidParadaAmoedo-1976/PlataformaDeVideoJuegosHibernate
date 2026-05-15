package org.davidparada.modelo.dto;

import java.time.Instant;

public class ReporteVentasDto {

    private final Instant fechaInicio;
    private final Instant fechaFin;
    private final Integer totalVentas;
    private final Double ingresosTotales;

    public ReporteVentasDto(Instant fechaInicio,
                            Instant fechaFin,
                            Integer totalVentas,
                            Double ingresosTotales) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.totalVentas = totalVentas;
        this.ingresosTotales = ingresosTotales;
    }

    public Instant getFechaInicio() {
        return fechaInicio;
    }

    public Instant getFechaFin() {
        return fechaFin;
    }

    public Integer getTotalVentas() {
        return totalVentas;
    }

    public Double getIngresosTotales() {
        return ingresosTotales;
    }

}
