package org.davidparada.app;

import org.davidparada.controlador.BibliotecaControlador;
import org.davidparada.controlador.UsuarioControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.BibliotecaDto;
import org.davidparada.modelo.dto.UsuarioDto;
import org.davidparada.modelo.enums.EstadoCuentaEnum;
import org.davidparada.modelo.enums.OrdenarJuegosBibliotecaEnum;
import org.davidparada.modelo.enums.PaisEnum;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.repositorio.implementacionHibernate.*;
import org.davidparada.repositorio.interfaceRepositorio.*;
import org.davidparada.transaciones.GestorTransaccionesHibernate;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MainBiblioteca {

    public static final Scanner scanner = new Scanner(System.in);

    public static void pausa() {
        System.out.println("\n⏸️ Pulsa ENTER para continuar...");
        scanner.nextLine();
    }

    public static void main(String[] args) {

        // 🔧 Inicializar dependencias
        IGestorTransacciones gestor = new GestorTransaccionesHibernate();
        ISessionManager sessionManager = (ISessionManager) gestor;

        ICompraRepo compraRepo = new CompraRepoHibernate(sessionManager);
        IUsuarioRepo usuarioRepo = new UsuarioRepoHibernate(sessionManager);
        IBibliotecaRepo bibliotecaRepo = new BibliotecaRepoHibernate(sessionManager);
        IResenaRepo resenaRepo = new ResenaRepoHibernate(sessionManager);
        IJuegoRepo juegoRepo = new JuegoRepoHibernate(sessionManager);

        ObtenerEntidadesOptional obtener =
                new ObtenerEntidadesOptional(compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, resenaRepo);

        // Controladores
        UsuarioControlador usuarioControlador =
                new UsuarioControlador(usuarioRepo, obtener, gestor);

        BibliotecaControlador bibliotecaControlador =
                new BibliotecaControlador(bibliotecaRepo, juegoRepo, obtener, gestor);

        try {

            // =========================
            // 👤 1. CREAR USUARIO
            // =========================
            UsuarioForm usuario = new UsuarioForm(
                    "qwerr3",
                    "qwerr3@email.com",
                    "1234Pasword",
                    "Asdf ghjk",
                    PaisEnum.ESPANA,
                    LocalDate.of(2000, 1, 1),
                    Instant.now(),
                    "avatar.png",
                    0.0,
                    EstadoCuentaEnum.ACTIVA
            );

            UsuarioDto usuarioCreado = usuarioControlador.registrarUsuario(usuario);
            System.out.println("✅ Usuario creado:");
            System.out.println(usuarioCreado);

            pausa();

            // ⚠️ IMPORTANTE:
            // Aquí debes asegurarte de que EXISTAN juegos en BD
            // (o crearlos antes si tienes controlador de juegos)

            Long idUsuario = usuarioCreado.idUsuario();

            // =========================
            // 🎮 2. AÑADIR JUEGO
            // =========================
            System.out.println("\n🎮 Añadir juego (ID 1):");

            BibliotecaDto b1 = bibliotecaControlador.anadirJuego(idUsuario, 1L);
            System.out.println(b1);

            pausa();

            // =========================
            // 📚 3. VER BIBLIOTECA
            // =========================
            System.out.println("\n📚 Biblioteca del usuario:");

            List<BibliotecaDto> biblioteca =
                    bibliotecaControlador.verBiblioteca(idUsuario, null);

            biblioteca.forEach(System.out::println);

            pausa();

            // =========================
            // ⏱️ 4. ACTUALIZAR HORAS
            // =========================
            System.out.println("\n⏱️ Añadir 5 horas de juego:");

            BibliotecaDto actualizado =
                    bibliotecaControlador.actualizarTiempoDeJuego(idUsuario, 1L, 5.0);

            System.out.println(actualizado);

            pausa();

            // =========================
            // 🔎 5. ORDENAR BIBLIOTECA
            // =========================
            System.out.println("\n🔎 Biblioteca ordenada por horas:");

            List<BibliotecaDto> ordenados =
                    bibliotecaControlador.verBiblioteca(
                            idUsuario,
                            OrdenarJuegosBibliotecaEnum.TIEMPO_DE_JUEGO
                    );

            ordenados.forEach(System.out::println);

            pausa();

            // =========================
            // 🔍 6. FILTRAR
            // =========================
            System.out.println("\n🔍 Buscar por texto 'game':");

            List<BibliotecaDto> filtrados =
                    bibliotecaControlador.buscarSegunCriterios(idUsuario, "game", null);

            filtrados.forEach(System.out::println);

            pausa();

            // =========================
            // ❌ 7. ELIMINAR JUEGO
            // =========================
            System.out.println("\n❌ Eliminar juego:");

            BibliotecaDto eliminado =
                    bibliotecaControlador.eliminarJuego(idUsuario, 1L);

            System.out.println(eliminado);

        } catch (ValidationException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("💥 Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
