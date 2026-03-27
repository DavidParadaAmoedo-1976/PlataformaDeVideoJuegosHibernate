package org.davidparada.controlador.util;

import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.entidad.*;
import org.davidparada.modelo.enums.TipoErrorEnum;
import org.davidparada.modelo.formulario.validacion.ErrorModel;
import org.davidparada.repositorio.interfaceRepositorio.*;

import java.util.List;

public class ObtenerEntidadesOptional {

    private static ICompraRepo compraRepo;
    private static IUsuarioRepo usuarioRepo;
    private static IJuegoRepo juegoRepo;
    private static IBibliotecaRepo bibliotecaRepo;
    private static IResenaRepo resenaRepo;

    public ObtenerEntidadesOptional(ICompraRepo compraRepo,
                                    IUsuarioRepo usuarioRepo,
                                    IJuegoRepo juegoRepo,
                                    IBibliotecaRepo bibliotecaRepo,
                                    IResenaRepo resenaRepo) {
        this.compraRepo = compraRepo;
        this.usuarioRepo = usuarioRepo;
        this.juegoRepo = juegoRepo;
        this.bibliotecaRepo = bibliotecaRepo;
        this.resenaRepo = resenaRepo;
    }

    public CompraEntidad obtenerCompra(Long idCompra, List<ErrorModel> errores) throws ValidationException {
        return compraRepo.buscarPorId(idCompra)
                .orElseThrow(() -> {
                    errores.add(new ErrorModel("compra", TipoErrorEnum.NO_ENCONTRADO));
                    return new ValidationException(errores);
                });
    }

    public CompraEntidad obtenerCompraUsuario(Long idCompra, Long idUsuario, List<ErrorModel> errores) throws ValidationException {
        return compraRepo.buscarPorCompraYUsuario(idCompra, idUsuario)
                .orElseThrow(() -> {
                    errores.add(new ErrorModel("compra", TipoErrorEnum.NO_ENCONTRADO));
                    return new ValidationException(errores);
                });
    }

    public UsuarioEntidad obtenerUsuario(Long idUsuario, List<ErrorModel> errores) throws ValidationException {
        return usuarioRepo.buscarPorId(idUsuario)
                .orElseThrow(() -> {
                    errores.add(new ErrorModel("usuario", TipoErrorEnum.NO_ENCONTRADO));
                    return new ValidationException(errores);
                });
    }

    public JuegoEntidad obtenerJuego(Long idJuego, List<ErrorModel> errores) throws ValidationException {
        return juegoRepo.buscarPorId(idJuego)
                .orElseThrow(() -> {
                    errores.add(new ErrorModel("juego", TipoErrorEnum.NO_ENCONTRADO));
                    return new ValidationException(errores);
                });
    }

    public BibliotecaEntidad obtenerBiblioteca(Long idUsuario, Long idJuego, List<ErrorModel> errores) throws ValidationException {
        return bibliotecaRepo.buscarPorUsuarioYJuego(idUsuario, idJuego)
                .orElseThrow(() -> {
                    errores.add(new ErrorModel("biblioteca", TipoErrorEnum.NO_ENCONTRADO));
                    return new ValidationException(errores);
                });
    }

    public ResenaEntidad obtenerResena(Long idResena, List<ErrorModel> errores) throws ValidationException {
        return resenaRepo.buscarPorId(idResena).orElseThrow(() -> {
            errores.add(new ErrorModel("resena", TipoErrorEnum.NO_ENCONTRADO));
            return new ValidationException(errores);
        });
    }

    public ResenaEntidad obtenerResenaPorIdYUsuario(Long idResena, Long idUsuario, List<ErrorModel> errores) throws ValidationException {
        return resenaRepo.buscarPorIdYUsuario(idResena, idUsuario).orElseThrow(() -> {
            errores.add(new ErrorModel("resena", TipoErrorEnum.NO_ENCONTRADO));
            return new ValidationException(errores);
        });
    }
}
