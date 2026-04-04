package org.davidparada.repositorio.implementacionHibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.davidparada.modelo.entidad.UsuarioEntidad;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.modelo.mapper.UsuarioFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IUsuarioRepo;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class UsuarioRepoHibernate implements IUsuarioRepo {

    private final ISessionManager sessionManager;

    public UsuarioRepoHibernate(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public UsuarioEntidad crear(UsuarioForm formulario) {
        Session session = sessionManager.getSession();

        UsuarioEntidad usuario = UsuarioFormularioAEntidadMapper.crearUsuarioEntidad(formulario);
        session.persist(usuario);

        return usuario;
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorId(Long idUsuario) {
        Session session = sessionManager.getSession();

        return Optional.ofNullable(session.find(UsuarioEntidad.class, idUsuario));
    }

    @Override
    public List<UsuarioEntidad> listarTodos() {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteria = session.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntidad> criteriaQuery = criteria.createQuery(UsuarioEntidad.class);
        Root<UsuarioEntidad> root = criteriaQuery.from(UsuarioEntidad.class);

        criteriaQuery.select(root);
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<UsuarioEntidad> actualizar(Long idUsuario, UsuarioForm formulario) {
        Session session = sessionManager.getSession();

        Optional<UsuarioEntidad> usuarioEntidad = this.buscarPorId(idUsuario);
        if (usuarioEntidad.isEmpty()) {
            return Optional.empty();
        }

        session.merge(new UsuarioEntidad(idUsuario,
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
        ));

        return buscarPorId(idUsuario);
    }

    @Override
    public boolean eliminar(Long idUsuario) {
        Session session = sessionManager.getSession();

        Optional<UsuarioEntidad> usuarioEntidad = this.buscarPorId(idUsuario);
        if (usuarioEntidad.isEmpty()) {
            return false;
        }
        session.remove(usuarioEntidad.get());
        return true;
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorEmail(String email) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteria = session.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntidad> criteriaQuery = criteria.createQuery(UsuarioEntidad.class);
        Root<UsuarioEntidad> root = criteriaQuery.from(UsuarioEntidad.class);
        criteriaQuery.select(root).where(criteria.equal(root.get("email"), email));

        return session.createQuery(criteriaQuery).getResultStream().findFirst();
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorNombreUsuario(String nombreUsuario) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteria = session.getCriteriaBuilder();
        CriteriaQuery<UsuarioEntidad> criteriaQuery = criteria.createQuery(UsuarioEntidad.class);
        Root<UsuarioEntidad> root = criteriaQuery.from(UsuarioEntidad.class);
        criteriaQuery.select(root).where(criteria.equal(root.get("nombreUsuario"), nombreUsuario));

        return session.createQuery(criteriaQuery).getResultStream().findFirst();
    }
}