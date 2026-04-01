package org.davidparada.transaciones.interfaceTransaciones;

import org.davidparada.modelo.entidad.JuegoEntidad;
import org.hibernate.Session;

public interface ISessionManager {
    Session getSession();

}
