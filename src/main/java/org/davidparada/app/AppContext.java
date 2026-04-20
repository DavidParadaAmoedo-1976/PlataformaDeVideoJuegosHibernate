package org.davidparada.app;

import org.davidparada.controlador.interfaceControlador.*;

public class AppContext {

    private final IUsuarioControlador usuarioControlador;
    private final IJuegoControlador juegoControlador;
    private final IBibliotecaControlador bibliotecaControlador;
    private final ICompraControlador compraControlador;
    private final IResenaControlador resenaControlador;

    public AppContext(
            IUsuarioControlador usuarioControlador,
            IJuegoControlador juegoControlador,
            IBibliotecaControlador bibliotecaControlador,
            ICompraControlador compraControlador,
            IResenaControlador resenaControlador
    ) {
        this.usuarioControlador = usuarioControlador;
        this.juegoControlador = juegoControlador;
        this.bibliotecaControlador = bibliotecaControlador;
        this.compraControlador = compraControlador;
        this.resenaControlador = resenaControlador;
    }

    public IUsuarioControlador usuario() {
        return usuarioControlador;
    }

    public IJuegoControlador juegos() {
        return juegoControlador;
    }

    public IBibliotecaControlador biblioteca() {
        return bibliotecaControlador;
    }

    public ICompraControlador compras() {
        return compraControlador;
    }

    public IResenaControlador resenas() {
        return resenaControlador;
    }
}
