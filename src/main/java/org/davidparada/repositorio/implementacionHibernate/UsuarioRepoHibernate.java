package org.davidparada.repositorio.implementacionHibernate;

import org.davidparada.modelo.entidad.UsuarioEntidad;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.modelo.mapper.UsuarioFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IUsuarioRepo;
import org.davidparada.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class UsuarioRepoHibernate implements IUsuarioRepo {

    @Override
    public UsuarioEntidad crear(UsuarioForm formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            UsuarioEntidad usuario = UsuarioFormularioAEntidadMapper.crearUsuarioEntidad(null, formulario);

            session.persist(usuario);

            tx.commit();
            return usuario;

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear usuario en base de datos", e);
        }
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorId(Long idUsuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            UsuarioEntidad usuario = session.get(UsuarioEntidad.class, idUsuario);

            tx.commit();
            return Optional.ofNullable(usuario);

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar usuario por Id", e);
        }
    }

    @Override
    public List<UsuarioEntidad> listarTodos() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM UsuarioEntidad";
            List<UsuarioEntidad> listaUsuarios = session.
                    createQuery(query, UsuarioEntidad.class)
                    .getResultList();

            tx.commit();
            return listaUsuarios;

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al listar los usuarios en base de datos", e);
        }
    }

    @Override
    public Optional<UsuarioEntidad> actualizar(Long idUsuario, UsuarioForm form) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            UsuarioEntidad usuarioEntidad = UsuarioFormularioAEntidadMapper.crearUsuarioEntidad(idUsuario, form);
            UsuarioEntidad usuarioActualizado = session.merge(usuarioEntidad);

            tx.commit();

            return Optional.of(usuarioActualizado);

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar usuario en base de datos", e);
        }
    }

    @Override
    public boolean eliminar(Long idUsuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            UsuarioEntidad usuarioEntidad = session.get(UsuarioEntidad.class, idUsuario);

            if (usuarioEntidad == null) {
                session.close();
                return false;
            }

            session.remove(usuarioEntidad);

            tx.commit();

            return true;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar usuario en base de datos", e);
        }
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorEmail(String email) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM UsuarioEntidad WHERE email = :email";
            UsuarioEntidad usuario = session.createQuery(query, UsuarioEntidad.class)
                    .setParameter("email", email)
                    .uniqueResult();

            session.close();

            return Optional.ofNullable(usuario);
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar usuario en base de datos", e);
        }
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorNombreUsuario(String nombreUsuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM UsuarioEntidad WHERE nombreUsuario = :nombreUsuario";
            UsuarioEntidad usuario = session.createQuery(query, UsuarioEntidad.class)
                    .setParameter("nombreUsuario", nombreUsuario)
                    .uniqueResult();

            tx.commit();
            return Optional.ofNullable(usuario);

        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar usuario en base de datos", e);
        }
    }
}