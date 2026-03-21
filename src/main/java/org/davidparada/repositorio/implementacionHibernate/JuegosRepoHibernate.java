package org.davidparada.repositorio.implementacionHibernate;

import org.davidparada.modelo.entidad.JuegoEntidad;
import org.davidparada.modelo.enums.ClasificacionJuegoEnum;
import org.davidparada.modelo.enums.EstadoJuegoEnum;
import org.davidparada.modelo.formulario.JuegoForm;
import org.davidparada.modelo.mapper.JuegoFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IJuegoRepo;
import org.davidparada.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class JuegosRepoHibernate implements IJuegoRepo {
    @Override
    public boolean existeTitulo(String titulo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Long contador = session.createQuery
                            ("SELECT COUNT(j) FROM JuegoEntidad j WHERE LOWER(j.titulo) = LOWER(:titulo)",
                                    Long.class
                            )
                    .setParameter("titulo", titulo)
                    .uniqueResult();

            return contador != null && contador > 0;
        }
    }

    @Override
    public List<JuegoEntidad> buscarConFiltros(String titulo, String categoria, Double precioMin, Double precioMax, ClasificacionJuegoEnum clasificacion, EstadoJuegoEnum estado) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return List.of();
    }

    @Override
    public JuegoEntidad crear(JuegoForm formulario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        JuegoEntidad juego = JuegoFormularioAEntidadMapper.crearJuegoEntidad(null, formulario);

        session.persist(juego);
        tx.commit();
        session.close();

        return juego;
    }

    @Override
    public Optional<JuegoEntidad> buscarPorId(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();

        JuegoEntidad juego = session.get(JuegoEntidad.class, id);

        session.close();

        return Optional.ofNullable(juego);
    }

    @Override
    public List<JuegoEntidad> listarTodos() {
        Session session = HibernateUtil.getSessionFactory().openSession();

        List<JuegoEntidad> listaJuegos = session.createQuery("FROM JuegoEntidad", JuegoEntidad.class).list();

        session.close();

        return listaJuegos;
    }

    @Override
    public Optional<JuegoEntidad> actualizar(Long id, JuegoForm formulario) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        JuegoEntidad juegoEntidad = session.get(JuegoEntidad.class, id);
        if (juegoEntidad != null) {
            session.close();
            return Optional.empty();
        }
        JuegoEntidad juegoAcualizado = JuegoFormularioAEntidadMapper.actualizarJuegoEntidad(id, formulario);
        session.merge(juegoAcualizado);

        tx.commit();
        session.close();

        return Optional.of(juegoAcualizado);
    }

    @Override
    public boolean eliminar(Long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        JuegoEntidad juegoEntidad = session.get(JuegoEntidad.class, id);

        if (juegoEntidad == null) {
            session.close();
            return false;
        }
        session.remove(juegoEntidad);

        tx.commit();
        session.close();

        return true;
    }
}
