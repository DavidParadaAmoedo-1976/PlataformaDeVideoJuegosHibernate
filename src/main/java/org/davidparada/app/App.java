package org.davidparada.app;

import org.davidparada.config.DatosPrueba;
import org.davidparada.controlador.*;
import org.davidparada.controlador.interfaceControlador.*;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.repositorio.implementacionHibernate.*;
import org.davidparada.repositorio.interfaceRepositorio.*;
import org.davidparada.transaciones.GestorTransaccionesHibernate;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;

import javax.swing.*;

public class App {

    public static void iniciar() throws ValidationException {
        // transacciones
        IGestorTransacciones gestor = new GestorTransaccionesHibernate();
        ISessionManager sessionManager = (ISessionManager) gestor;

        // repositorios
        ICompraRepo compraRepo = new CompraRepoHibernate(sessionManager);
        IUsuarioRepo usuarioRepo = new UsuarioRepoHibernate(sessionManager);
        IBibliotecaRepo bibliotecaRepo = new BibliotecaRepoHibernate(sessionManager);
        IResenaRepo resenaRepo = new ResenaRepoHibernate(sessionManager);
        IJuegoRepo juegoRepo = new JuegoRepoHibernate(sessionManager);

        ObtenerEntidadesOptional obtener =
                new ObtenerEntidadesOptional(compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, resenaRepo);

        // controladores
        IUsuarioControlador usuarioControlador = new UsuarioControlador(usuarioRepo, obtener, gestor);
        IJuegoControlador juegoControlador = new JuegoControlador(juegoRepo, obtener, gestor);

        IBibliotecaControlador bibliotecaControlador =
                new BibliotecaControlador(bibliotecaRepo, juegoRepo, obtener, gestor);

        ICompraControlador compraControlador =
                new CompraControlador(compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, bibliotecaControlador, obtener, gestor);

        IResenaControlador resenaControlador =
                new ResenaControlador(resenaRepo, obtener, gestor);

        // contexto
        AppContext ctx = new AppContext(
                usuarioControlador,
                juegoControlador,
                bibliotecaControlador,
                compraControlador,
                resenaControlador
        );

        // datos prueba
        DatosPrueba.cargarDatos(
                ctx.usuario(),
                ctx.juegos(),
                ctx.biblioteca(),
                ctx.compras(),
                ctx.resenas()
        );

        SwingUtilities.invokeLater(() -> {
            try {
                UI.iniciar(ctx);
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });
    }
}