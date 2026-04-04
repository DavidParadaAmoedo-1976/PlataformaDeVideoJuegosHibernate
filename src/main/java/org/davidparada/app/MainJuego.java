package org.davidparada.app;

import org.davidparada.controlador.JuegoControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.JuegoDto;
import org.davidparada.modelo.enums.ClasificacionJuegoEnum;
import org.davidparada.modelo.enums.EstadoJuegoEnum;
import org.davidparada.modelo.enums.OrdenarJuegosEnum;
import org.davidparada.modelo.formulario.JuegoForm;
import org.davidparada.repositorio.implementacionHibernate.*;
import org.davidparada.repositorio.interfaceRepositorio.*;
import org.davidparada.transaciones.GestorTransaccionesHibernate;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;


public class MainJuego {
    public static final Scanner scanner = new Scanner(System.in);

    public static void pausa() {
        System.out.println("\n⏸️ Pulsa ENTER para continuar...");
        scanner.nextLine();
    }

    static void main(String[] args) {

        // 🔧 Inicializar dependencias (usa tus implementaciones reales)
        IGestorTransacciones gestor = new GestorTransaccionesHibernate();
        ISessionManager sessionManager = (ISessionManager) gestor;

        ICompraRepo compraRepo = new CompraRepoHibernate(sessionManager);
        IUsuarioRepo usuarioRepo = new UsuarioRepoHibernate(sessionManager);
        IBibliotecaRepo bibliotecaRepo = new BibliotecaRepoHibernate(sessionManager);
        IResenaRepo resenaRepo = new ResenaRepoHibernate(sessionManager);
        IJuegoRepo juegoRepo = new JuegoRepoHibernate(sessionManager); // 👈 tu implementación
        ObtenerEntidadesOptional obtener = new ObtenerEntidadesOptional(compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, resenaRepo);

        JuegoControlador controlador = new JuegoControlador(juegoRepo, obtener, gestor);

        try {

            // =========================
            // 🎮 1. CREAR JUEGOS
            // =========================
            JuegoForm juego1 = new JuegoForm(
                    "Elden Ring",
                    "Juego souls",
                    "FromSoftware",
                    LocalDate.of(2022, 2, 25),
                    60.0,
                    0,
                    "RPG",
                    ClasificacionJuegoEnum.PEGI_18,
                    List.of("ES", "EN"),
                    EstadoJuegoEnum.DISPONIBLE
            );

            JuegoForm juego2 = new JuegoForm(
                    "FIFA 23",
                    "Juego de futbol",
                    "EA Sports",
                    LocalDate.of(2023, 9, 1),
                    50.0,
                    0,
                    "Deportes",
                    ClasificacionJuegoEnum.PEGI_12,
                    List.of("ES"),
                    EstadoJuegoEnum.PREVENTA
            );

            JuegoDto creado1 = controlador.crearJuego(juego1);
            JuegoDto creado2 = controlador.crearJuego(juego2);

            System.out.println("✅ Juegos creados:");
            System.out.println(creado1);
            System.out.println(creado2);

            pausa();
            // =========================
            // 🔍 2. BUSCAR CON FILTROS
            // =========================
            System.out.println("\n🔍 Buscar juegos por categoria RPG:");

            List<JuegoDto> filtrados = controlador.buscarJuegos(
                    null,
                    "RPG",
                    null,
                    null,
                    null,
                    null
            );

            filtrados.forEach(System.out::println);

            pausa();
            // =========================
            // 📚 3. CONSULTAR CATÁLOGO
            // =========================
            System.out.println("\n📚 Catálogo ordenado por precio:");

            List<JuegoDto> catalogo = controlador.consultarCatalogo(OrdenarJuegosEnum.PRECIO);

            catalogo.forEach(System.out::println);

            pausa();
            // =========================
            // 🔎 4. CONSULTAR DETALLES
            // =========================
            System.out.println("\n🔎 Detalles del juego ID 1:");

            JuegoDto detalles = controlador.consultarDetalles(1L);
            System.out.println(detalles);

            pausa();
            // =========================
            // 💸 5. APLICAR DESCUENTO
            // =========================
            System.out.println("\n💸 Aplicar descuento al juego ID 1:");

            JuegoDto conDescuento = controlador.aplicarDescuento(1L, 20);
            System.out.println(conDescuento);

            pausa();
            // =========================
            // 🔄 6. CAMBIAR ESTADO
            // =========================
            System.out.println("\n🔄 Cambiar estado del juego ID 2:");

            JuegoDto actualizado = controlador.cambiarEstado(2L, EstadoJuegoEnum.NO_DISPONIBLE);
            System.out.println(actualizado);

        } catch (ValidationException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("💥 Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

