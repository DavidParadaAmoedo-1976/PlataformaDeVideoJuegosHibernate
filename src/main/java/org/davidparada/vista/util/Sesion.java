package org.davidparada.vista.util;

import org.davidparada.modelo.dto.UsuarioDto;

public class Sesion {

    private static UsuarioDto usuarioActual;
    private static boolean invitado = false;

    public static void iniciarSesion(UsuarioDto usuario) {
        usuarioActual = usuario;
        invitado = false;
    }

    public static void iniciarComoInvitado() {
        usuarioActual = null;
        invitado = true;
    }

    public static UsuarioDto getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean esInvitado() {
        return invitado;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
        invitado = false;
    }
}
