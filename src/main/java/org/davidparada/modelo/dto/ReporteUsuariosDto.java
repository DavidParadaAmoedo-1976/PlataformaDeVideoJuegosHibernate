package org.davidparada.modelo.dto;

import java.time.Instant;

public class ReporteUsuariosDto {

    private final Instant fechaInicio;
    private final Instant fechaFin;
    private final Integer nuevosUsuarios;
    private final Integer usuariosActivos;

    public ReporteUsuariosDto(Instant fechaInicio,
                              Instant fechaFin,
                              Integer nuevosUsuarios,
                              Integer usuariosActivos) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.nuevosUsuarios = nuevosUsuarios;
        this.usuariosActivos = usuariosActivos;
    }

    public Instant getFechaInicio() {
        return fechaInicio;
    }

    public Instant getFechaFin() {
        return fechaFin;
    }

    public Integer getNuevosUsuarios() {
        return nuevosUsuarios;
    }

    public Integer getUsuariosActivos() {
        return usuariosActivos;
    }

}
