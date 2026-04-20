package org.davidparada.vista.panel;

import org.davidparada.vista.componente.BotonPeligro;
import org.davidparada.vista.componente.BotonPrincipal;
import org.davidparada.vista.util.Navegador;
import org.davidparada.vista.util.Sesion;

import javax.swing.*;
import java.awt.*;

public class PanelMenu extends JPanel {

    private Navegador navegador;

    public PanelMenu(Navegador navegador) {
        this.navegador = navegador;
        construirMenu();
    }

    private void construirMenu() {

        removeAll(); // limpia botones anteriores

        setLayout(new GridLayout(7, 1, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));

        // 👇 AQUI añadimos el mensaje
        if (!Sesion.esInvitado() && Sesion.getUsuarioActual() != null) {

            JLabel bienvenida = new JLabel(
                    "Bienvenido " + Sesion.getUsuarioActual().nombreUsuario(),
                    SwingConstants.CENTER
            );

            bienvenida.setFont(new Font("Arial", Font.BOLD, 18));

            add(bienvenida);
        }

        add(new BotonPrincipal("Catálogo", 200, 50, e ->
                navegador.irA("catalogo")));

        if (!Sesion.esInvitado()) {

            add(new BotonPrincipal("Biblioteca", 200, 50, e ->
                    navegador.irA("biblioteca")));

            add(new BotonPrincipal("Perfil", 200, 50, e ->
                    navegador.irA("perfil")));

            add(new BotonPrincipal("Compras", 200, 50, e ->
                    navegador.irA("compras")));
        }

        add(new BotonPrincipal("Reportes", 200, 50, e ->
                navegador.irA("reportes")));

        add(new BotonPeligro("Salir", 200, 50, e ->
                System.exit(0)));

        revalidate();
        repaint();
    }

    public void refrescar() {
        construirMenu();
    }

//    public PanelMenu(Navegador navegador) {
//
//        setLayout(new GridLayout(6, 1, 10, 10));
//        setBorder(BorderFactory.createEmptyBorder(40, 80, 40, 80));
//
//        add(new BotonPrincipal("Catálogo", 200, 50, e ->
//                navegador.irA("catalogo")));
//
//        if (!Sesion.esInvitado()) {
//            add(new BotonPrincipal("Biblioteca", 200, 50, e ->
//                    navegador.irA("biblioteca")));
//        }
//
//        if (!Sesion.esInvitado()) {
//            add(new BotonPrincipal("Perfil", 200, 50, e ->
//                    navegador.irA("perfil")));
//        }
//        if (!Sesion.esInvitado()) {
//            add(new BotonPrincipal("Compras", 200, 50, e ->
//                    navegador.irA("compras")));
//        }
//
//        add(new BotonPrincipal("Reportes", 200, 50, e ->
//                navegador.irA("reportes")));
//
//        add(new BotonPeligro("Salir", 200, 50, e ->
//                System.exit(0)));
//    }
}
