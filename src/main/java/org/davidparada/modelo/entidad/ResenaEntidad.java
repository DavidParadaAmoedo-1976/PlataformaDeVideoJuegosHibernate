package org.davidparada.modelo.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import org.davidparada.modelo.enums.EstadoPublicacionEnum;

import java.time.Instant;

@Entity
@Table(name = "resena")
public class ResenaEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Long idResena;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_juego", nullable = false)
    private Long idJuego;

    @Column(name = "recomendado", nullable = false)
    private boolean recomendado;

    @Size(min = 50, max = 8000)
    @Column(name = "texto_resena", nullable = false)
    private String textoResena;

    @Column(name = "cantidad_horas_jugadas", precision = 9, scale = 1)
    @DecimalMin(value = "0.0")
    @Digits(integer = 8, fraction = 1)
    private Double cantidadHorasJugadas;

    @Column(name = "fecha_publicacion")
    private Instant fechaPublicacion;

    @Column(name = "fecha_ultima_edicion")
    private Instant fechaUltimaEdicion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_publicacion")
    private EstadoPublicacionEnum estadoPublicacion = EstadoPublicacionEnum.PUBLICADA;

    // Constructor completo
    public ResenaEntidad(Long idResena,
                         Long idUsuario,
                         Long idJuego,
                         boolean recomendado,
                         String textoResena,
                         Double cantidadHorasJugadas,
                         Instant fechaPublicacion,
                         Instant fechaUltimaEdicion,
                         EstadoPublicacionEnum estadoPublicacion) {
        this.idResena = idResena;
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.recomendado = recomendado;
        this.textoResena = textoResena;
        this.cantidadHorasJugadas = cantidadHorasJugadas;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaUltimaEdicion = fechaUltimaEdicion;
        this.estadoPublicacion = estadoPublicacion;
    }

    // Constructor vacio
    public ResenaEntidad() {
    }

    // Getters
    public Long getIdResena() {
        return idResena;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdJuego() {
        return idJuego;
    }

    public boolean isRecomendado() {
        return recomendado;
    }

    public String getTextoResena() {
        return textoResena;
    }

    public Double getCantidadHorasJugadas() {
        return cantidadHorasJugadas;
    }

    public Instant getFechaPublicacion() {
        return fechaPublicacion;
    }

    public Instant getFechaUltimaEdicion() {
        return fechaUltimaEdicion;
    }

    public EstadoPublicacionEnum getEstadoPublicacion() {
        return estadoPublicacion;
    }

    // Setters

    public void setIdResena(Long idResena) {
        this.idResena = idResena;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdJuego(Long idJuego) {
        this.idJuego = idJuego;
    }

    public void setRecomendado(boolean recomendado) {
        this.recomendado = recomendado;
    }

    public void setTextoResena(String textoResena) {
        this.textoResena = textoResena;
    }

    public void setCantidadHorasJugadas(Double cantidadHorasJugadas) {
        this.cantidadHorasJugadas = cantidadHorasJugadas;
    }

    public void setFechaPublicacion(Instant fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public void setFechaUltimaEdicion(Instant fechaUltimaEdicion) {
        this.fechaUltimaEdicion = fechaUltimaEdicion;
    }

    public void setEstadoPublicacion(EstadoPublicacionEnum estadoPublicacion) {
        this.estadoPublicacion = estadoPublicacion;
    }
}