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
