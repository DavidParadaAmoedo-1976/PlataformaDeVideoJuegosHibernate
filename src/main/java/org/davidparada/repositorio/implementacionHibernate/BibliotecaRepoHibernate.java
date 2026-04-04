package org.davidparada.repositorio.implementacionHibernate;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.davidparada.modelo.entidad.BibliotecaEntidad;
import org.davidparada.modelo.formulario.BibliotecaForm;
import org.davidparada.modelo.mapper.BibliotecaFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IBibliotecaRepo;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

public class BibliotecaRepoHibernate implements IBibliotecaRepo {

    private final ISessionManager sessionManager;

    public BibliotecaRepoHibernate(ISessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public BibliotecaEntidad crear(BibliotecaForm formulario) {
        Session session = sessionManager.getSession();

        BibliotecaEntidad bibliotecaEntidad = BibliotecaFormularioAEntidadMapper.crearBibliotecaEntidad(formulario);
        session.persist(bibliotecaEntidad);

        return bibliotecaEntidad;
    }

    @Override
    public Optional<BibliotecaEntidad> buscarPorId(Long idBiblioteca) {
        Session session = sessionManager.getSession();

        return Optional.ofNullable(session.find(BibliotecaEntidad.class, idBiblioteca));
    }

    @Override
    public List<BibliotecaEntidad> listarTodos() {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<BibliotecaEntidad> criteriaQuery = criteriaBuilder.createQuery(BibliotecaEntidad.class);
        Root<BibliotecaEntidad> root = criteriaQuery.from(BibliotecaEntidad.class);

        criteriaQuery.select(root);

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<BibliotecaEntidad> actualizar(Long idBiblioteca, BibliotecaForm formulario) {
        Session session = sessionManager.getSession();

        Optional<BibliotecaEntidad> bibliotecaEntidad = this.buscarPorId(idBiblioteca);
        if (bibliotecaEntidad.isEmpty()) {
            return Optional.empty();
        }
        session.merge(new BibliotecaEntidad(idBiblioteca,
                formulario.getIdUsuario(),
                formulario.getIdJuego(),
                formulario.getFechaAdquisicion(),
                formulario.getHorasDeJuego(),
                formulario.getUltimaFechaDeJuego(),
                formulario.isEstadoInstalacion()
        ));

        return buscarPorId(idBiblioteca);
    }

    @Override
    public boolean eliminar(Long idBiblioteca) {
        Session session = sessionManager.getSession();

        Optional<BibliotecaEntidad> bibliotecaEntidad = this.buscarPorId(idBiblioteca);
        if (bibliotecaEntidad.isEmpty()) {
            return false;
        }
        session.remove(bibliotecaEntidad.get());
        return true;
    }

    @Override
    public List<BibliotecaEntidad> buscarPorUsuario(Long idUsuario) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<BibliotecaEntidad> criteriaQuery = criteriaBuilder.createQuery(BibliotecaEntidad.class);
        Root<BibliotecaEntidad> root = criteriaQuery.from(BibliotecaEntidad.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("idUsuario"), idUsuario));

        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<BibliotecaEntidad> buscarPorUsuarioYJuego(Long idUsuario, Long idJuego) {
        Session session = sessionManager.getSession();

        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<BibliotecaEntidad> criteriaQuery = criteriaBuilder.createQuery(BibliotecaEntidad.class);
        Root<BibliotecaEntidad> root = criteriaQuery.from(BibliotecaEntidad.class);

        Predicate p1 = criteriaBuilder.equal(root.get("idUsuario"), idUsuario);
        Predicate p2 = criteriaBuilder.equal(root.get("idJuego"), idJuego);
        criteriaQuery.select(root).where(criteriaBuilder.and(p1, p2));

        return session.createQuery(criteriaQuery).getResultStream().findFirst();
    }
}