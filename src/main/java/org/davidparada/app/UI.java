package org.davidparada.app;

import org.davidparada.excepcion.ValidationException;
import org.davidparada.vista.panel.*;
import org.davidparada.vista.principal.VentanaPrincipal;
import org.davidparada.vista.util.Navegador;

public class UI {

    public static void iniciar(AppContext ctx) throws ValidationException {

        VentanaPrincipal ventana = new VentanaPrincipal();
        Navegador nav = new Navegador(ventana);

        PanelDetalleJuego panelDetalle = new PanelDetalleJuego(nav);

        PanelCatalogo catalogo = new PanelCatalogo(ctx.juegos(), nav, panelDetalle);

        PanelMenu panelMenu = new PanelMenu(nav);

        PanelInicio panelInicio = new PanelInicio(nav, panelMenu);

        PanelLogin panelLogin =
                new PanelLogin(nav, panelMenu, ctx.usuario());

        PanelRegistro registro =
                new PanelRegistro(nav, ctx.usuario());

        ventana.agregarPanel("inicio", panelInicio);
        ventana.agregarPanel("login", panelLogin);
        ventana.agregarPanel("menu", panelMenu);
        ventana.agregarPanel("catalogo", catalogo);
        ventana.agregarPanel("registro", registro);
        ventana.agregarPanel("detalleJuego", panelDetalle);

        ventana.mostrar("inicio");
        ventana.setVisible(true);
    }
}
