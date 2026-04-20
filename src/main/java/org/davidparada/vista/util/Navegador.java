package org.davidparada.vista.util;

import org.davidparada.vista.principal.VentanaPrincipal;

public class Navegador {

    private VentanaPrincipal ventana;

    public Navegador(VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    public void irA(String panel) {
        ventana.mostrar(panel);
    }
}
