package org.davidparada.app;

import org.davidparada.controlador.UsuarioControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.UsuarioDto;
import org.davidparada.modelo.enums.EstadoCuentaEnum;
import org.davidparada.modelo.enums.PaisEnum;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.repositorio.implementacionHibernate.*;
import org.davidparada.repositorio.interfaceRepositorio.*;
import org.davidparada.transaciones.GestorTransaccionesHibernate;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Scanner;

public class MainUsuario {

    public static final Scanner scanner = new Scanner(System.in);

    public static void pausa() {
        System.out.println("\n⏸️ Pulsa ENTER para continuar...");
        scanner.nextLine();
    }

    public static void main(String[] args) {

        // 🔧 Inicializar dependencias
        IGestorTransacciones gestor = new GestorTransaccionesHibernate();
        ISessionManager sessionManager = (ISessionManager) gestor;

        ICompraRepo compraRepo = new CompraRepoHibernate();
        IUsuarioRepo usuarioRepo = new UsuarioRepoHibernate(sessionManager);
        IBibliotecaRepo bibliotecaRepo = new BibliotecaRepoHibernate();
        IResenaRepo resenaRepo = new ResenaRepoHibernate();
        IJuegoRepo juegoRepo = new JuegoRepoHibernate(sessionManager);

        ObtenerEntidadesOptional obtener =
                new ObtenerEntidadesOptional(compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, resenaRepo);

        UsuarioControlador controlador =
                new UsuarioControlador(usuarioRepo, obtener, gestor);

        try {

            // =========================
            // 👤 1. CREAR USUARIOS
            // =========================
            UsuarioForm usuario1 = new UsuarioForm(
                    "david123",
                    "david@email.com",
                    "1234Pasword",
                    "David Parada",
                    PaisEnum.ESPANA,
                    LocalDate.of(2000, 1, 1),
                    Instant.now(),
                    "avatar1.png",
                    0.0,
                    EstadoCuentaEnum.ACTIVA
            );

            UsuarioForm usuario2 = new UsuarioForm(
                    "ana456",
                    "ana@email.com",
                    "1234Pasword",
                    "Ana López",
                    PaisEnum.ESPANA,
                    LocalDate.of(1998, 5, 10),
                    Instant.now(),
                    "avatar2.png",
                    0.0,
                    EstadoCuentaEnum.ACTIVA
            );

            UsuarioDto creado1 = controlador.registrarUsuario(usuario1);
            UsuarioDto creado2 = controlador.registrarUsuario(usuario2);

            System.out.println("✅ Usuarios creados:");
            System.out.println(creado1);
            System.out.println(creado2);

            pausa();

            // =========================
            // 🔎 2. CONSULTAR PERFIL (ID)
            // =========================
            System.out.println("\n🔎 Consultar perfil ID 1:");

            UsuarioDto perfil = controlador.consultarPerfil(1L);
            System.out.println(perfil);

            pausa();

            // =========================
            // 🔎 3. CONSULTAR PERFIL (USERNAME)
            // =========================
            System.out.println("\n🔎 Consultar perfil por username:");

            UsuarioDto perfilNombre = controlador.consultarPerfil("david123");
            System.out.println(perfilNombre);

            pausa();

            // =========================
            // 💰 4. AÑADIR SALDO
            // =========================
            System.out.println("\n💰 Añadir saldo al usuario ID 1:");

            UsuarioDto actualizado = controlador.anadirSaldo(1L, 50.0);
            System.out.println(actualizado);

            pausa();

            // =========================
            // 💳 5. CONSULTAR SALDO
            // =========================
            System.out.println("\n💳 Consultar saldo usuario ID 1:");

            UsuarioDto saldo = controlador.consultarSaldo(1L);
            System.out.println(saldo);

        } catch (ValidationException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("💥 Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }

        pausa();
    }
}
