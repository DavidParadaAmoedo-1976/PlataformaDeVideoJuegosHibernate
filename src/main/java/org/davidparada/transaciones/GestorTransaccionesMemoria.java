//package org.davidparada.transaciones;
//
//import org.davidparada.transaciones.interfaceTransaciones.IGestorTransacciones;
//
//import java.util.function.Supplier;
//
///**
// * Implementación no-op de {@link IGestorTransacciones}.
// * Se usa con repositorios en memoria donde no existe el concepto de transacción.
// */
//public class GestorTransaccionesMemoria implements IGestorTransacciones {
//
//    @Override
//    public <T> T inTransaction(Supplier<T> work) {
//        return work.get();
//    }
//}
//
