package org.davidparada.repositorio.implementacionHibernate;

import org.davidparada.modelo.entidad.CompraEntidad;
import org.davidparada.modelo.formulario.CompraForm;
import org.davidparada.modelo.mapper.CompraFormularioAEntidadMapper;
import org.davidparada.repositorio.interfaceRepositorio.ICompraRepo;
import org.davidparada.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class CompraRepoHibernate implements ICompraRepo {
    @Override
    public List<CompraEntidad> buscarPorUsuario(Long idUsuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM CompraEntidad WHERE idUsuario = :idUsuario";
            List<CompraEntidad> comprasEntidad = session
                    .createQuery(query, CompraEntidad.class)
                    .setParameter("idUsuario", idUsuario)
                    .getResultList();

            tx.commit();
            return comprasEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar compras en base de datos", e);
        }
    }

    @Override
    public Optional<CompraEntidad> buscarPorCompraYUsuario(Long idCompra, Long idUsuario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM CompraEntidad where idCompra = :idCompra and idUsuario = :idUsuario";
            Optional<CompraEntidad> compraEntidad = session
                    .createQuery(query, CompraEntidad.class)
                    .setParameter("idCompra", idCompra)
                    .setParameter("idUsuario", idUsuario)
                    .uniqueResultOptional();

            tx.commit();
            return compraEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar compra en base de datos", e);
        }
    }

    @Override
    public CompraEntidad crear(CompraForm formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            CompraEntidad compraEntidad = CompraFormularioAEntidadMapper.crearCompraEntidad(formulario);
            session.persist(compraEntidad);

            tx.commit();
            return compraEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al crear compra en base de datos", e);
        }
    }

    @Override
    public Optional<CompraEntidad> buscarPorId(Long idCompra) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Optional<CompraEntidad> compraEntidad = Optional.ofNullable(session.get(CompraEntidad.class, idCompra));

            tx.commit();
            return compraEntidad;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar compra en base de datos", e);
        }
    }

    @Override
    public List<CompraEntidad> listarTodos() {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            String query = "FROM CompraEntidad";
            List<CompraEntidad> comprasEntidad = session
                    .createQuery(query, CompraEntidad.class)
                    .getResultList();

            tx.commit();
            return comprasEntidad;


        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al buscar compras en base de datos", e);
        }
    }

    @Override
    public Optional<CompraEntidad> actualizar(Long idCompra, CompraForm formulario) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            CompraEntidad compraEntidad = session.get(CompraEntidad.class, idCompra);
            if (compraEntidad != null) {
                tx.commit();
                return Optional.empty();
            }
            CompraFormularioAEntidadMapper.actualizar(compraEntidad, formulario);

            tx.commit();
            return Optional.of(compraEntidad);

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al actualizar compra en base de datos", e);
        }
    }

    @Override
    public boolean eliminar(Long idCompra) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            CompraEntidad compraEntidad = session.get(CompraEntidad.class, idCompra);
            if (compraEntidad == null) {
                tx.commit();
                return false;
            }
            session.remove(compraEntidad);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException("Error al eliminar compra en base de datos", e);
        }
    }
}
