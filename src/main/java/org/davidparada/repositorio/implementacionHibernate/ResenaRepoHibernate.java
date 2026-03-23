package org.davidparada.repositorio.implementacionHibernate;

import org.davidparada.modelo.entidad.ResenaEntidad;
import org.davidparada.modelo.formulario.ResenaForm;
import org.davidparada.modelo.mapper.ResenaFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IResenaRepo;
import org.davidparada.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class ResenaRepoHibernate implements IResenaRepo {
    @Override
    public List<ResenaEntidad> buscarPorUsuario(Long idUsuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM ResenaEntidad WHERE idUsuario = :idUsuario";
            List<ResenaEntidad> resenasEntidad = session
                    .createQuery(query, ResenaEntidad.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();

            tx.commit();
            return resenasEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar reseñas en base de datos", e);
        }
    }

    @Override
    public List<ResenaEntidad> buscarPorJuego(Long idJuego) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM ResenaEntidad WHERE Juego = :idJuego";
            List<ResenaEntidad> resenasEntidad = session
                    .createQuery(query, ResenaEntidad.class)
                    .setParameter("idJuego", idJuego)
                    .getResultList();

            tx.commit();
            return resenasEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar reseñas en base de datos", e);
        }
    }

    @Override
    public Optional<ResenaEntidad> buscarPorIdYUsuario(Long idResena, Long idUsuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            ResenaEntidad resenaEntidad = session.get(ResenaEntidad.class, idResena);

            if (resenaEntidad == null || !resenaEntidad.getIdUsuario().equals(idUsuario)) {
                tx.commit();
                return Optional.empty();
            }

            tx.commit();
            return Optional.of(resenaEntidad);

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar reseña en base de datos", e);
        }
    }

    @Override
    public ResenaEntidad crear(ResenaForm formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            ResenaEntidad resenaEntidad = ResenaFormularioAEntidadMapper.crearReseniaEntidad(formulario);
            session.persist(resenaEntidad);

            tx.commit();
            return resenaEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear reseña en base de datos", e);
        }
    }

    @Override
    public Optional<ResenaEntidad> buscarPorId(Long idResena) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Optional<ResenaEntidad> resenaEntidad = Optional.ofNullable(session.get(ResenaEntidad.class, idResena));

            tx.commit();
            return resenaEntidad;

        } catch (
                Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar reseña en base de datos", e);
        }
    }

    @Override
    public List<ResenaEntidad> listarTodos() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM ResenaEntidad";
            List<ResenaEntidad> resenasEntidad = session
                    .createQuery(query, ResenaEntidad.class)
                    .getResultList();

            tx.commit();
            return resenasEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar reseñas en base de datos", e);
        }
    }

    @Override
    public Optional<ResenaEntidad> actualizar(Long idResena, ResenaForm formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            ResenaEntidad resenaEntidad = session.get(ResenaEntidad.class, idResena);
            if (resenaEntidad == null) {
                tx.commit();
                return Optional.empty();
            }
            ResenaFormularioAEntidadMapper.actualizar(resenaEntidad, formulario);

            tx.commit();
            return Optional.of(resenaEntidad);


        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar reseña en base de datos", e);
        }
    }

    @Override
    public boolean eliminar(Long idResena) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            ResenaEntidad resenaEntidad = session.get(ResenaEntidad.class, idResena);
            if (resenaEntidad == null) {
                tx.commit();
                return false;
            }
            session.remove(resenaEntidad);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al  en base de datos", e);
        }
    }
}
