package org.davidparada.servicio;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.exceptions.PdfException;
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
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.FacturaDto;
import org.davidparada.modelo.enums.EstadoCompraEnum;
import org.davidparada.modelo.enums.TipoErrorEnum;
import org.davidparada.modelo.formulario.validacion.ErrorModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.davidparada.controlador.util.ComprobarErrores.comprobarListaErrores;

public class PdfServicio {
    private static final Double IVA = 0.21;

    public static String generarFacturaPDF(FacturaDto factura) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();

        // Ruta de las facturas
        String ruta;
        if(factura.estadoCompra().equals(EstadoCompraEnum.REEMBOLSADA)) {
            ruta = "documentacion/facturas/" + "R-" + factura.numeroFactura() + "_TeisGame.pdf";
        } else {
            ruta = "documentacion/facturas/" + "P-" + factura.numeroFactura() + "_TeisGame.pdf";
        }

        // Ruta de la imagen
        String rutaImagen = "src/main/resources/imagen/logo.png";

        try {
            // Crea directorio
            new File("documentacion/facturas").mkdirs();

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
            float[] columnasInicio = {1, 1};
            Table tablaInicio = new Table(UnitValue.createPercentArray(columnasInicio));
            tablaInicio.useAllAvailableWidth();
            tablaInicio.setBorder(Border.NO_BORDER);

            // Logo
            Image imagenLogo = new Image(imageData);
            imagenLogo.scaleToFit(100, 100);
            DeviceRgb colorBorde = new DeviceRgb(85, 150, 240);
            imagenLogo.setBorder(new SolidBorder(colorBorde, 2));

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
                    .add(new Paragraph(new Text("Fecha de emisión").setTextAlignment(TextAlignment.CENTER)).setBold())
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
            float[] columnasDatos = {1, 1, 1};
            Table tablaDatos = new Table(UnitValue.createPercentArray(columnasDatos));
            tablaDatos.useAllAvailableWidth();
            tablaDatos.setBorder(Border.NO_BORDER);

            // Cabeceras
            Cell cabecera1 = new Cell()
                    .add(new Paragraph("FACTURAR A")
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12))
                    .setBorderTop(Border.NO_BORDER)
                    .setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER);
            Cell cabecera2 = new Cell()
                    .add(new Paragraph("DETALLES")
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12))
                    .setBorderTop(Border.NO_BORDER)
                    .setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER);
            Cell cabecera3 = new Cell()
                    .add(new Paragraph("PAGO")
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(12))
                    .setBorderTop(Border.NO_BORDER)
                    .setBorderLeft(Border.NO_BORDER)
                    .setBorderRight(Border.NO_BORDER);

            tablaDatos.addCell(cabecera1);
            tablaDatos.addCell(cabecera2);
            tablaDatos.addCell(cabecera3);

            // Datos cliente
            tablaDatos.addCell(new Cell()
                    .add(new Paragraph(factura.nombreReal())
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(10))
                    .setBorder(Border.NO_BORDER));
            tablaDatos.addCell(new Cell()
                    .add(new Paragraph("")
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(10))
                    .setBorder(Border.NO_BORDER));
            tablaDatos.addCell(new Cell()
                    .add(new Paragraph(factura.metodoPago().toString())
                            .setTextAlignment(TextAlignment.CENTER)
                            .setFontSize(10))
                    .setBorder(Border.NO_BORDER));

            document.add(tablaDatos);

            // Separador
            LineSeparator linea2 = new LineSeparator(separador);
            linea2.setWidth(UnitValue.createPercentValue(100));
            linea2.setMarginTop(70);
            linea2.setMarginBottom(15);

            document.add(linea2);

            // Bloque de datos juego
            float[] columnasJuego = {2, 1, 1, 1};
            Table tablaJuego = new Table(UnitValue.createPercentArray(columnasJuego));
            tablaJuego.useAllAvailableWidth();
            tablaJuego.setBorder(Border.NO_BORDER);

            // Cabeceras
            DeviceRgb colorRelleno = new DeviceRgb(135, 200, 240);
            Cell cabecera11 = new Cell()
                    .add(new Paragraph("TITULO")
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(colorRelleno)
                            .setFontSize(12));
            Cell cabecera12 = new Cell()
                    .add(new Paragraph("PRECIO")
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(colorRelleno)
                            .setFontSize(12));
            Cell cabecera13 = new Cell()
                    .add(new Paragraph("DESCUENTO")
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(colorRelleno)
                            .setFontSize(12));
            Cell cabecera14 = new Cell()
                    .add(new Paragraph("IMPORTE")
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBackgroundColor(colorRelleno)
                            .setFontSize(12));

            tablaJuego.addCell(cabecera11);
            tablaJuego.addCell(cabecera12);
            tablaJuego.addCell(cabecera13);
            tablaJuego.addCell(cabecera14);

            // Datos juego
            DeviceRgb colorFactura1 = new DeviceRgb(220, 220, 220);
            DeviceRgb colorFactura2 = new DeviceRgb(240, 240, 240);

            tablaJuego.addCell(new Cell()
                    .add(new Paragraph(factura.tituloJuego())
                            .setTextAlignment(TextAlignment.LEFT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));
            tablaJuego.addCell(new Cell()
                    .add(new Paragraph(factura.precioBase().toString() + " €")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));
            tablaJuego.addCell(new Cell()
                    .add(new Paragraph(factura.descuento().toString() + " %")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));
            tablaJuego.addCell(new Cell()
                    .add(new Paragraph(factura.importe().toString() + " €")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));

            tablaJuego.addCell(new Cell()
                    .add(new Paragraph("")
                            .setTextAlignment(TextAlignment.LEFT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura2)
                    .setBorder(Border.NO_BORDER));
            tablaJuego.addCell(new Cell()
                    .add(new Paragraph("")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura2)
                    .setBorder(Border.NO_BORDER));
            tablaJuego.addCell(new Cell()
                    .add(new Paragraph("")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura2)
                    .setBorder(Border.NO_BORDER));
            tablaJuego.addCell(new Cell()
                    .add(new Paragraph("")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura2)
                    .setBorder(Border.NO_BORDER));

            anadirCeldaVacia(tablaJuego, colorFactura2);
            anadirCeldaVacia(tablaJuego, colorFactura2);
            anadirCeldaVacia(tablaJuego, colorFactura2);
            anadirCeldaVacia(tablaJuego, colorFactura2);

            anadirCeldaVacia(tablaJuego, colorFactura1);
            anadirCeldaVacia(tablaJuego, colorFactura1);
            anadirCeldaVacia(tablaJuego, colorFactura1);
            anadirCeldaVacia(tablaJuego, colorFactura1);

            document.add(tablaJuego);

            // Desglose final

            Double precioSinIva = factura.importe() - factura.importe() * IVA;
            Double iva = factura.importe() * IVA;
            DeviceRgb sinColor = new DeviceRgb(254, 254, 254);
            float[] columnasDesglose = {2, 1, 1, 1};
            Table tablaDesglose = new Table(UnitValue.createPercentArray(columnasDesglose));
            tablaDesglose.useAllAvailableWidth();
            tablaDesglose.setBorder(Border.NO_BORDER);

            anadirCeldaVacia(tablaDesglose, sinColor);
            anadirCeldaVacia(tablaDesglose, sinColor);
            tablaDesglose.addCell(new Cell()
                    .add(new Paragraph("Precio Base")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));
            tablaDesglose.addCell(new Cell()
                    .add(new Paragraph(precioSinIva + " €")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));

            anadirCeldaVacia(tablaDesglose, sinColor);
            anadirCeldaVacia(tablaDesglose, sinColor);
            tablaDesglose.addCell(new Cell()
                    .add(new Paragraph("IVA (21%)")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));
            tablaDesglose.addCell(new Cell()
                    .add(new Paragraph(iva + " €")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));

            anadirCeldaVacia(tablaDesglose, sinColor);
            anadirCeldaVacia(tablaDesglose, sinColor);
            tablaDesglose.addCell(new Cell()
                    .add(new Paragraph("TOTAL A PAGAR")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));
            tablaDesglose.addCell(new Cell()
                    .add(new Paragraph(factura.importe() + " €")
                            .setTextAlignment(TextAlignment.RIGHT)
                            .setFontSize(10))
                    .setBackgroundColor(colorFactura1)
                    .setBorder(Border.NO_BORDER));

            tablaDesglose.setMarginTop(20);
            tablaDesglose.setMarginBottom(15);
            document.add(tablaDesglose);


            // Estado de la compra
            String mensaje = factura.estadoCompra().equals(EstadoCompraEnum.COMPLETADA) ? "PAGADA" : "REEMBOLSADA";
            document.add(new Paragraph().add(new Text(mensaje)
                            .setBold())
                    .setFontSize(30)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.RED)
                    .setMarginTop(175));


            // Cerrar
            document.close();

            System.out.println("PDF generado correctamente");

        } catch (FileNotFoundException e) {
            // System.err.println("No se pudo crear el archivo PDF: " + e.getMessage());
            errores.add (new ErrorModel("Archivos", TipoErrorEnum.ARCHIVO_NO_CREADO));
        } catch (IOException e) {
            // System.err.println("Error al leer recursos (imagen): " + e.getMessage());
            errores.add(new ErrorModel("Leer recursos", TipoErrorEnum.NO_SE_PUEDE_LEER_ARCHIVO));
        } catch (PdfException e) {
            // System.err.println("Error generando el PDF: " + e.getMessage());
            errores.add(new ErrorModel("PDF", TipoErrorEnum.NO_SE_PUEDE_CREAR_PDF));
        }

        comprobarListaErrores(errores);
        return ruta;
    }

    private static void anadirCeldaVacia(Table tabla, DeviceRgb color) {
        tabla.addCell(new Cell()
                .add(new Paragraph("\u00a0").setFontSize(10))
                .setBackgroundColor(color)
                .setBorder(Border.NO_BORDER));
    }


    public static String moverFacturaReembolsada(FacturaDto factura) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();

        // Crea directorio
        new File("documentacion/facturasReembolsadas").mkdirs();

        Path origen = Paths.get("documentacion/facturas", "P-" + factura.numeroFactura() + "_TeisGame.pdf");
        Path destino = Paths.get("documentacion/facturasReembolsadas", "P-" + factura.numeroFactura() + "_TeisGame.pdf");

        generarFacturaPDF(factura);

        try {
            if (!Files.exists(origen)) {
                // System.err.println("El archivo de origen no existe: " + origen.toAbsolutePath());
                errores.add(new ErrorModel("Archivo", TipoErrorEnum.ARCHIVO_NO_ENCONTRADO));
                return null;
            }
            Path directorioDestino = destino.getParent();
            if (directorioDestino != null && Files.notExists(directorioDestino)) {
                Files.createDirectories(directorioDestino);
            }

            Files.move(origen, destino, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Archivo movido correctamente a: " + destino.getFileName());

        } catch (IOException e) {
            // System.err.println("Error de E/S al mover el archivo: " + e.getMessage());
            errores.add(new ErrorModel("Archivos", TipoErrorEnum.NO_SE_PUEDE_MOVER_ARCHIVO));
        }
        comprobarListaErrores(errores);
        return destino.toString();
    }
}
