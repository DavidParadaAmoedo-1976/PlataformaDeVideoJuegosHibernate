package org.davidparada.modelo.mapper;

import org.davidparada.modelo.entidad.BibliotecaEntidad;
import org.davidparada.modelo.formulario.BibliotecaForm;

import java.time.Instant;

public class BibliotecaFormularioAEntidadMapper {

    public static final double HORAS_DE_JUEGO_POR_DEFECTO = 0.0;

    private BibliotecaFormularioAEntidadMapper() {
    }

    public static BibliotecaEntidad crearBibliotecaEntidad(Long idBiblioteca, BibliotecaForm formulario) {

        return new BibliotecaEntidad(
                idBiblioteca,
                formulario.getIdUsuario(),
                formulario.getIdJuego(),
                Instant.now(),              // FechaAdquisicion automática
                HORAS_DE_JUEGO_POR_DEFECTO, // HorasDeJuego inicial
                null,                       // "ultimaFechaDeJuego" -> aún no ha jugado
                false                       // No instalado por defecto
        );
    }

    public static BibliotecaEntidad crearBibliotecaEntidad(BibliotecaForm formulario) {
        return crearBibliotecaEntidad(null, formulario);
    }

    public static BibliotecaEntidad actualizarBibliotecaEntidad(Long idBibliteca, BibliotecaForm formulario) {
        return new BibliotecaEntidad(
                idBibliteca,
                formulario.getIdUsuario(),
                formulario.getIdJuego(),
                formulario.getFechaAdquisicion(),
                formulario.getHorasDeJuego(),
                formulario.getUltimaFechaDeJuego(),
                formulario.isEstadoInstalacion()
        );
    }
}
