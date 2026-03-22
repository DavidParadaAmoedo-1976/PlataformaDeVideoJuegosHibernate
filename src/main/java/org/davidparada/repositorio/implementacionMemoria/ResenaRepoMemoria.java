package org.davidparada.repositorio.implementacionMemoria;

import org.davidparada.modelo.entidad.ResenaEntidad;
import org.davidparada.modelo.formulario.ResenaForm;
import org.davidparada.modelo.mapper.ResenaFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IResenaRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResenaRepoMemoria implements IResenaRepo {
    public static final long INICIALIZAR_CONTADOR_ID = 1L;
    private final List<ResenaEntidad> reseniasEntidad = new ArrayList<>();
    private Long siguienteId = INICIALIZAR_CONTADOR_ID;

    private Long generarId() {
        return siguienteId++;
    }

    @Override
    public ResenaEntidad crear(ResenaForm form) {
        ResenaEntidad resenaEntidad = ResenaFormularioAEntidadMapper.crearReseniaEntidad(generarId(), form);
        reseniasEntidad.add(resenaEntidad);
        return resenaEntidad;
    }

    @Override
    public Optional<ResenaEntidad> buscarPorId(Long idResena) {
        return reseniasEntidad.stream()
                .filter(r -> r.getIdResena().equals(idResena))
                .findFirst();
    }

    @Override
    public List<ResenaEntidad> listarTodos() {
        return new ArrayList<>(reseniasEntidad);
    }

    @Override
    public Optional<ResenaEntidad> actualizar(Long idResena, ResenaForm form) {
        ResenaEntidad resenaEntidad = buscarPorId(idResena).orElse(null);
        if (resenaEntidad == null) {
            return Optional.empty();
        }
        ResenaEntidad nuevaResenia = ResenaFormularioAEntidadMapper.actualizarReseniaEntidad(idResena, form);
        reseniasEntidad.remove(resenaEntidad);
        reseniasEntidad.add(nuevaResenia);
        return Optional.of(nuevaResenia);
    }

    @Override
    public boolean eliminar(Long idResena) {
        Optional<ResenaEntidad> resenaEntidad = buscarPorId(idResena);
        return resenaEntidad.map(reseniasEntidad::remove).orElse(false);
    }

    @Override
    public List<ResenaEntidad> buscarPorUsuario(Long idUsuario) {
        return reseniasEntidad.stream()
                .filter(r -> r.getIdUsuario().equals(idUsuario))
                .toList();
    }

    @Override
    public List<ResenaEntidad> buscarPorJuego(Long idJuego) {
        return reseniasEntidad.stream()
                .filter(r -> r.getIdJuego().equals(idJuego))
                .toList();
    }

    @Override
    public Optional<ResenaEntidad> buscarPorIdYUsuario(Long idResena, Long idUsuario) {
        return reseniasEntidad.stream()
                .filter(r -> r.getIdResena().equals(idResena) && r.getIdUsuario().equals(idUsuario))
                .findFirst();
    }
}

