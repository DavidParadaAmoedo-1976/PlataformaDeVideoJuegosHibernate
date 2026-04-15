package org.davidparada.servicio;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.davidparada.modelo.dto.FacturaDto;

import java.io.File;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class PdfServicio {

    public String generarFacturaPDF(FacturaDto factura) {

        // Ruta de las facturas
        String ruta = "facturas/factura_" + factura.numeroFactura() + "_TeisGame.pdf";

        // Ruta de la imagen
        String rutaImagen = "src/main/resources/imagen/logo.png";


        try {
            // Crea directorio
            new File("facturas").mkdirs();

            // CREAR PDF
            PdfWriter writer = new PdfWriter(ruta);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            // CARGAR IMAGEN
            ImageData imageData = ImageDataFactory.create(rutaImagen);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String fecha = factura.fechaEmision()
                    .atZone(ZoneId.systemDefault())
                    .format(formatter);


            // Cabecera
            document.add(new Paragraph().add(new Text("FACTURA").setBold())
                    .setFontSize(24)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(5));


            // Bloque inicio
            float[] columnasInicio = {1,1};
            Table tablaInicio = new Table(UnitValue.createPercentArray(columnasInicio));
            tablaInicio.useAllAvailableWidth();
            tablaInicio.setBorder(Border.NO_BORDER);

            // Logo
            Image imagenLogo = new Image(imageData);
            imagenLogo.scaleToFit(80, 80);
            DeviceRgb borderColor = new DeviceRgb(85, 150, 240);
            imagenLogo.setBorder(new SolidBorder(borderColor, 2));

            Cell logoCelda = new Cell()
                    .add(imagenLogo)
                    .setBorder(Border.NO_BORDER)
                    .setPadding(50)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);

            tablaInicio.addCell(logoCelda);

            // Datos de Factura
            Cell datosFactura = new Cell()
                    .add(new Paragraph(new Text("Nº Factura").setTextAlignment(TextAlignment.CENTER)).setBold())
                    .add(new Paragraph(factura.numeroFactura()).setTextAlignment(TextAlignment.CENTER))
                    .add(new Paragraph(new Text("Fecha de emision").setTextAlignment(TextAlignment.CENTER)).setBold())
                    .add(new Paragraph(fecha).setTextAlignment(TextAlignment.CENTER))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
            tablaInicio.addCell(datosFactura);


            document.add(tablaInicio);

            // Separador
            SolidLine separador = new SolidLine(2);
            separador.setColor(ColorConstants.DARK_GRAY);

            LineSeparator linea = new LineSeparator(separador);
            linea.setWidth(UnitValue.createPercentValue(100));
            linea.setMarginTop(5);
            linea.setMarginBottom(15);

            document.add(linea);

            // Bloque datos cliente
            float[] columnasDatos = {1,1,1};
            Table tablaDatos = new Table(UnitValue.createPercentArray(columnasDatos));
            tablaDatos.useAllAvailableWidth();
            tablaDatos.setBorder(Border.NO_BORDER);

            // Cabeceras
            Cell cabecera1 = new Cell().add(new Paragraph("FACTURAR A").setBold().setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12)
                    .setBorder(Border.NO_BORDER));
            Cell cabecera2 = new Cell().add(new Paragraph("DETALLES").setBold().setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12)
                    .setBorder(Border.NO_BORDER));
            Cell cabecera3 = new Cell().add(new Paragraph("PAGO").setBold().setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12)
                    .setBorder(Border.NO_BORDER));

            tablaDatos.addCell(cabecera1);
            tablaDatos.addCell(cabecera2);
            tablaDatos.addCell(cabecera3);

            // Datos cliente
            tablaDatos.addCell(new Paragraph("FACTURAR A").setBold()).setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12)
                    .setBorder(Border.NO_BORDER));
            tablaDatos.addCell(new Paragraph("").setTextAlignment(TextAlignment.CENTER).setFontSize(10)
                    .setFontSize(12)
                    .setBorder(Border.NO_BORDER));
            tablaDatos.addCell(new Paragraph(factura.metodoPago().toString()).setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10)
                    .setBorder(Border.NO_BORDER));


            document.add(tablaDatos);

            // Cerrar
            document.close();

            System.out.println("PDF generado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ruta;
    }
}