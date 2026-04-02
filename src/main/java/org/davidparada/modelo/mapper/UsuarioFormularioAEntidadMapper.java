package org.davidparada.modelo.mapper;

import org.davidparada.modelo.entidad.UsuarioEntidad;
import org.davidparada.modelo.enums.EstadoCuentaEnum;
import org.davidparada.modelo.formulario.UsuarioForm;

import java.time.Instant;

public class UsuarioFormularioAEntidadMapper {

    public static final double SALDO_POR_DEFECTO = 0.0;

    private UsuarioFormularioAEntidadMapper() {
    }

    public static UsuarioEntidad crearUsuarioEntidad(Long id, UsuarioForm formulario) {
        return new UsuarioEntidad(
                id,
                formulario.getNombreUsuario(),
                formulario.getEmail(),
                formulario.getPassword(),
                formulario.getNombreReal(),
                formulario.getPais(),
                formulario.getFechaNacimiento(),
                Instant.now(),
                formulario.getAvatar(),
                SALDO_POR_DEFECTO,
                EstadoCuentaEnum.ACTIVA
        );
    }

    public static UsuarioEntidad crearUsuarioEntidad(UsuarioForm formulario) {
        return crearUsuarioEntidad(null, formulario);
    }

    public static UsuarioEntidad actualizarUsuarioEntidad(Long id, UsuarioForm formulario) {

        return new UsuarioEntidad(
                id,
                formulario.getNombreUsuario(),
                formulario.getEmail(),
                formulario.getPassword(),
                formulario.getNombreReal(),
                formulario.getPais(),
                formulario.getFechaNacimiento(),
                formulario.getFechaRegistro(),
                formulario.getAvatar(),
                formulario.getSaldo(),
                formulario.getEstadoCuenta()
        );
    }
}

