package org.davidparada.modelo.mapper;

import org.davidparada.modelo.entidad.JuegoEntidad;
import org.davidparada.modelo.enums.EstadoJuegoEnum;
import org.davidparada.modelo.formulario.JuegoForm;

public class JuegoFormularioAEntidadMapper {

    public static final int DESCUENTO_POR_DEFECTO = 0;

    private JuegoFormularioAEntidadMapper() {
    }

    public static JuegoEntidad crearJuegoEntidad(Long id, JuegoForm formulario) {
        return new JuegoEntidad(
                id,
                formulario.getTitulo(),
                formulario.getDescripcion(),
                formulario.getDesarrollador(),
                formulario.getFechaLanzamiento(),
                formulario.getPrecioBase(),
                DESCUENTO_POR_DEFECTO,
                formulario.getCategoria(),
                formulario.getClasificacionPorEdad(),
                formulario.getIdiomas(),
                EstadoJuegoEnum.DISPONIBLE
        );
    }

    public static JuegoEntidad crearJuegoEntidad(JuegoForm formulario) {
        return crearJuegoEntidad(null, formulario);
    }

    public static JuegoEntidad actualizarJuegoEntidad(Long id, JuegoForm formulario) {

        return new JuegoEntidad(
                id,
                formulario.getTitulo(),
                formulario.getDescripcion(),
                formulario.getDesarrollador(),
                formulario.getFechaLanzamiento(),
                formulario.getPrecioBase(),
                formulario.getDescuento(),
                formulario.getCategoria(),
                formulario.getClasificacionPorEdad(),
                formulario.getIdiomas(),
                formulario.getEstado()
        );
    }
}

