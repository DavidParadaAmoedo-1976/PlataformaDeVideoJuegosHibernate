package org.davidparada.vista.panel;

import org.davidparada.modelo.dto.JuegoDto;
import org.davidparada.vista.componente.TarjetaPanel;
import org.davidparada.vista.componente.uiState.TarjetaPanelUIState;
import org.davidparada.vista.util.Navegador;

import javax.swing.*;
import java.awt.*;

public class PanelDetalleJuego extends JPanel {

    private Navegador nav;

    public PanelDetalleJuego(Navegador nav) {
        this.nav = nav;
        setLayout(new GridBagLayout()); // Para centrar la tarjeta
    }

    public void cargarJuego(JuegoDto juego) {
        removeAll(); // Limpiar el anterior

        // Mapeamos el DTO al UIState de la tarjeta
        TarjetaPanelUIState state = new TarjetaPanelUIState(
                juego.idJuego(),
                juego.titulo(),
                juego.descripcion(),
                juego.estado().getDescripcion(),
                String.join(", ", juego.idiomas()),
                juego.precioBase(),
                juego.descuento(),
                null, // ruta imagen
                "Comprar",
                "Volver al catálogo",
                null,
                id -> System.out.println("Comprando juego: " + id),
                id -> nav.irA("catalogo"), // Acción del botón secundario: Volver
                null
        );

        add(new TarjetaPanel(state));

        revalidate();
        repaint();
    }
}
