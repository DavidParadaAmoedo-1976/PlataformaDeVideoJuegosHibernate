package org.davidparada;

import org.davidparada.controlador.*;
import org.davidparada.modelo.dto.*;
import org.davidparada.modelo.enums.*;
import org.davidparada.modelo.formulario.*;
import org.davidparada.repositorio.implementacionHibernate.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class Programa {

    public static void main(String[] args) {

        try {

            // =========================
            // REPOS (OBLIGATORIO EN TU DISEÑO)
            // =========================
            var usuarioRepo = new UsuarioRepoHibernate();
            var juegoRepo = new JuegosRepoHibernate();
            var compraRepo = new CompraRepoHibernate();
            var bibliotecaRepo = new BibliotecaRepoHibernate();
            var resenaRepo = new ResenaRepoHibernate();

            // =========================
            // CONTROLADORES (CON DEPENDENCIAS)
            // =========================
            UsuarioControlador usuarioCtrl = new UsuarioControlador(usuarioRepo);
            JuegoControlador juegoCtrl = new JuegoControlador(juegoRepo);
            BibliotecaControlador bibliotecaCtrl = new BibliotecaControlador(bibliotecaRepo, juegoRepo);
            CompraControlador compraCtrl = new CompraControlador(
                    compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, bibliotecaCtrl
            );
            ResenaControlador resenaCtrl = new ResenaControlador(resenaRepo);

            System.out.println("=== INICIO ===");

            // =========================
            // 1. USUARIO
            // =========================
            UsuarioForm usuarioForm = new UsuarioForm(
                    "usuario_ok",
                    "ok@email.com",
                    "Password123",
                    "Nombre Real",
                    PaisEnum.ESPANA,
                    LocalDate.of(2000, 1, 1),
                    Instant.now(),
                    "avatar.png",
                    100.0,
                    EstadoCuentaEnum.ACTIVA
            );

            UsuarioDto usuario = usuarioCtrl.registrarUsuario(usuarioForm);
            System.out.println("Usuario: " + usuario.nombreUsuario());

            // =========================
            // 2. JUEGO
            // =========================
            JuegoForm juegoForm = new JuegoForm(
                    "Juego OK",
                    "Descripción",
                    "Dev",
                    LocalDate.of(2023, 1, 1),
                    20.0,
                    0,
                    "Accion",
                    ClasificacionJuegoEnum.PEGI_18,
                    List.of("ES"),
                    EstadoJuegoEnum.DISPONIBLE
            );

            JuegoDto juego = juegoCtrl.crearJuego(juegoForm);
            System.out.println("Juego: " + juego.titulo());

            // =========================
            // 3. COMPRA
            // =========================
            CompraDto compra = compraCtrl.realizarCompra(
                    usuario.idUsuario(),
                    juego.idJuego(),
                    MetodoPagoEnum.TARJETA
            );

            System.out.println("Compra ID: " + compra.idCompra());

            // =========================
            // 4. PROCESAR PAGO
            // =========================
            CompraDto compraFinal = compraCtrl.procesarPago(
                    compra.idCompra(),
                    MetodoPagoEnum.TARJETA
            );

            System.out.println("Estado compra: " + compraFinal.estadoCompra());

            // =========================
            // 5. BIBLIOTECA
            // =========================
            List<BibliotecaDto> biblioteca =
                    bibliotecaCtrl.verBiblioteca(usuario.idUsuario(), null);

            System.out.println("Biblioteca:");
            for (BibliotecaDto b : biblioteca) {
                System.out.println("- " + b.juegoDto().titulo());
            }

            // =========================
            // 6. RESEÑA
            // =========================
            ResenaDto resena = resenaCtrl.escribirResena(
                    usuario.idUsuario(),
                    juego.idJuego(),
                    true,
                    "Este juego es muy bueno y cumple perfectamente con lo esperado."
            );

            System.out.println("Reseña ID: " + resena.idResena());

            System.out.println("=== FIN ===");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}