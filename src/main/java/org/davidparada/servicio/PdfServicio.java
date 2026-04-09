package org.davidparada.servicio;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
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

            // Logo

            Image imgBorder = new Image(imageData);
            imgBorder.scaleToFit(50, 50);
            DeviceRgb borderColor = new DeviceRgb(85, 150, 240);
            imgBorder.setBorder(new SolidBorder(borderColor, 2));

            document.add(imgBorder);
            document.add(new Paragraph(""));



            // Cerrar

            document.close();

            System.out.println("PDF generado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ruta;
    }
}