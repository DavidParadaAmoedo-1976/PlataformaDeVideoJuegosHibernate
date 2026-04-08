package org.davidparada.modelo.dto;

import java.util.List;
import java.util.Optional;

public record EstadisticasBibliotecaDto(Integer totalDeJuegos,
                                        Double horasTotales,
                                        List<JuegoDto> juegosInstalados,
                                        Optional<JuegoDto> juegoMasJugado,
                                        Double valorTotal,
                                        List<JuegoDto> juegosNuncaJugados) {
}

