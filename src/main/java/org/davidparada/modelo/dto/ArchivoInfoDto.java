package org.davidparada.modelo.dto;

import java.time.Instant;

public record ArchivoInfoDto(
        String idArchivo,
        String tipoArchivo,
        String nombreArchivo,
        String rutaArchivo,
        long tamañoBytes,
        Instant fechaCreacion) {

}
