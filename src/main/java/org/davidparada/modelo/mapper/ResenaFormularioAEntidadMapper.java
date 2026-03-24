package org.davidparada.modelo.mapper;

import org.davidparada.modelo.entidad.ResenaEntidad;
import org.davidparada.modelo.enums.EstadoPublicacionEnum;
import org.davidparada.modelo.formulario.ResenaForm;

import java.time.Instant;

public class ResenaFormularioAEntidadMapper {

    public ResenaFormularioAEntidadMapper() {
    }

    public static ResenaEntidad crearReseniaEntidad(Long idResenia, ResenaForm formulario) {

        return new ResenaEntidad(
                idResenia,
                formulario.getIdUsuario(),
                formulario.getIdJuego(),
                formulario.isRecomendado(),
                formulario.getTextoResena(),
                formulario.getCantidadHorasJugadas(),
                Instant.now(),
                Instant.now(),
                EstadoPublicacionEnum.PUBLICADA
        );
    }

    public static ResenaEntidad crearReseniaEntidad(ResenaForm formulario) {
        return crearReseniaEntidad(null, formulario);
    }

    public static ResenaEntidad actualizarReseniaEntidad(Long idResenia, ResenaForm form) {

        return new ResenaEntidad(
                idResenia,
                form.getIdUsuario(),
                form.getIdJuego(),
                form.isRecomendado(),
                form.getTextoResena(),
                form.getCantidadHorasJugadas(),
                form.getFechaPublicacion(),
                Instant.now(),
                form.getEstadoPublicacion()
        );
    }
}
