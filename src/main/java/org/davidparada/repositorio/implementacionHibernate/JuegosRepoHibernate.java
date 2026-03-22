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
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "SELECT COUNT(j) FROM JuegoEntidad j WHERE LOWER(j.titulo) = LOWER(:titulo)";
            Long contador = session.createQuery(query, Long.class)
                    .setParameter("titulo", titulo)
                    .uniqueResult();

            tx.commit();
            return contador != null && contador > 0;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar el titulo en base de datos", e);
        }
    }

    @Override
    public List<JuegoEntidad> buscarConFiltros(String titulo, String categoria, Double precioMin, Double precioMax, ClasificacionJuegoEnum clasificacion, EstadoJuegoEnum estado) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            //-------------------------------------------
            //-------------------------------------------

            tx.commit();
            return List.of();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar juegos en base de datos", e);
        }
    }

    @Override
    public JuegoEntidad crear(JuegoForm formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            JuegoEntidad juegoEntidad = JuegoFormularioAEntidadMapper.crearJuegoEntidad(formulario);
            session.persist(juegoEntidad);

            tx.commit();
            return juegoEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear juego en base de datos", e);
        }
    }

    @Override
    public Optional<JuegoEntidad> buscarPorId(Long idJuego) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Optional<JuegoEntidad> juegoEntidad = Optional.ofNullable(session.get(JuegoEntidad.class, idJuego));

            tx.commit();
            return juegoEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar juego en base de datos", e);
        }
    }

    @Override
    public List<JuegoEntidad> listarTodos() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM JuegoEntidad";
            List<JuegoEntidad> listaJuegos = session
                    .createQuery(query, JuegoEntidad.class)
                    .getResultList();

            tx.commit();
            return listaJuegos;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al listar los juegos de base de datos", e);
        }
    }

    @Override
    public Optional<JuegoEntidad> actualizar(Long idJuego, JuegoForm formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            JuegoEntidad juegoEntidad = session.get(JuegoEntidad.class, idJuego);
            if (juegoEntidad == null) {
                tx.commit();
                return Optional.empty();
            }
            JuegoFormularioAEntidadMapper.actualizar(juegoEntidad, formulario);

            tx.commit();
            return Optional.of(juegoEntidad);

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar el juego en base de datos", e);
        }
    }

    @Override
    public boolean eliminar(Long idJuego) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            JuegoEntidad juegoEntidad = session.get(JuegoEntidad.class, idJuego);
            if (juegoEntidad == null) {
                tx.commit();
                return false;
            }
            session.remove(juegoEntidad);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar juego en la base de datos", e);
        }
    }
}
