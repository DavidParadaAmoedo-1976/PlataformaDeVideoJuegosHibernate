package org.davidparada.util;

import org.davidparada.modelo.dto.ArchivoInfoDto;
import org.davidparada.modelo.enums.TipoArchivoEnum;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

public class ArchivoUtils {

    public static ArchivoInfoDto crearArchivoInfo(String idArchivo, TipoArchivoEnum tipo, String rutaArchivo) {
        Path path = Path.of(rutaArchivo);
        String nombreArchivo = path.getFileName().toString();

        long tamanoBytes = 0;
        try {
            tamanoBytes = Files.size(path);
        } catch (IOException e) {
            System.err.println("No se pudo calcular el tamaño del archivo: " + e.getMessage());
        }

        return new ArchivoInfoDto(
                idArchivo,
                tipo.descripcion(),
                nombreArchivo,
                rutaArchivo,
                tamanoBytes,
                Instant.now()
        );
    }
}