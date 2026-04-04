package org.davidparada.repositorio.implementacionHibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.davidparada.modelo.entidad.CompraEntidad;
import org.davidparada.modelo.formulario.CompraForm;
import org.davidparada.modelo.mapper.CompraFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.ICompraRepo;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class CompraRepoHibernate implements ICompraRepo {

    private final ISessionManager sessionManager;

    public CompraRepoHibernate(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public CompraEntidad crear(CompraForm formulario) {
        Session session = sessionManager.getSession();

        CompraEntidad compraEntidad = CompraFormularioAEntidadMapper.crearCompraEntidad(formulario);
        session.persist(compraEntidad);

        return compraEntidad;
    }

    @Override
    public Optional<CompraEntidad> buscarPorId(Long idCompra) {
        Session session = sessionManager.getSession();

        return Optional.ofNullable(session.find(CompraEntidad.class, idCompra));
    }

    @Override
    public List<CompraEntidad> listarTodos() {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CompraEntidad> criteriaQuery = criteriaBuilder.createQuery(CompraEntidad.class);
        Root<CompraEntidad> root = criteriaQuery.from(CompraEntidad.class);

        criteriaQuery.select(root);

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<CompraEntidad> actualizar(Long idCompra, CompraForm formulario) {
        Session session = sessionManager.getSession();

        Optional<CompraEntidad> compraEntidad = this.buscarPorId(idCompra);
        if (compraEntidad.isEmpty()) {
            return Optional.empty();
        }
        session.merge(new CompraEntidad(idCompra,
                formulario.getIdUsuario(),
                formulario.getIdJuego(),
                formulario.getFechaCompra(),
                formulario.getMetodoPago(),
                formulario.getPrecioBase(),
                formulario.getDescuento(),
                formulario.getEstadoCompra()
        ));

        return buscarPorId(idCompra);
    }

    @Override
    public boolean eliminar(Long idCompra) {
        Session session = sessionManager.getSession();

        Optional<CompraEntidad> compraEntidad = this.buscarPorId(idCompra);
        if (compraEntidad.isEmpty()) {
            return false;
        }
        session.remove(compraEntidad.get());
        return true;
    }

    @Override
    public List<CompraEntidad> buscarPorUsuario(Long idUsuario) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CompraEntidad> criteriaQuery = criteriaBuilder.createQuery(CompraEntidad.class);
        Root<CompraEntidad> root = criteriaQuery.from(CompraEntidad.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("idUsuario"), idUsuario));

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<CompraEntidad> buscarPorCompraYUsuario(Long idCompra, Long idUsuario) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CompraEntidad> criteriaQuery = criteriaBuilder.createQuery(CompraEntidad.class);
        Root<CompraEntidad> root = criteriaQuery.from(CompraEntidad.class);

        Predicate p1 = criteriaBuilder.equal(root.get("idCompra"), idCompra);
        Predicate p2 = criteriaBuilder.equal(root.get("idUsuario"), idUsuario);
        criteriaQuery.select(root).where(criteriaBuilder.and(p1, p2));
        ;

        return session.createQuery(criteriaQuery).getResultStream().findFirst();
    }
}