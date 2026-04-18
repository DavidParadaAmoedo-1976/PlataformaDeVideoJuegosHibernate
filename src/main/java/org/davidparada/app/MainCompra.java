package org.davidparada.app;

import org.davidparada.controlador.BibliotecaControlador;
import org.davidparada.controlador.CompraControlador;
import org.davidparada.controlador.UsuarioControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.CompraDto;
import org.davidparada.modelo.dto.DetallesCompraDto;
import org.davidparada.modelo.dto.FacturaDto;
import org.davidparada.modelo.dto.UsuarioDto;
import org.davidparada.modelo.enums.EstadoCuentaEnum;
import org.davidparada.modelo.enums.MetodoPagoEnum;
import org.davidparada.modelo.enums.PaisEnum;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.repositorio.implementacionHibernate.BibliotecaRepoHibernate;
import org.davidparada.repositorio.implementacionHibernate.CompraRepoHibernate;
import org.davidparada.repositorio.implementacionHibernate.JuegoRepoHibernate;
import org.davidparada.repositorio.implementacionHibernate.UsuarioRepoHibernate;
import org.davidparada.repositorio.interfaceRepositorio.IBibliotecaRepo;
import org.davidparada.repositorio.interfaceRepositorio.ICompraRepo;
import org.davidparada.repositorio.interfaceRepositorio.IJuegoRepo;
import org.davidparada.repositorio.interfaceRepositorio.IUsuarioRepo;
import org.davidparada.servicio.PdfServicio;
import org.davidparada.transaciones.GestorTransaccionesHibernate;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MainCompra {

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
        IJuegoRepo juegoRepo = new JuegoRepoHibernate(sessionManager);

        ObtenerEntidadesOptional obtener =
                new ObtenerEntidadesOptional(compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, null);

        // Controladores
        UsuarioControlador usuarioControlador =
                new UsuarioControlador(usuarioRepo, obtener, gestor);

        BibliotecaControlador bibliotecaControlador =
                new BibliotecaControlador(bibliotecaRepo, juegoRepo, obtener, gestor);

        CompraControlador compraControlador =
                new CompraControlador(
                        compraRepo,
                        usuarioRepo,
                        juegoRepo,
                        bibliotecaRepo,
                        bibliotecaControlador,
                        obtener,
                        gestor
                );

        try {
            // =========================
            // 👤 1. CREAR USUARIO
            // =========================
            UsuarioForm usuario = new UsuarioForm(
                    "userCompra97",
                    "compra97@email.com",
                    "1234Password",
                    "Nombre Para Factura",
                    PaisEnum.ESPANA,
                    LocalDate.of(2000, 1, 1),
                    Instant.now(),
                    "avatar.png",
                    100.0, // saldo para probar cartera
                    EstadoCuentaEnum.ACTIVA
            );

            UsuarioDto usuarioCreado = usuarioControlador.registrarUsuario(usuario);
            UsuarioDto actualizado = usuarioControlador.anadirSaldo(usuarioCreado.idUsuario(), 100.0);
            System.out.println("✅ Usuario creado:");
            System.out.println(usuarioCreado);
            System.out.println(actualizado);

            pausa();

            Long idUsuario = actualizado.idUsuario();

            // =========================
            // 🛒 2. REALIZAR COMPRA
            // =========================
            System.out.println("\n🛒 Crear compra:");

            CompraDto compra = compraControlador.realizarCompra(
                    idUsuario,
                    1L,
                    MetodoPagoEnum.CARTERA_STEAM
            );

            System.out.println(compra);

            pausa();

            Long idCompra = compra.idCompra();

            // =========================
            // 💳 3. PROCESAR PAGO
            // =========================
            System.out.println("\n💳 Procesar pago:");

            CompraDto compraPagada = compraControlador.procesarPago(
                    idCompra,
                    MetodoPagoEnum.CARTERA_STEAM
            );

            System.out.println(compraPagada);

            pausa();

            // =========================
            // 📋 4. LISTAR COMPRAS
            // =========================
            System.out.println("\n📋 Historial compras:");

            List<CompraDto> compras =
                    compraControlador.listarCompras(idUsuario);

            compras.forEach(System.out::println);

            pausa();

            // =========================
            // 🔍 5. CONSULTAR COMPRA
            // =========================
            System.out.println("\n🔍 Consultar compra:");

            CompraDto consulta =
                    compraControlador.consultarCompra(idCompra, idUsuario);

            System.out.println(consulta);

            pausa();

            // =========================
            // 📦 6. DETALLES COMPRA
            // =========================
            System.out.println("\n📦 Detalles compra:");

            DetallesCompraDto detalles =
                    compraControlador.detallesDeUnaCompra(idCompra, idUsuario);

            System.out.println(detalles);

            pausa();

            // =========================
            // 🧾 7. FACTURA + PDF
            // =========================
            System.out.println("\n🧾 Factura:");

            FacturaDto factura =
                    compraControlador.generarFactura(idCompra);

            System.out.println(factura);

            // Generar PDF
            PdfServicio pdfService = new PdfServicio();

            // Crear carpeta si no existe
            new java.io.File("facturas").mkdirs();

            String rutaPdf = pdfService.generarFacturaPDF(factura);

            System.out.println("📄 PDF generado en: " + rutaPdf);

            pausa();

            // =========================
            // 💸 8. REEMBOLSO
            // =========================
            System.out.println("\n💸 Solicitar reembolso:");

            CompraDto reembolso =
                    compraControlador.solicitarReembolso(idCompra);

            System.out.println(reembolso);

        } catch (ValidationException e) {
            System.out.println("❌ Error de validación: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("💥 Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
