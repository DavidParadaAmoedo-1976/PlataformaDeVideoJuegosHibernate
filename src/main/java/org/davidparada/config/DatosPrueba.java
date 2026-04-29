package org.davidparada.config;

import org.davidparada.controlador.interfaceControlador.*;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.CompraDto;
import org.davidparada.modelo.dto.UsuarioDto;
import org.davidparada.modelo.entidad.CompraEntidad;
import org.davidparada.modelo.enums.*;
import org.davidparada.modelo.formulario.JuegoForm;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.modelo.formulario.validacion.ErrorModel;
import org.davidparada.repositorio.interfaceRepositorio.ICompraRepo;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DatosPrueba {

        private static ICompraRepo compraRepo;

        public static void cargarDatos (
                IUsuarioControlador usuarioCtrl,
                IJuegoControlador juegoCtrl,
                IBibliotecaControlador bibliotecaCtrl,
                ICompraControlador compraCtrl,
                IResenaControlador resenaCtrl
    ){
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

                        UsuarioDto usuario = usuarioCtrl.registrarUsuario(new UsuarioForm(
                                "usuario" + i,
                                "usuario" + i + "@mail.com",
                                "1234Password",
                                "Nombre Real " + i,
                                PaisEnum.ESPANA,
                                LocalDate.of(random.nextInt(1990, 2000), 5, 10),
                                Instant.now(),
                                null,
                                0.0,
                                EstadoCuentaEnum.ACTIVA
                        ));

                    usuarioCtrl.anadirSaldo(usuario.idUsuario(), random.nextDouble(50,200));
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
                // CREAR 10 COMPRAS
                // ==========================

                for (int i = 1; i <= 10; i++) {
                    try {
                        CompraDto compra = compraCtrl.realizarCompra(
                                (long) (random.nextInt(15) + 1),
                                (long) (random.nextInt(30) + 1),
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
                // PAGAR COMPRAS
                // ==========================

                for (int i = 1; i <= 10; i++) {
                    if (i % 2 == 0) {
                        compraCtrl.procesarPago((long) i);
                        System.out.println("✅ Compra PAGADA.");
                    } else {
                        System.out.println("⏳ Compra pendiente de pago.");
                    }
                }
                
                
                // ==========================
                // CREAR 10 EN BIBLIOTECA
                // ==========================

                for (int i = 1; i <= 10; i++) {

                    bibliotecaCtrl.anadirJuego(
                            (long) (random.nextInt(15) + 1),
                            (long) (random.nextInt(30) + 1)
                    );
                }
//
//                // ==========================
//                // CREAR 10 RESEÑAS
//                // ==========================
//
//                for (int i = 1; i <= 10; i++) {
//
//                    resenaCtrl.escribirResena(
//                            (long) (random.nextInt(15) + 1),
//                            (long) (random.nextInt(30) + 1),
//                            random.nextBoolean(),
//                            "Reseña de prueba ".repeat(5) + i
//                    );
//                }

                System.out.println("✅ Datos de prueba cargados correctamente.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
