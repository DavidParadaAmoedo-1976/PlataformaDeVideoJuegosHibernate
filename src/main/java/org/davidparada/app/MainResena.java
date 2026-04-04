package org.davidparada.app;

import org.davidparada.controlador.BibliotecaControlador;
import org.davidparada.controlador.ResenaControlador;
import org.davidparada.controlador.UsuarioControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.ResenaDto;
import org.davidparada.modelo.dto.UsuarioDto;
import org.davidparada.modelo.enums.EstadoCuentaEnum;
import org.davidparada.modelo.enums.OrdenarResenaEnum;
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

public class MainResena {

    public static final Scanner scanner = new Scanner(System.in);

    public static void pausa() {
        System.out.println("\n⏸️ Pulsa ENTER para continuar...");
        scanner.nextLine();
    }

    public static void main(String[] args) {

        // 🔧 Dependencias
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

        ResenaControlador resenaControlador =
                new ResenaControlador(resenaRepo, obtener, gestor);

        try {

            // =========================
            // 👤 1. CREAR USUARIO
            // =========================
            UsuarioForm usuario = new UsuarioForm(
                    "userResena12",
                    "resena12@email.com",
                    "1234Password",
                    "Nombre Apellido",
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

            Long idUsuario = usuarioCreado.idUsuario();

            // =========================
            // 🎮 2. AÑADIR JUEGO A BIBLIOTECA
            // =========================
            System.out.println("\n🎮 Añadir juego (ID 1):");

            bibliotecaControlador.anadirJuego(idUsuario, 1L);

            pausa();

            // =========================
            // ✍️ 3. CREAR RESEÑA
            // =========================
            System.out.println("\n✍️ Crear reseña:");

            ResenaDto resena = resenaControlador.escribirResena(
                    idUsuario,
                    1L,
                    true,
                    "Muy buen juego 🔥".repeat(5)
            );

            System.out.println(resena);

            pausa();

            Long idResena = resena.idResena();

            // =========================
            // 📋 4. VER RESEÑAS DEL JUEGO
            // =========================
            System.out.println("\n📋 Reseñas del juego:");

            List<ResenaDto> resenasJuego =
                    resenaControlador.obtenerResenas(
                            1L,
                            true,
                            OrdenarResenaEnum.RECIENTES
                    );

            resenasJuego.forEach(System.out::println);

            pausa();

            // =========================
            // 👤 5. RESEÑAS DEL USUARIO
            // =========================
            System.out.println("\n👤 Reseñas del usuario:");

            List<ResenaDto> resenasUsuario =
                    resenaControlador.obtenerResenasUsuario(idUsuario);

            resenasUsuario.forEach(System.out::println);

            pausa();

            // =========================
            // 📊 6. ESTADÍSTICAS
            // =========================
            System.out.println("\n📊 Estadísticas del juego:");

            System.out.println(
                    resenaControlador.consultarEstadisticasResenaPorJuego(1L)
            );

            pausa();

            // =========================
            // 🙈 7. OCULTAR RESEÑA
            // =========================
            System.out.println("\n🙈 Ocultar reseña:");

            ResenaDto oculta =
                    resenaControlador.ocultarResena(idResena, idUsuario);

            System.out.println(oculta);

            pausa();

            // =========================
            // ❌ 8. ELIMINAR RESEÑA
            // =========================
            System.out.println("\n❌ Eliminar reseña:");

            ResenaDto eliminada =
                    resenaControlador.eliminarResena(idResena, idUsuario);

            System.out.println(eliminada);

        } catch (ValidationException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("💥 Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}