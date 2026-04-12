package org.davidparada.repositorio.implementacionHibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.davidparada.modelo.entidad.JuegoEntidad;
import org.davidparada.modelo.enums.ClasificacionJuegoEnum;
import org.davidparada.modelo.enums.EstadoJuegoEnum;
import org.davidparada.modelo.formulario.JuegoForm;
import org.davidparada.modelo.mapper.JuegoFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IJuegoRepo;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JuegoRepoHibernate implements IJuegoRepo {

    private final ISessionManager sessionManager;

    public JuegoRepoHibernate(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public JuegoEntidad crear(JuegoForm formulario) {
        Session session = sessionManager.getSession();

        JuegoEntidad juegoEntidad = JuegoFormularioAEntidadMapper.crearJuegoEntidad(formulario);
        session.persist(juegoEntidad);

        return juegoEntidad;
    }

    @Override
    public Optional<JuegoEntidad> buscarPorId(Long idJuego) {
        Session session = sessionManager.getSession();

        return Optional.ofNullable(session.find(JuegoEntidad.class, idJuego));
    }

    @Override
    public List<JuegoEntidad> listarTodos() {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<JuegoEntidad> criteriaQuery = criteriaBuilder.createQuery(JuegoEntidad.class);
        Root<JuegoEntidad> root = criteriaQuery.from(JuegoEntidad.class);

        criteriaQuery.select(root);

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<JuegoEntidad> actualizar(Long idJuego, JuegoForm formulario) {
        Session session = sessionManager.getSession();

        Optional<JuegoEntidad> juegoEntidad = this.buscarPorId(idJuego);
        if (juegoEntidad.isEmpty()) {
            return Optional.empty();
        }

        session.merge(new JuegoEntidad(idJuego,
                formulario.getTitulo(),
                formulario.getDescripcion(),
                formulario.getDesarrollador(),
                formulario.getFechaLanzamiento(),
                formulario.getPrecioBase(),
                formulario.getDescuento(),
                formulario.getCategoria(),
                formulario.getClasificacionPorEdad(),
                formulario.getIdiomas(),
                formulario.getEstado()
        ));
        return buscarPorId(idJuego);
    }

    @Override
    public boolean eliminar(Long idJuego) {
        Session session = sessionManager.getSession();

        Optional<JuegoEntidad> juegoEntidad = this.buscarPorId(idJuego);
        if (juegoEntidad.isEmpty()) {
            return false;
        }
        session.remove(juegoEntidad.get());
        return true;
    }

    @Override
    public Optional<JuegoEntidad> buscarPorTitulo(String titulo) {

        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<JuegoEntidad> criteriaQuery = criteriaBuilder.createQuery(JuegoEntidad.class);
        Root<JuegoEntidad> root = criteriaQuery.from(JuegoEntidad.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("titulo"), titulo));

        return session.createQuery(criteriaQuery).getResultStream().findFirst();
    }

    @Override
    public List<JuegoEntidad> buscarConFiltros(String titulo, String categoria, Double precioMin, Double precioMax, ClasificacionJuegoEnum clasificacion, EstadoJuegoEnum estado) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteria = session.getCriteriaBuilder();
        CriteriaQuery<JuegoEntidad> criteriaQuery = criteria.createQuery(JuegoEntidad.class);
        Root<JuegoEntidad> root = criteriaQuery.from(JuegoEntidad.class);
        List<Predicate> filtros = new ArrayList<>();

        if (titulo != null && !titulo.isEmpty()) {
            filtros.add(criteria.like(root.get("titulo"), "%" + titulo + "%"));
        }
        if (categoria != null) {
            filtros.add(criteria.equal(root.get("categoria"), categoria));
        }
        if (precioMin != null) {
            filtros.add(criteria.greaterThanOrEqualTo(root.get("precio"), precioMin));
        }
        if (precioMax != null) {
            filtros.add(criteria.lessThanOrEqualTo(root.get("precio"), precioMax));
        }
        if (clasificacion != null) {
            filtros.add(criteria.equal(root.get("clasificacion"), clasificacion));
        }
        if (estado != null) {
            filtros.add(criteria.equal(root.get("estado"), estado));
        }
        criteriaQuery.where(filtros.toArray(new Predicate[0]));

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public boolean existeTitulo(String titulo) {
        Session session = sessionManager.getSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<JuegoEntidad> root = query.from(JuegoEntidad.class);

        query.select(cb.count(root))
                .where(cb.equal(root.get("titulo"), titulo));

        Long count = session.createQuery(query).getSingleResult();

        return count > 0;
    }
}
