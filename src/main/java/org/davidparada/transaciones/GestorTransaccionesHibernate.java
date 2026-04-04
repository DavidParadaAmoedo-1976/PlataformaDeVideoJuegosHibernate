package org.davidparada.transaciones;

import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;
import org.davidparada.transaciones.interfaceTransaciones.ISessionManager;
import org.davidparada.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.function.Supplier;


/**
 * Implementación Hibernate de {@link IGestorTransacciones}.
 * Gestiona el ciclo de vida de la sesión y la transacción.
 * Expone {@link #getSession()} para que {@code HibernateAlumnoRepository}
 * pueda acceder a la sesión activa durante el bloque de trabajo.
 */
public class GestorTransaccionesHibernate implements IGestorTransacciones, ISessionManager {

    private Session session;

    @Override
    public <T> T inTransaction(Supplier<T> work) {
        Transaction tx = null;
        Session s = null;

        try {
            s = HibernateUtil.getSessionFactory().openSession();
            session = s;

            tx = s.beginTransaction();

            T result = work.get();

            tx.commit();
            return result;

        } catch (Exception e) {

            if (tx != null && tx.isActive()) {
                tx.rollback();
                System.out.println("ROLLBACK");
            }

            throw new RuntimeException(e);

        } finally {

            if (s != null && s.isOpen()) {
                s.close();
            }

            session = null;
        }
    }

//    @Override
//    public <T> T inTransaction(Supplier<T> work) {
//        Transaction tx = null;
//        try (Session s = HibernateUtil.getSessionFactory().openSession()) {
//            session = s;
//            tx = s.beginTransaction();
//            T result = work.get();
//            tx.commit();
//            return result;
//        } catch (Exception e) {
//            if (tx != null) tx.rollback();
//            throw e;
//        } finally {
//            session = null;
//        }
//    }

    /**
     * Devuelve la sesión activa dentro de un bloque {@link #inTransaction}.
     */
    public Session getSession() {
        return session;
    }
}

