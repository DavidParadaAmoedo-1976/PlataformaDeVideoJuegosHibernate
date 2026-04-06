package org.davidparada.servicio;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.davidparada.modelo.dto.FacturaDto;

import java.io.FileNotFoundException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PdfServicio {

    public String generarFacturaPDF(FacturaDto factura) {
        String ruta = "facturas/factura_" + factura.numeroFactura() + ".pdf";

        try {
            PdfWriter writer = new PdfWriter(ruta);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm VV");

            String fecha = factura.fechaEmision()
                    .atZone(ZoneId.systemDefault())
                    .format(formatter);

            document.add(new Paragraph("FACTURA"));
            document.add(new Paragraph("Número: " + factura.numeroFactura()));
            document.add(new Paragraph("Cliente: " + factura.nombreReal()));
            document.add(new Paragraph("Email: " + factura.email()));
            document.add(new Paragraph("Fecha: " + fecha));
            document.add(new Paragraph("Precio final: " + factura.importe() + "€"));
            document.add(new Paragraph("Descuento: " + factura.descuento() + "%"));
            document.add(new Paragraph("Método de pago: " + factura.metodoPago()));

            document.close();


            return ruta;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

