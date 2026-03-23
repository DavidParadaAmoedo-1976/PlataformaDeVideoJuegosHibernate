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

    public static void actualizar(JuegoEntidad juego, JuegoForm formulario) {
        juego.setTitulo(formulario.getTitulo());
        juego.setDescripcion(formulario.getDescripcion());
        juego.setDesarrollador(formulario.getDesarrollador());
        juego.setFechaLanzamiento(formulario.getFechaLanzamiento());
        juego.setPrecioBase(formulario.getPrecioBase());
        juego.setDescuento(formulario.getDescuento());
        juego.setCategoria(formulario.getCategoria());
        juego.setClasificacionPorEdad(formulario.getClasificacionPorEdad());
        juego.setIdiomas(formulario.getIdiomas());
        juego.setEstado(formulario.getEstado());
    }
}

