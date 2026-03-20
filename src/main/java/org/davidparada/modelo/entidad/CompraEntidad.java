package org.davidparada.modelo.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.davidparada.modelo.enums.EstadoCompraEnum;
import org.davidparada.modelo.enums.MetodoPagoEnum;

import java.time.Instant;

@Entity
@Table(name = "compra")
public class CompraEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Long idCompra;

    @Column(name = "id_usuario",nullable = false)
    private Long idUsuario;

    @Column(name = "id_juego",nullable = false)
    private Long idJuego;

    @Column(name = "fecha_compra")
    private Instant fechaCompra;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private MetodoPagoEnum metodoPago;

    @Column(name = "precio_base", nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.0")
    @Digits(integer = 8, fraction = 2)
    private Double precioBase;

    @Min(0)
    @Max(100)
    @Column(name = "descuento")
    private Integer descuento = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_compra")
    private EstadoCompraEnum estadoCompra = EstadoCompraEnum.PENDIENTE;

    // Constructor completo
    public CompraEntidad(Long idCompra,
                         Long idUsuario,
                         Long idJuego,
                         Instant fechaCompra,
                         MetodoPagoEnum metodoPago,
                         Double precioBase,
                         Integer descuento,
                         EstadoCompraEnum estadoCompra) {

        this.idCompra = idCompra;
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.fechaCompra = fechaCompra;
        this.metodoPago = metodoPago;
        this.precioBase = precioBase;
        this.descuento = descuento;
        this.estadoCompra = estadoCompra;
    }

    // Constructor vacio
    public CompraEntidad() {
    }

    // Getters
    public Long getIdCompra() {
        return idCompra;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdJuego() {
        return idJuego;
    }

    public Instant getFechaCompra() {
        return fechaCompra;
    }

    public MetodoPagoEnum getMetodoPago() {
        return metodoPago;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public EstadoCompraEnum getEstadoCompra() {
        return estadoCompra;
    }

    // Setters
    public void setIdCompra(Long idCompra) {
        this.idCompra = idCompra;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdJuego(Long idJuego) {
        this.idJuego = idJuego;
    }

    public void setFechaCompra(Instant fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public void setMetodoPago(MetodoPagoEnum metodoPago) {
        this.metodoPago = metodoPago;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }

    public void setEstadoCompra(EstadoCompraEnum estadoCompra) {
        this.estadoCompra = estadoCompra;
    }
}
