package org.davidparada;

import org.davidparada.config.DatosPrueba;
import org.davidparada.controlador.*;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.repositorio.implementacionHibernate.*;
import org.davidparada.repositorio.interfaceRepositorio.*;


public class Program {

    static void main(String[] args) {

        // repositorios
        IUsuarioRepo usuarioRepo = new UsuarioRepoHibernate();
        IJuegoRepo juegoRepo = new JuegosRepoHibernate();
        IBibliotecaRepo bibliotecaRepo = new BibliotecaRepoHibernate();
        ICompraRepo compraRepo = new CompraRepoHibernate();
        IResenaRepo resenaRepo = new ResenaRepoHibernate();

        // controladores
        ObtenerEntidadesOptional obtenerEntidades = new ObtenerEntidadesOptional(compraRepo, usuarioRepo,juegoRepo,bibliotecaRepo, resenaRepo);

        UsuarioControlador usuarioControlador = new UsuarioControlador(usuarioRepo);
        JuegoControlador juegoControlador = new JuegoControlador(juegoRepo);

        BibliotecaControlador bibliotecaControlador =
                new BibliotecaControlador(bibliotecaRepo, juegoRepo, obtenerEntidades);

        CompraControlador compraControlador =
                new CompraControlador(compraRepo, usuarioRepo, juegoRepo, bibliotecaRepo, bibliotecaControlador, obtenerEntidades);

        ResenaControlador resenaControlador =
                new ResenaControlador(resenaRepo, obtenerEntidades);

        // datos prueba

        DatosPrueba.cargarDatos(usuarioControlador, juegoControlador, bibliotecaControlador, compraControlador, resenaControlador);
    }
}

