package org.davidparada.controlador.util;

import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.formulario.validacion.ErrorModel;

import java.util.List;

public class ComprobarErrores {

//    public static void comprobarListaErrores(List<ErrorModel> errores) throws ValidationException {
//        if (!errores.isEmpty()) {
//            throw new ValidationException(errores);
//        }
//    }

    public static void comprobarListaErrores(List<ErrorModel> errores) throws ValidationException {
        if (!errores.isEmpty()) {
            errores.forEach(e -> System.out.println(e.campo() + " - " + e.error()));
            throw new ValidationException(errores);
        }
    }
}
