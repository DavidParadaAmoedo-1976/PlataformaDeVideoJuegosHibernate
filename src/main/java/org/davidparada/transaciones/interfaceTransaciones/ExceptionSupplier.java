package org.davidparada.transaciones.interfaceTransaciones;

import org.davidparada.excepcion.ValidationException;

public interface ExceptionSupplier<T> {
    T get() throws ValidationException;
}
