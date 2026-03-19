package org.davidparada.repositorio.implementacionHibernate;

import org.davidparada.modelo.entidad.UsuarioEntidad;
import org.davidparada.modelo.formulario.UsuarioForm;
import org.davidparada.modelo.mapper.UsuarioFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.IUsuarioRepo;
import org.davidparada.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class UsuarioRepoHibernate implements IUsuarioRepo {

    @Override
    public UsuarioEntidad crear(UsuarioForm form) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        UsuarioEntidad usuario =
                UsuarioFormularioAEntidadMapper.crearUsuarioEntidad(null, form);

        session.persist(usuario);

        tx.commit();
        session.close();

        return usuario;
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorId(Long id) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        UsuarioEntidad usuario = session.get(UsuarioEntidad.class, id);

        session.close();

        return Optional.ofNullable(usuario);
    }

    @Override
    public List<UsuarioEntidad> listarTodos() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<UsuarioEntidad> lista =
                session.createQuery("FROM UsuarioEntidad", UsuarioEntidad.class).list();

        session.close();

        return lista;
    }

    @Override
    public Optional<UsuarioEntidad> actualizar(Long id, UsuarioForm form) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        UsuarioEntidad existente = session.get(UsuarioEntidad.class, id);

        if (existente == null) {
            session.close();
            return Optional.empty();
        }

        UsuarioEntidad actualizado =
                UsuarioFormularioAEntidadMapper.actualizarUsuarioEntidad(id, form);

        session.merge(actualizado);

        tx.commit();
        session.close();

        return Optional.of(actualizado);
    }

    @Override
    public boolean eliminar(Long id) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        UsuarioEntidad usuario = session.get(UsuarioEntidad.class, id);

        if (usuario == null) {
            session.close();
            return false;
        }

        session.remove(usuario);

        tx.commit();
        session.close();

        return true;
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorEmail(String email) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        UsuarioEntidad usuario = session.createQuery(
                        "FROM UsuarioEntidad WHERE email = :email", UsuarioEntidad.class)
                .setParameter("email", email)
                .uniqueResult();

        session.close();

        return Optional.ofNullable(usuario);
    }

    @Override
    public Optional<UsuarioEntidad> buscarPorNombreUsuario(String nombreUsuario) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        UsuarioEntidad usuario = session.createQuery(
                        "FROM UsuarioEntidad WHERE nombreUsuario = :nombre", UsuarioEntidad.class)
                .setParameter("nombre", nombreUsuario)
                .uniqueResult();

        session.close();

        return Optional.ofNullable(usuario);
    }
}