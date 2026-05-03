//package org.DavidParada.vista.componente;
//
//
//
//import org.DavidParada.vista.componente.uiState.TarjetaPanelUIState;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.function.Consumer;
//
//public class TarjetaPanel extends JPanel {
//
//
//    // Paneles
//    private JPanel mainPanel, extrasPanel, precioPanel, tituloPanel, botonesPanel;
//
//    // Imagen
//    private ImagenLabel imagenLabel;
//
//    // Labels
//    private JLabel tituloLabel, estadoLabel, descuentoLabel, precioLabel, descripcionLabel, lenguajeLabel;
//
//    // botones
//    private BotonPricipal botonPricipal;
//    private BotonSecundario botonSecundario;
//    public BotonPeligro botonPeligro;
//    //___________________________________________________________________________
//
//    private TarjetaPanelUIState uiState;
//
//    public TarjetaPanel(TarjetaPanelUIState uiState) {
//
//        this.uiState = uiState;
//
//        iniciarComponentes();
//        setBotonesAccion(uiState.getAccionPrincipal(), uiState.getAccionSecundario(),  uiState.getAccionPeligro());
//        anadirComponentes();
//        pintarTarjeta();
//    }
//
//    private void setBotonesAccion(
//            Consumer<Long> accionPrincipal,
//            Consumer<Long> accionSecundario,
//            Consumer<Long> accionPeligro) {
//
//        Long id = uiState.getId();
//
//        if (accionPrincipal != null)
//            botonPricipal.addActionListener(e -> accionPrincipal.accept(id));
//
//        if (accionSecundario != null)
//            botonSecundario.addActionListener(e -> accionSecundario.accept(id));
//
//        if (accionPeligro != null)
//            botonPeligro.addActionListener(e -> accionPeligro.accept(id));
//    }
//
//
/// /    private void iniciarComponentes() {
/// /        mainPanel = new JPanel();
/// /        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
/// /        extrasPanel = new JPanel();
/// /
/// /        precioPanel = new JPanel();
/// /        tituloPanel = new JPanel();
/// /        botonesPanel = new JPanel();
/// /
/// /
/// /        tituloLabel = new JLabel("Titulo");
/// /        estadoLabel = new JLabel("Estado");
/// /        descuentoLabel = new JLabel("Descuento");
/// /        precioLabel = new JLabel("Precio");
/// /        descripcionLabel = new JLabel("Descripcion");
/// /        lenguajeLabel = new JLabel("Lenguaje");
/// /
/// /
/// /    }
//private void iniciarComponentes() {
//
//    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
//
//    mainPanel = new JPanel();
//    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
//
//    extrasPanel = new JPanel();
//    precioPanel = new JPanel();
//    tituloPanel = new JPanel();
//    botonesPanel = new JPanel();
//
//    imagenLabel = new ImagenLabel(uiState.getRutaImagen());
//
//    tituloLabel = new JLabel();
//    estadoLabel = new JLabel();
//    descuentoLabel = new JLabel();
//    precioLabel = new JLabel();
//    descripcionLabel = new JLabel();
//    lenguajeLabel = new JLabel(uiState.getLenguaje());
//
//    botonPricipal = new BotonPrincipal(uiState.getTextoBotonPrincipal());
//    botonSecundario = new BotonSecundario(uiState.getTextoBotonSecundario());
//    botonPeligro = new BotonPeligro(uiState.getTextoBotonPeligro());
//
//
//    botonPricipal.setPreferredSize(new Dimension(140, 40));
//    botonSecundario.setPreferredSize(new Dimension(140, 40));
//    botonPeligro.setPreferredSize(new Dimension(140, 40));
//}
//
//
//    private void anadirComponentes() {
//
//        this.add(imagenLabel);
//
//        tituloPanel.add(tituloLabel);
//        tituloPanel.add(estadoLabel);
//        mainPanel.add(tituloPanel);
//
//        mainPanel.add(descripcionLabel);
//
//        botonesPanel.add(botonPricipal);
//        botonesPanel.add(botonSecundario);
//        botonesPanel.add(botonPeligro);
//        mainPanel.add(botonesPanel);
//
//        this.add(mainPanel);
//
//        precioPanel.add(precioLabel);
//        precioPanel.add(descuentoLabel);
//
//        extrasPanel.add(precioPanel);
//        extrasPanel.add(lenguajeLabel);
//
//        this.add(extrasPanel);
//    }
//
//
/// /    public void pintarTarjeta() {
/// /        tituloLabel.setText(this.uiState.getTitulo());
/// /        descripcionLabel.setText(this.uiState.getDescripcion());
/// /        descuentoLabel.setText(this.uiState.getDescuentoTarjeta());
/// /
/// /        if (this.uiState.getEstado() == null || this.uiState.getEstado().isEmpty()) {
/// /            estadoLabel.setVisible(false);
/// /        } else {
/// /            estadoLabel.setVisible(true);
/// /            estadoLabel.setText(this.uiState.getEstado());
/// /        }
/// /
/// /        if (this.descuentoLabel == null || descuentoLabel.getText().isEmpty()) {
/// /            descuentoLabel.setVisible(true);
/// /            precioLabel.setText(String.format("%.2f", this.uiState.getPrecio()));
/// /
/// /        } else {
/// /            descuentoLabel.setVisible(true);
/// /            precioLabel.setText(String.format("<html><strike>%.2f €</strike></html>",this.uiState.getPrecio() ));
/// /
/// /        }
/// /
/// /
/// /        this.add(imagenLabel);
/// /
/// /        tituloPanel.add(tituloLabel);
/// /        tituloPanel.add(estadoLabel);
/// /        mainPanel.add(tituloPanel);
/// /
/// /        mainPanel.add(descripcionLabel);
/// /
/// /        botonesPanel.add(botonPricipal);
/// /        botonesPanel.add(botonSecundario);
/// /        botonesPanel.add(botonPeligro);
/// /        mainPanel.add(botonesPanel);
/// /        this.add(mainPanel);
/// /
/// /
/// /        precioPanel.add(precioLabel);
/// /        precioPanel.add(descuentoLabel);
/// /        extrasPanel.add(precioPanel);
/// /        extrasPanel.add(lenguajeLabel);
/// /        this.add(extrasPanel);
/// /
/// /
/// /    }
//public void pintarTarjeta() {
//
//    tituloLabel.setText(uiState.getTitulo());
//    descripcionLabel.setText(uiState.getDescripcion());
//    lenguajeLabel.setText(uiState.getLenguaje());
//
//    // Estado
//    if (uiState.getEstado() == null || uiState.getEstado().isEmpty()) {
//        estadoLabel.setVisible(false);
//    } else {
//        estadoLabel.setVisible(true);
//        estadoLabel.setText(uiState.getEstado());
//    }
//
//    // Precio y descuento
//    if (uiState.getDescuentoTarjeta() == null || uiState.getDescuentoTarjeta().isEmpty()) {
//
//        descuentoLabel.setVisible(false);
//        precioLabel.setText(String.format("%.2f €", uiState.getPrecio()));
//
//    } else {
//
//        descuentoLabel.setVisible(true);
//        descuentoLabel.setText(uiState.getDescuentoTarjeta());
//
//        precioLabel.setText(
//                String.format("<html><strike>%.2f €</strike></html>", uiState.getPrecio())
//        );
//    }
//}
//
//}

package org.davidparada.vista.componente;

import org.davidparada.vista.componente.uiState.TarjetaPanelUIState;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TarjetaPanel extends JPanel {

    // Paneles de estructura
    private JPanel mainPanel, extrasPanel, precioPanel, tituloPanel, botonesPanel;

    // Componentes visuales
    private JLabel imagenLabel;
    private JLabel tituloLabel, estadoLabel, descuentoLabel, precioLabel, descripcionLabel, lenguajeLabel;

    // Botones personalizados
    private BotonPrincipal botonPrincipal;
    private BotonSecundario botonSecundario;
    private BotonPeligro botonPeligro;

    private final TarjetaPanelUIState uiState;

    public TarjetaPanel(TarjetaPanelUIState uiState) {
        this.uiState = uiState;

        iniciarComponentes();
        anadirComponentes();
        pintarTarjeta();

        // Estilo del panel contenedor (la tarjeta en sí)
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(10, 10));
    }

    private void iniciarComponentes() {
        // Inicialización de etiquetas con fuentes básicas
        tituloLabel = new JLabel();
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 16));

        estadoLabel = new JLabel();
        estadoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        estadoLabel.setForeground(new Color(0, 150, 0)); // Verde para estados positivos

        descripcionLabel = new JLabel();
        descripcionLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        precioLabel = new JLabel();
        precioLabel.setFont(new Font("Arial", Font.BOLD, 14));

        descuentoLabel = new JLabel();
        descuentoLabel.setForeground(Color.WHITE);
        descuentoLabel.setBackground(new Color(76, 175, 80));
        descuentoLabel.setOpaque(true);

        lenguajeLabel = new JLabel();
        lenguajeLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        lenguajeLabel.setForeground(Color.GRAY);

        // Imagen (Usando JLabel estándar si ImagenLabel es solo un contenedor vacío)
        // Aquí podrías cargar un ImageIcon real usando uiState.getRutaImagen()
        imagenLabel = new JLabel("IMAGEN", SwingConstants.CENTER);
        imagenLabel.setPreferredSize(new Dimension(150, 100));
        imagenLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Botones usando el ID y las acciones del UIState
        botonPrincipal = new BotonPrincipal(
                uiState.getTextoBotonPrincipal(), 120, 35,
                e -> { if(uiState.getAccionPrincipal() != null) uiState.getAccionPrincipal().accept(uiState.getId()); }
        );

        botonSecundario = new BotonSecundario(
                uiState.getTextoBotonSecundario(), 120, 35,
                e -> { if(uiState.getAccionSecundario() != null) uiState.getAccionSecundario().accept(uiState.getId()); }
        );

        botonPeligro = new BotonPeligro(
                uiState.getTextoBotonPeligro(), 120, 35,
                e -> { if(uiState.getAccionPeligro() != null) uiState.getAccionPeligro().accept(uiState.getId()); }
        );

        // Configurar visibilidad inicial de botones
        botonPrincipal.setVisible(uiState.getTextoBotonPrincipal() != null);
        botonSecundario.setVisible(uiState.getTextoBotonSecundario() != null);
        botonPeligro.setVisible(uiState.getTextoBotonPeligro() != null);
    }

    private void anadirComponentes() {
        // 1. Panel de Título y Estado (Norte del panel interno)
        tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setOpaque(false);
        tituloPanel.add(tituloLabel, BorderLayout.WEST);
        tituloPanel.add(estadoLabel, BorderLayout.EAST);

        // 2. Panel Central (Descripción y Botones)
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.add(tituloPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(descripcionLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        botonesPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        botonesPanel.setOpaque(false);
        botonesPanel.add(botonPrincipal);
        botonesPanel.add(botonSecundario);
        botonesPanel.add(botonPeligro);
        mainPanel.add(botonesPanel);

        // 3. Panel de Precio y Extras (Sur)
        precioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        precioPanel.setOpaque(false);
        precioPanel.add(precioLabel);
        precioPanel.add(descuentoLabel);

        extrasPanel = new JPanel(new BorderLayout());
        extrasPanel.setOpaque(false);
        extrasPanel.add(precioPanel, BorderLayout.WEST);
        extrasPanel.add(lenguajeLabel, BorderLayout.EAST);

        // Ensamblar todo en el BorderLayout de la clase
        add(imagenLabel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        add(extrasPanel, BorderLayout.SOUTH);
    }

    public void pintarTarjeta() {
        tituloLabel.setText(uiState.getTitulo());

        // Acortar descripción si es muy larga para la tarjeta
        String desc = uiState.getDescripcion();
        if (desc != null && desc.length() > 60) desc = desc.substring(0, 57) + "...";
        descripcionLabel.setText(desc);

        lenguajeLabel.setText("Idiomas: " + uiState.getLenguaje());

        // Manejo del estado
        if (uiState.getEstado() == null || uiState.getEstado().isEmpty()) {
            estadoLabel.setVisible(false);
        } else {
            estadoLabel.setVisible(true);
            estadoLabel.setText(uiState.getEstado());
        }

        // Lógica de Precios y Descuento
        if (uiState.getDescuento() == null || uiState.getDescuento() <= 0) {
            descuentoLabel.setVisible(false);
            precioLabel.setText(String.format("%.2f €", uiState.getPrecio()));
        } else {
            descuentoLabel.setVisible(true);
            descuentoLabel.setText(" " + uiState.getDescuentoFormateado() + " ");

            // HTML para el efecto de tachado en el precio original
            precioLabel.setText(String.format("<html><strike>%.2f €</strike> <font color='red'>%.2f €</font></html>",
                    uiState.getPrecio(),
                    uiState.getPrecioConDescuento()));
        }

        // Refrescar UI
        revalidate();
        repaint();
    }
}
