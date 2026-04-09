package org.davidparada.app;

import org.davidparada.controlador.BibliotecaControlador;
import org.davidparada.controlador.CompraControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.modelo.dto.FacturaDto;
import org.davidparada.repositorio.implementacionHibernate.*;
import org.davidparada.repositorio.interfaceRepositorio.*;
import org.davidparada.servicio.PdfServicio;
import org.davidparada.transaciones.GestorTransaccionesHibernate;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;

public class MainPdfTest {

    public static void main(String[] args) {

        // 🔧 Setup mínimo
        IGestorTransacciones gestor = new GestorTransaccionesHibernate();
        ISessionManager sessionManager = (ISessionManager) gestor;

        ICompraRepo compraRepo = new CompraRepoHibernate(sessionManager);
        IUsuarioRepo usuarioRepo = new UsuarioRepoHibernate(sessionManager);
        IBibliotecaRepo bibliotecaRepo = new BibliotecaRepoHibernate(sessionManager);
        IJuegoRepo juegoRepo = new JuegoRepoHibernate(sessionManager);

        ObtenerEntidadesOptional obtener =
                new ObtenerEntidadesOptional(compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, null);

        CompraControlador compraCtrl =
                new CompraControlador(
                        compraRepo,
                        usuarioRepo,
                        juegoRepo,
                        bibliotecaRepo,
                        new BibliotecaControlador(bibliotecaRepo, juegoRepo, obtener, gestor),
                        obtener,
                        gestor
                );

        PdfServicio pdfService = new PdfServicio();

        try {
            Long idCompra = 1L; // 👈 CAMBIA ESTE ID (compra existente en BD)

            // =========================
            // 🧾 FACTURA
            // =========================
            FacturaDto factura = compraCtrl.generarFactura(idCompra);

            System.out.println("Factura: " + factura);

            // =========================
            // 📄 PDF
            // =========================
            new java.io.File("facturas").mkdirs();

            String ruta = pdfService.generarFacturaPDF(factura);

            System.out.println("PDF generado en: " + ruta);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}