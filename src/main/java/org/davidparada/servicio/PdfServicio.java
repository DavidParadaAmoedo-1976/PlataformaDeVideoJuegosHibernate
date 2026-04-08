package org.davidparada.servicio;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.*;

import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import org.davidparada.modelo.dto.FacturaDto;

import java.io.File;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfServicio {

    /**
     * Genera factura estilo Steam con múltiples productos
     */
    public String generarFacturaPDF(FacturaDto factura) {

        String ruta = "facturas/factura_" + factura.numeroFactura() + "_steam.pdf";

        try {
            new File("facturas").mkdirs();

            PdfWriter writer = new PdfWriter(ruta);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // =========================
            // 🎮 CABECERA (TIPO STEAM)
            // =========================
            Paragraph titulo = new Paragraph("FACTURA")
                    .setBold()
                    .setFontSize(18)
                    .setTextAlignment(TextAlignment.RIGHT);

            document.add(titulo);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                            .withZone(ZoneId.systemDefault());

            document.add(new Paragraph("Factura Nº: " + factura.numeroFactura())
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("Fecha: " + formatter.format(factura.fechaEmision()))
                    .setTextAlignment(TextAlignment.RIGHT));

            document.add(new Paragraph("\n"));

            // =========================
            // 👤 CLIENTE (ARRIBA)
            // =========================
            document.add(new Paragraph("Cliente: " + factura.nombreReal()).setBold());
            document.add(new Paragraph(factura.email()));

            document.add(new Paragraph("\n"));

            // =========================
            // 📊 TABLA DE PRODUCTOS
            // =========================
            Table tabla = new Table(new float[]{4, 2}).useAllAvailableWidth();

            tabla.addHeaderCell(new Paragraph("Producto").setBold());
            tabla.addHeaderCell(new Paragraph("Precio").setBold());

            double subtotal = 0;

//            // 👉 MULTIPLES PRODUCTOS
//            for (int i = 0; i < juegos.size(); i++) {
//                tabla.addCell(juegos.get(i));
//
//                tabla.addCell(
//                        new Paragraph(precios.get(i) + "€")
//                                .setTextAlignment(TextAlignment.RIGHT)
//                );
//
//                subtotal += precios.get(i);
//            }
//
//            document.add(tabla);
//
//            document.add(new Paragraph("\n"));

            // =========================
            // 💰 TOTALES
            // =========================
            Table totales = new Table(1)
                    .setWidth(250)
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT);

            double descuento = factura.descuento() != null
                    ? subtotal * factura.descuento() / 100
                    : 0;

            double base = subtotal - descuento;
            double iva = base * 0.21;
            double total = base + iva;

            totales.addCell(new Paragraph("Subtotal: " + subtotal + "€"));

            if (descuento > 0) {
                totales.addCell(new Paragraph("Descuento: -" + descuento + "€"));
            }

            totales.addCell(new Paragraph("IVA (21%): " + iva + "€"));

            totales.addCell(
                    new Paragraph("TOTAL: " + total + "€")
                            .setBold()
                            .setFontSize(14)
            );

            document.add(totales);

            document.add(new Paragraph("\n"));

            // =========================
            // 💳 MÉTODO DE PAGO
            // =========================
            document.add(new Paragraph("Método de pago: " + factura.metodoPago()));

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ruta;
    }
}