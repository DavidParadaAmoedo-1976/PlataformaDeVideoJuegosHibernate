package org.davidparada.modelo.mapper;

import org.davidparada.modelo.dto.JuegoDto;
import org.davidparada.modelo.entidad.JuegoEntidad;

import java.util.ArrayList;
import java.util.List;

public class JuegoEntidadADtoMapper {

    private JuegoEntidadADtoMapper() {
    }

    public static JuegoDto juegoEntidadADto(JuegoEntidad juego) {

        if (juego == null) {
            return null;
        }

        List<String> idiomas = new ArrayList<>(juego.getIdiomas());
        return new JuegoDto(
                juego.getIdJuego(),
                juego.getTitulo(),
                juego.getDescripcion(),
                juego.getDesarrollador(),
                juego.getFechaLanzamiento(),
                juego.getPrecioBase(),
                juego.getDescuento(),
                juego.getCategoria(),
                juego.getClasificacionPorEdad(),
                idiomas,
                juego.getEstado()
        );
    }
}

