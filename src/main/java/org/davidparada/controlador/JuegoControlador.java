package org.davidparada.controlador;

import org.davidparada.controlador.interfaceControlador.IJuegoControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.JuegoDto;
import org.davidparada.modelo.entidad.JuegoEntidad;
import org.davidparada.modelo.enums.ClasificacionJuegoEnum;
import org.davidparada.modelo.enums.EstadoJuegoEnum;
import org.davidparada.modelo.enums.OrdenarJuegosEnum;
import org.davidparada.modelo.enums.TipoErrorEnum;
import org.davidparada.modelo.formulario.JuegoForm;
import org.davidparada.modelo.formulario.validacion.ErrorModel;
import org.davidparada.modelo.formulario.validacion.JuegoFormValidador;
import org.davidparada.modelo.mapper.JuegoEntidadADtoMapper;
import org.davidparada.repositorio.interfaceRepositorio.IJuegoRepo;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.davidparada.controlador.util.ComprobarErrores.comprobarListaErrores;

public class JuegoControlador implements IJuegoControlador {

    public static final int DESCUENTO_MINIMO = 0;
    public static final int DESCUENTO_MAXIMO = 100;
    private final IJuegoRepo juegoRepo;
    private final ObtenerEntidadesOptional obtenerEntidades;
    private final IGestorTransacciones gestorTransacciones;

    public JuegoControlador(IJuegoRepo juegoRepo, ObtenerEntidadesOptional obtenerEntidades, IGestorTransacciones gestorTransacciones) {
        this.juegoRepo = juegoRepo;
        this.obtenerEntidades = obtenerEntidades;
        this.gestorTransacciones = gestorTransacciones;
    }

    // Anadir Juego
    @Override
    public JuegoDto crearJuego(JuegoForm formulario) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        JuegoFormValidador.validarJuego(formulario);

        JuegoEntidad juegoCreado = gestorTransacciones.inTransaction(() -> {
            if (juegoRepo.buscarPorTitulo(formulario.getTitulo()).isPresent()) {
                errores.add(new ErrorModel("titulo", TipoErrorEnum.DUPLICADO));
            }
            comprobarListaErrores(errores);

            return juegoRepo.crear(formulario);
        });

        return JuegoEntidadADtoMapper.juegoEntidadADto(juegoCreado);
    }

    // Buscar juegos
    @Override
    public List<JuegoDto> buscarJuegos(
            String titulo,
            String categoria,
            Double precioMin,
            Double precioMax,
            ClasificacionJuegoEnum clasificacion,
            EstadoJuegoEnum estado
    ) throws ValidationException {

        return gestorTransacciones.inTransaction(() -> {

            List<JuegoEntidad> juegos = juegoRepo.buscarConFiltros(
                    titulo, categoria, precioMin, precioMax, clasificacion, estado
            );

            return juegos.stream()
                    .map(juego -> JuegoEntidadADtoMapper.juegoEntidadADto(juego))
                    .toList();
        });
    }

    // Consultar catalogo completo
    @Override
    public List<JuegoDto> consultarCatalogo(OrdenarJuegosEnum orden) throws ValidationException {

        return gestorTransacciones.inTransaction(() -> {

            List<JuegoEntidad> juegos = juegoRepo.listarTodos();

            if (orden != null) {

                switch (orden) {
                    case ALFABETICO -> juegos.sort(Comparator.comparing(j -> j.getTitulo()));
                    case PRECIO -> juegos.sort(Comparator.comparing(j -> j.getPrecioBase()));
                    case FECHA -> juegos.sort(Comparator.comparing(j -> j.getFechaLanzamiento()));
                    default -> throw new IllegalArgumentException("No se encontro el tipo de búsqueda");
                }
            }

            return juegos.stream()
                    .map(j -> JuegoEntidadADtoMapper.juegoEntidadADto(j))
                    .toList();
        });
    }

    // Consultar detalles de un juego
    @Override
    public JuegoDto consultarDetalles(Long idJuego) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idJuego == null) {
            errores.add(new ErrorModel("id", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            JuegoEntidad juego = obtenerEntidades.obtenerJuego(idJuego, errores);

            return JuegoEntidadADtoMapper.juegoEntidadADto(juego);
        });
    }

    // Aplicar descuento
    @Override
    public JuegoDto aplicarDescuento(Long idJuego, Integer descuento) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idJuego == null)
            errores.add(new ErrorModel("id", TipoErrorEnum.OBLIGATORIO));
        comprobarListaErrores(errores);

        if (descuento == null) {
            errores.add(new ErrorModel("descuento", TipoErrorEnum.OBLIGATORIO));
        } else if (descuento < DESCUENTO_MINIMO || descuento > DESCUENTO_MAXIMO) {
            errores.add(new ErrorModel("descuento", TipoErrorEnum.RANGO_INVALIDO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            JuegoEntidad juego = obtenerEntidades.obtenerJuego(idJuego, errores);

            juegoRepo.actualizar(juego.getIdJuego(),
                    new JuegoForm(juego.getTitulo(),
                            juego.getDescripcion(),
                            juego.getDesarrollador(),
                            juego.getFechaLanzamiento(),
                            juego.getPrecioBase(),
                            descuento,
                            juego.getCategoria(),
                            juego.getClasificacionPorEdad(),
                            juego.getIdiomas(),
                            juego.getEstado()
                    ));
            return JuegoEntidadADtoMapper.juegoEntidadADto(juego);
        });
    }

    // Cambiar estado del juego
    @Override
    public JuegoDto cambiarEstado(Long idJuego, EstadoJuegoEnum nuevoEstado) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idJuego == null) {
            errores.add(new ErrorModel("id", TipoErrorEnum.OBLIGATORIO));
        }
        if (nuevoEstado == null) {
            errores.add(new ErrorModel("estado", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            JuegoEntidad juego = obtenerEntidades.obtenerJuego(idJuego, errores);

            juegoRepo.actualizar(juego.getIdJuego(),
                    new JuegoForm(
                            juego.getTitulo(),
                            juego.getDescripcion(),
                            juego.getDesarrollador(),
                            juego.getFechaLanzamiento(),
                            juego.getPrecioBase(),
                            juego.getDescuento(),
                            juego.getCategoria(),
                            juego.getClasificacionPorEdad(),
                            juego.getIdiomas(),
                            nuevoEstado
                    ));
            return JuegoEntidadADtoMapper.juegoEntidadADto(juego);
        });
    }

    // Eliminar el juego

    // Método no aparece en la gestion de juego.
    // Se deja comentado por si hace falta en el futuro.
//
//    @Override
//    public boolean eliminar(Long id) throws ValidationException {
//        List<ErrorModel> errores = new ArrayList<>();
//        if (id == null) {
//            errores.add(new ErrorModel("id", TipoErrorEnum.OBLIGATORIO));
//        }
//        obtenerEntidades.obtenerJuego(id, errores);
//
//        return juegoRepo.eliminar(id);
//    }
}
