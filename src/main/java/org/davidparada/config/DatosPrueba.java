package org.davidparada.config;

import org.davidparada.controlador.*;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.enums.*;
import org.davidparada.modelo.formulario.JuegoForm;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.modelo.formulario.validacion.ErrorModel;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class DatosPrueba {

    public static void cargarDatos(
            UsuarioControlador usuarioCtrl,
            JuegoControlador juegoCtrl,
            BibliotecaControlador bibliotecaCtrl,
            CompraControlador compraCtrl,
            ResenaControlador resenaCtrl
    ) {

        Random random = new Random();

        try {

            // ==========================
            // CREAR 30 JUEGOS
            // ==========================

            String[] idiomas = new String[]{"Español, Inglés"};

            for (int i = 1; i <= 30; i++) {

                juegoCtrl.crearJuego(new JuegoForm(
                        "Juego " + i,
                        "Descripción del juego " + i,
                        "DevStudio " + (i % 5),
                        LocalDate.now().minusDays(i),
                        10.0 + random.nextInt(50),
                        0,
                        "Acción",
                        ClasificacionJuegoEnum.PEGI_18,
                        List.of(idiomas),
                        EstadoJuegoEnum.DISPONIBLE
                ));
            }

            // ==========================
            // CREAR 15 USUARIOS
            // ==========================

            for (int i = 1; i <= 15; i++) {
                try {

                    usuarioCtrl.registrarUsuario(new UsuarioForm(
                            "usuario" + i,
                            "usuario" + i + "@mail.com",
                            "Pasword" + i,
                            "Nombre Real " + i,
                            PaisEnum.ESPANA,
                            LocalDate.of(random.nextInt(1990, 2000), 5, 10),
                            Instant.now(),
                            null,
                            200.0,
                            EstadoCuentaEnum.ACTIVA
                    ));
                } catch (ValidationException e) {

                    System.out.println("❌ Error en usuario " + i);

                    for (ErrorModel error : e.getErrores()) {
                        System.out.println(
                                "Campo: " + error.campo() +
                                        " | Tipo: " + error.error()
                        );
                    }

                    System.out.println("-----------------------------");
                }
            }

            // ==========================
            // 3️⃣ CREAR 10 COMPRAS
            // ==========================

            for (int i = 1; i <= 10; i++) {
                try {
                    compraCtrl.realizarCompra(
                            (long) i,
                            (long) i,
                            MetodoPagoEnum.TARJETA
                    );
                } catch (ValidationException e) {

                    System.out.println("❌ Error en usuario " + i);

                    for (ErrorModel error : e.getErrores()) {
                        System.out.println(
                                "Campo: " + error.campo() +
                                        " | Tipo: " + error.error()
                        );
                    }

                    System.out.println("-----------------------------");
                }
            }

            // ==========================
            // CREAR 10 EN BIBLIOTECA
            // ==========================

            for (int i = 1; i <= 10; i++) {

                bibliotecaCtrl.anadirJuego(
                        (long) (i),
                        (long) (i)
                );
            }

            // ==========================
            // 5️⃣ CREAR 10 RESEÑAS
            // ==========================

            for (int i = 1; i <= 10; i++) {

                resenaCtrl.escribirResena(
                        (long) (i),
                        (long) (i),
                        random.nextBoolean(),
                        "Reseña de prueba ".repeat(5)
                );
            }

            System.out.println("✅ Datos de prueba cargados correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

