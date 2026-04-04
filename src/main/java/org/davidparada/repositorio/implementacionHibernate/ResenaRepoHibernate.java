package org.davidparada.repositorio.implementacionHibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.davidparada.modelo.entidad.ResenaEntidad;
import org.davidparada.modelo.formulario.ResenaForm;
import org.davidparada.modelo.mapper.ResenaFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IResenaRepo;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class ResenaRepoHibernate implements IResenaRepo {

    private final ISessionManager sessionManager;

    public ResenaRepoHibernate(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public ResenaEntidad crear(ResenaForm formulario) {
        Session session = sessionManager.getSession();

        ResenaEntidad resenaEntidad = ResenaFormularioAEntidadMapper.crearReseniaEntidad(formulario);
        session.persist(resenaEntidad);

        return resenaEntidad;
    }

    @Override
    public Optional<ResenaEntidad> buscarPorId(Long idResena) {
        Session session = sessionManager.getSession();

        return Optional.ofNullable(session.find(ResenaEntidad.class, idResena));
    }

    @Override
    public List<ResenaEntidad> listarTodos() {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ResenaEntidad> criteriaQuery = criteriaBuilder.createQuery(ResenaEntidad.class);
        Root<ResenaEntidad> root = criteriaQuery.from(ResenaEntidad.class);

        criteriaQuery.select(root);

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<ResenaEntidad> actualizar(Long idResena, ResenaForm formulario) {
        Session session = sessionManager.getSession();

        Optional<ResenaEntidad> resenaEntidad = this.buscarPorId(idResena);
        if (resenaEntidad.isEmpty()) {
            return Optional.empty();
        }

        session.merge(new ResenaEntidad(idResena,
                formulario.getIdUsuario(),
                formulario.getIdJuego(),
                formulario.isRecomendado(),
                formulario.getTextoResena(),
                formulario.getCantidadHorasJugadas(),
                formulario.getFechaPublicacion(),
                formulario.getFechaUltimaEdicion(),
                formulario.getEstadoPublicacion()
        ));
        return buscarPorId(idResena);
    }

    @Override
    public boolean eliminar(Long idResena) {
        Session session = sessionManager.getSession();

        Optional<ResenaEntidad> resenaEntidad = this.buscarPorId(idResena);
        if (resenaEntidad.isEmpty()) {
            return false;
        }
        session.remove(resenaEntidad.get());
        return true;
    }

    @Override
    public List<ResenaEntidad> buscarPorUsuario(Long idUsuario) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ResenaEntidad> criteriaQuery = criteriaBuilder.createQuery(ResenaEntidad.class);
        Root<ResenaEntidad> root = criteriaQuery.from(ResenaEntidad.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("idUsuario"), idUsuario));

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<ResenaEntidad> buscarPorJuego(Long idJuego) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ResenaEntidad> criteriaQuery = criteriaBuilder.createQuery(ResenaEntidad.class);
        Root<ResenaEntidad> root = criteriaQuery.from(ResenaEntidad.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("idJuego"), idJuego));

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<ResenaEntidad> buscarPorIdYUsuario(Long idResena, Long idUsuario) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<ResenaEntidad> criteriaQuery = criteriaBuilder.createQuery(ResenaEntidad.class);
        Root<ResenaEntidad> root = criteriaQuery.from(ResenaEntidad.class);

        Predicate p1 = criteriaBuilder.equal(root.get("idResena"), idResena);
        Predicate p2 = criteriaBuilder.equal(root.get("idUsuario"), idUsuario);
        criteriaQuery.select(root).where(criteriaBuilder.and(p1, p2));

        return session.createQuery(criteriaQuery).getResultStream().findFirst();
    }

}
