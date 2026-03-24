package org.davidparada.modelo.mapper;

import org.davidparada.modelo.entidad.CompraEntidad;
import org.davidparada.modelo.enums.EstadoCompraEnum;
import org.davidparada.modelo.formulario.CompraForm;

import java.time.Instant;

public class CompraFormularioAEntidadMapper {

    public static final int DESCUENTO_POR_DEFECTO = 0;
    public static final double REDONDEO = 100.0;

    private CompraFormularioAEntidadMapper() {
    }

    public static CompraEntidad crearCompraEntidad(Long idCompra, CompraForm formulario) {

        return new CompraEntidad(
                idCompra,
                formulario.getIdUsuario(),
                formulario.getIdJuego(),
                Instant.now(),
                formulario.getMetodoPago(),
                formulario.getPrecioBase(),
                DESCUENTO_POR_DEFECTO,
                EstadoCompraEnum.PENDIENTE
        );
    }

    public static CompraEntidad crearCompraEntidad(CompraForm formulario) {
        return crearCompraEntidad(null, formulario);
    }

    public static CompraEntidad actualizarCompraEntidad(Long idCompra, CompraForm formulario) {
//
//        double precioBase = redondear(formulario.getPrecioBase());

        return new CompraEntidad(
                idCompra,
                formulario.getIdUsuario(),
                formulario.getIdJuego(),
                formulario.getFechaCompra(),
                formulario.getMetodoPago(),
                formulario.getPrecioBase(),
                formulario.getDescuento(),
                formulario.getEstadoCompra()
        );
    }

//    private static double redondear(double valor) {
//        return Math.round(valor * REDONDEO) / REDONDEO;
//    }
}
