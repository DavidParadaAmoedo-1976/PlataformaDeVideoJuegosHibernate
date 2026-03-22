package org.davidparada.repositorio.implementacionHibernate;

import org.davidparada.modelo.entidad.BibliotecaEntidad;
import org.davidparada.modelo.entidad.UsuarioEntidad;
import org.davidparada.modelo.formulario.BibliotecaForm;
import org.davidparada.modelo.mapper.BibliotecaFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IBibliotecaRepo;
import org.davidparada.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class BibliotecaRepoHibernate implements IBibliotecaRepo {
    @Override
    public List<BibliotecaEntidad> buscarPorUsuario(Long idUsuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM BibliotecaEntidad WHERE idUsuario = :idUsuario";
            List<BibliotecaEntidad>  bibliotecasEntidad = session
                    .createQuery(query, BibliotecaEntidad.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();

            tx.commit();
            return bibliotecasEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar biblioteca en base de datos", e);
        }
    }


    @Override
    public Optional<BibliotecaEntidad> buscarPorUsuarioYJuego(Long idUsuario, Long idJuego) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM BibliotecaEntidad WHERE idUsuario = :idUsuario AND idJuego = :idJuego";
            Optional<BibliotecaEntidad> bibliotecaEntidad = session
                    .createQuery(query, BibliotecaEntidad.class)
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("idJuego", idJuego)
                    .uniqueResultOptional();

            tx.commit();
            return bibliotecaEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar biblioteca en base de datos", e);
        }
    }

    @Override
    public BibliotecaEntidad crear(BibliotecaForm formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            BibliotecaEntidad bibliotecaEntidad = BibliotecaFormularioAEntidadMapper.crearBibliotecaEntidad(formulario);
            session.persist(bibliotecaEntidad);

            tx.commit();
            return bibliotecaEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear biblioteca en base de datos", e);
        }
    }

    @Override
    public Optional<BibliotecaEntidad> buscarPorId(Long idBiblioteca) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Optional<BibliotecaEntidad> bibliotecaEntidad = Optional.ofNullable(session.get(BibliotecaEntidad.class, idBiblioteca));

            tx.commit();
            return bibliotecaEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar biblioteca en base de datos", e);
        }
    }

    @Override
    public List<BibliotecaEntidad> listarTodos() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM BibliotecaEntidad";
            List<BibliotecaEntidad> bibliotecasEntidad = session
                    .createQuery(query, BibliotecaEntidad.class)
                    .getResultList();

            tx.commit();
            return bibliotecasEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar bibliotecas en base de datos", e);
        }
    }

    @Override
    public Optional<BibliotecaEntidad> actualizar(Long idBiblioteca, BibliotecaForm formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

        BibliotecaEntidad bibliotecaEntidad = session.get(BibliotecaEntidad.class, idBiblioteca);
        if(bibliotecaEntidad == null){
            tx.commit();
            return Optional.empty();
        }
        BibliotecaFormularioAEntidadMapper.actualizar(bibliotecaEntidad, formulario);

        tx.commit();
        return Optional.of(bibliotecaEntidad);

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar la biblioteca en base de datos", e);
        }
    }

    @Override
    public boolean eliminar(Long idBiblioteca) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            BibliotecaEntidad bibliotecaEntidad = session.get(BibliotecaEntidad.class, idBiblioteca);
            if(bibliotecaEntidad == null){
                tx.commit();
                return false;
            }
            session.remove(bibliotecaEntidad);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar biblioteca en base de datos", e);
        }
    }
}
