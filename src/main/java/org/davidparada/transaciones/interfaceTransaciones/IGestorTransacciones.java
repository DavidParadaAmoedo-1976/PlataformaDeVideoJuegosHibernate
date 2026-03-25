package org.davidparada.transaciones.interfaceTransaciones;

import java.util.function.Supplier;

/**
 * Abstracción de unidad de trabajo atómica.
 * Desacopla el manejo de transacciones de los repositorios y el controlador.
 */
public interface IGestorTransacciones {

    /**
     * Ejecuta {@code work} dentro de una unidad de trabajo atómica.
     * Si ocurre cualquier excepción, la unidad se deshace (rollback).
     */
    <T> T inTransaction(Supplier<T> work);

}
