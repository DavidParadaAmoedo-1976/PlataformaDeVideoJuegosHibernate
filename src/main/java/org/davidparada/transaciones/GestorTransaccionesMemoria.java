package org.davidparada.transaciones;

import org.davidparada.excepcion.ValidationException;
import org.davidparada.transaciones.interfaceTransaciones.ExceptionSupplier;
import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Implementación no-op de {@link IGestorTransacciones}.
 * Se usa con repositorios en memoria donde no existe el concepto de transacción.
 */
public class GestorTransaccionesMemoria implements IGestorTransacciones {

    @SuppressWarnings("unchecked")
    @Override
    public <T> T inTransaction(ExceptionSupplier<T> work) throws ValidationException {
        try {
            return work.get();
        }
        catch (ValidationException ex) {
            throw ex;
        }
        catch (Exception e) {
            try {

                T temp = (T) Optional.empty();
                return temp;
            } catch (ClassCastException ex) {
                return null;
            }
        }

    }
}
