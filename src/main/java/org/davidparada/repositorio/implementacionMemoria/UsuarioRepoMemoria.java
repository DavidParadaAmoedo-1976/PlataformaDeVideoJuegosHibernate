package org.davidparada.repositorio.implementacionMemoria;

import org.davidparada.modelo.entidad.UsuarioEntidad;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.modelo.mapper.UsuarioFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IUsuarioRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepoMemoria implements IUsuarioRepo {
    public static final long INICIALIZAR_CONTADOR_ID = 1L;
    private final List<UsuarioEntidad> usuariosEntidad = new ArrayList<>();
    private Long siguienteId = INICIALIZAR_CONTADOR_ID;

    private Long generarId() {
        return siguienteId++;
    }

    @Override
    public UsuarioEntidad crear(UsuarioForm form) {
        UsuarioEntidad usuario = UsuarioFormularioAEntidadMapper.crearUsuarioEntidad(generarId(), form);

        usuariosEntidad.add(usuario);
        return usuario;
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorId(Long idUsuario) {
        return usuariosEntidad.stream()
                .filter(u -> u.getIdUsuario().equals(idUsuario))
                .findFirst();
    }

    @Override
    public List<UsuarioEntidad> listarTodos() {
        return new ArrayList<>(usuariosEntidad);
    }

    @Override
    public Optional<UsuarioEntidad> actualizar(Long idUsuario, UsuarioForm form) {
        UsuarioEntidad usuario = buscarPorId(idUsuario).orElse(null);
        if (usuario == null) {
            return Optional.empty();
        }

        UsuarioEntidad nuevoUsuario = UsuarioFormularioAEntidadMapper.actualizarUsuarioEntidad(idUsuario, form);
        usuariosEntidad.remove(usuario);
        usuariosEntidad.add(nuevoUsuario);

        return Optional.of(nuevoUsuario);
    }

    @Override
    public boolean eliminar(Long idUsuario) {
        Optional<UsuarioEntidad> usuarioEntidad = buscarPorId(idUsuario);
        if (usuarioEntidad.isEmpty()) {
            return false;
        }
        return usuariosEntidad.remove(usuarioEntidad.get());
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorEmail(String email) {
        return usuariosEntidad.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorNombreUsuario(String nombreUsuario) {
        return usuariosEntidad.stream()
                .filter(u -> u.getNombreUsuario().equalsIgnoreCase(nombreUsuario))
                .findFirst();
    }

    @Override
    public void actualizarSaldo(Long idUsuario, Double nuevoSaldo) {
        List<UsuarioEntidad> usuarios = listarTodos();
        for (UsuarioEntidad usuario : usuarios) {
            if (usuario.getIdUsuario().equals(idUsuario)) {
                usuario.setSaldo(nuevoSaldo);
                return; // importante salir
            }
        }
    }
}
