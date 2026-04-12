package org.davidparada.controlador;

import org.davidparada.controlador.interfaceControlador.IUsuarioControlador;
import org.davidparada.controlador.util.ObtenerEntidadesOptional;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.UsuarioDto;
import org.davidparada.modelo.entidad.UsuarioEntidad;
import org.davidparada.modelo.enums.EstadoCuentaEnum;
import org.davidparada.modelo.enums.TipoErrorEnum;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.modelo.formulario.validacion.ErrorModel;
import org.davidparada.modelo.formulario.validacion.UsuarioFormValidador;
import org.davidparada.modelo.mapper.UsuarioEntidadADtoMapper;
import org.davidparada.repositorio.interfaceRepositorio.IUsuarioRepo;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;
import org.davidparada.util.EncriptarPassword;

import java.util.ArrayList;
import java.util.List;

import static org.davidparada.controlador.util.ComprobarErrores.comprobarListaErrores;

public class UsuarioControlador implements IUsuarioControlador {

    public static final double SALDO_MIN_A_ANADIR = 5.0;
    public static final double SALDO_MAX_A_ANADIR = 500.0;
    public static final int CERO = 0;
    private final IUsuarioRepo usuarioRepo;
    private final ObtenerEntidadesOptional obtenerEntidades;
    private final IGestorTransacciones gestorTransacciones;

    public UsuarioControlador(IUsuarioRepo usuarioRepo, ObtenerEntidadesOptional obtenerEntidades, IGestorTransacciones gestorTransacciones) {
        this.usuarioRepo = usuarioRepo;
        this.obtenerEntidades = obtenerEntidades;
        this.gestorTransacciones = gestorTransacciones;
    }

    @Override
    public UsuarioDto registrarUsuario(UsuarioForm formulario) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        UsuarioFormValidador.validarUsuario(formulario);

        return gestorTransacciones.inTransaction(() -> {
            if (usuarioRepo.buscarPorEmail(formulario.getEmail()).isPresent()) {
                errores.add(new ErrorModel("email", TipoErrorEnum.DUPLICADO));
            }
            if (usuarioRepo.buscarPorNombreUsuario(formulario.getNombreUsuario()).isPresent()) {
                errores.add(new ErrorModel("nombreUsuario", TipoErrorEnum.DUPLICADO));
            }
            comprobarListaErrores(errores);

            String passwordHash = EncriptarPassword.generarHash(formulario.getPassword());

            UsuarioForm formularioHash = new UsuarioForm(
                    formulario.getNombreUsuario(),
                    formulario.getEmail(),
                    passwordHash,
                    formulario.getNombreReal(),
                    formulario.getPais(),
                    formulario.getFechaNacimiento(),
                    formulario.getFechaRegistro(),
                    formulario.getAvatar(),
                    formulario.getSaldo(),
                    formulario.getEstadoCuenta()
            );

            UsuarioEntidad usuario = usuarioRepo.crear(formularioHash);

            return UsuarioEntidadADtoMapper.usuarioEntidadADto(usuario);
        });
    }


    @Override
    public UsuarioDto consultarPerfil(Long idUsuario) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idUsuario == null) {
            errores.add(new ErrorModel("id", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            UsuarioEntidad usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);

            return UsuarioEntidadADtoMapper.usuarioEntidadADto(usuario);

        });
    }

    @Override
    public UsuarioDto consultarPerfil(String nombreUsuario) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            errores.add(new ErrorModel("nombre", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {

            return usuarioRepo.buscarPorNombreUsuario(nombreUsuario)
                    .map(usuarioEntidad -> UsuarioEntidadADtoMapper.usuarioEntidadADto(usuarioEntidad))
                    .orElseThrow(() -> new ValidationException(errores));

        });
    }

    @Override
    public UsuarioDto anadirSaldo(Long idUsuario, Double cantidad) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idUsuario == null) {
            errores.add(new ErrorModel("id", TipoErrorEnum.OBLIGATORIO));
        }
        if (cantidad == null) {
            errores.add(new ErrorModel("saldo", TipoErrorEnum.OBLIGATORIO));
        } else {
            if (cantidad <= CERO) {
                errores.add(new ErrorModel("saldo", TipoErrorEnum.VALOR_NEGATIVO));
            }
            if (cantidad < SALDO_MIN_A_ANADIR || cantidad > SALDO_MAX_A_ANADIR) {
                errores.add(new ErrorModel("saldo", TipoErrorEnum.RANGO_INVALIDO));
            }
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            UsuarioEntidad usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);
            if (usuario.getEstadoCuenta() != EstadoCuentaEnum.ACTIVA) {
                errores.add(new ErrorModel("estadoCuenta", TipoErrorEnum.ESTADO_INCORRECTO));
            }
            comprobarListaErrores(errores);

            double nuevoSaldo = usuario.getSaldo() + cantidad;
            usuarioRepo.actualizar(usuario.getIdUsuario(), new UsuarioForm(
                    usuario.getNombreUsuario(),
                    usuario.getEmail(),
                    usuario.getPassword(),
                    usuario.getNombreReal(),
                    usuario.getPais(),
                    usuario.getFechaNacimiento(),
                    usuario.getFechaRegistro(),
                    usuario.getAvatar(),
                    nuevoSaldo,
                    usuario.getEstadoCuenta()
            ));

            UsuarioEntidad usuarioActualizado = obtenerEntidades.obtenerUsuario(idUsuario, errores);

            return UsuarioEntidadADtoMapper.usuarioEntidadADto(usuarioActualizado);
        });
    }

    @Override
    public UsuarioDto consultarSaldo(Long idUsuario) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (idUsuario == null) {
            errores.add(new ErrorModel("id", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            UsuarioEntidad usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);

            return UsuarioEntidadADtoMapper.usuarioEntidadADto(usuario);
        });
    }

    @Override
    public UsuarioDto login(String nombreUsuario, String password) throws ValidationException {
        List<ErrorModel> errores = new ArrayList<>();
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            errores.add(new ErrorModel("nombreUsuario", TipoErrorEnum.OBLIGATORIO));
        }
        if (password == null || password.isBlank()) {
            errores.add(new ErrorModel("password", TipoErrorEnum.OBLIGATORIO));
        }
        comprobarListaErrores(errores);

        return gestorTransacciones.inTransaction(() -> {
            UsuarioEntidad usuario = usuarioRepo.buscarPorNombreUsuario(nombreUsuario)
                    .orElse(null);
            if (usuario == null) {
                errores.add(new ErrorModel("usuario", TipoErrorEnum.NO_ENCONTRADO));
            }
            if (!EncriptarPassword.verificarPassword(password, usuario.getPassword())) {
                errores.add(new ErrorModel("password", TipoErrorEnum.NO_COINCIDE));
            }
            comprobarListaErrores(errores);

            return UsuarioEntidadADtoMapper.usuarioEntidadADto(usuario);
        });
    }

    // Métodos creados pero se dejan documentados por que no se piden, pero pueden llegar a hacer falta.

//    public UsuarioDto cambiarEstado(Long idUsuario, EstadoCuentaEnum nuevoEstado) throws ValidationException {
//        List<ErrorModel> errores = new ArrayList<>();
//        if (idUsuario == null) {
//            errores.add(new ErrorModel("id", TipoErrorEnum.OBLIGATORIO));
//        }
//        if (nuevoEstado == null) {
//            errores.add(new ErrorModel("estadoCuenta", TipoErrorEnum.OBLIGATORIO));
//        }
//        comprobarListaErrores(errores);
//
//        return gestorTransacciones.inTransaction(() -> {
//                UsuarioEntidad usuario = obtenerEntidades.obtenerUsuario(idUsuario, errores);
//
//            UsuarioForm usuarioNuevoEstado = new UsuarioForm(
//                    usuario.getNombreUsuario(),
//                    usuario.getEmail(),
//                    usuario.getPassword(),
//                    usuario.getNombreReal(),
//                    usuario.getPais(),
//                    usuario.getFechaNacimiento(),
//                    usuario.getFechaRegistro(),
//                    usuario.getAvatar(),
//                    usuario.getSaldo(),
//                    nuevoEstado
//            );
//            UsuarioFormValidador.validarUsuario(usuarioNuevoEstado);
//
//                usuarioRepo.actualizar(usuario.getIdUsuario(),usuarioNuevoEstado);
//                UsuarioEntidad usuarioActualizado = obtenerEntidades.obtenerUsuario(idUsuario, errores);
//
//                return UsuarioEntidadADtoMapper.usuarioEntidadADto(usuarioActualizado);
//        });
//    }
//
//    public boolean eliminarUsuario(Long id) throws ValidationException {
//        List<ErrorModel> errores = new ArrayList<>();
//        if (id == null) {
//            errores.add(new ErrorModel("id", TipoErrorEnum.OBLIGATORIO));
//        }
//        comprobarListaErrores(errores);
//
//        return gestorTransacciones.inTransaction(() -> {
//                UsuarioEntidad usuario = obtenerEntidades.obtenerUsuario(id, errores);
//                return usuarioRepo.eliminar(usuario.getIdUsuario());
//        });
//    }
}
