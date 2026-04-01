package org.davidparada.modelo.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "biblioteca")
public class BibliotecaEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_biblioteca")
    private Long idBiblioteca;

    @Column(name = "id_usuario", nullable = false)
    private Long idUsuario;

    @Column(name = "id_juego", nullable = false)
    private Long idJuego;

    @Column(name = "fecha_adquisicion", nullable = false)
    private Instant fechaAdquisicion;

    @Column(name = "horas_de_juego")
    @DecimalMin(value = "0.0")
    @Digits(integer = 8, fraction = 1)
    private Double horasDeJuego = 0.0;

    @Column(name = "ultima_fecha_de_juego")
    private Instant ultimaFechaDeJuego;

    @Column(name = "estado_instalacion")
    private boolean estadoInstalacion = false;

    // Constructor completo
    public BibliotecaEntidad(Long idBiblioteca,
                             Long idUsuario,
                             Long idJuego,
                             Instant fechaAdquisicion,
                             Double horasDeJuego,
                             Instant ultimaFechaDeJuego,
                             boolean estadoInstalacion) {
        this.idBiblioteca = idBiblioteca;
        this.idUsuario = idUsuario;
        this.idJuego = idJuego;
        this.fechaAdquisicion = fechaAdquisicion;
        this.horasDeJuego = horasDeJuego;
        this.ultimaFechaDeJuego = ultimaFechaDeJuego;
        this.estadoInstalacion = estadoInstalacion;
    }

    // Constructor vacio
    public BibliotecaEntidad() {
    }

    // Getters
    public Long getIdBiblioteca() {
        return idBiblioteca;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdJuego() {
        return idJuego;
    }

    public Instant getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public Double getHorasDeJuego() {
        return horasDeJuego;
    }

    public Instant getUltimaFechaDeJuego() {
        return ultimaFechaDeJuego;
    }

    public boolean isEstadoInstalacion() {
        return estadoInstalacion;
    }

    // Setters
    public void setIdBiblioteca(Long idBiblioteca) {
        this.idBiblioteca = idBiblioteca;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setIdJuego(Long idJuego) {
        this.idJuego = idJuego;
    }

    public void setFechaAdquisicion(Instant fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public void setHorasDeJuego(Double horasDeJuego) {
        this.horasDeJuego = horasDeJuego;
    }

    public void setUltimaFechaDeJuego(Instant ultimaFechaDeJuego) {
        this.ultimaFechaDeJuego = ultimaFechaDeJuego;
    }

    public void setEstadoInstalacion(boolean estadoInstalacion) {
        this.estadoInstalacion = estadoInstalacion;
    }

    // toString
    @Override
    public String toString() {
        return "BibliotecaEntidad{" +
                "idBiblioteca=" + idBiblioteca +
                ", idUsuario=" + idUsuario +
                ", idJuego=" + idJuego +
                ", fechaAdquisicion=" + fechaAdquisicion +
                ", horasDeJuego=" + horasDeJuego +
                ", ultimaFechaDeJuego=" + ultimaFechaDeJuego +
                ", estadoInstalacion=" + estadoInstalacion +
                '}';
    }

    // Equals and hashCode

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BibliotecaEntidad that = (BibliotecaEntidad) o;
        return estadoInstalacion == that.estadoInstalacion
                && Objects.equals(idBiblioteca, that.idBiblioteca)
                && Objects.equals(idUsuario, that.idUsuario)
                && Objects.equals(idJuego, that.idJuego)
                && Objects.equals(fechaAdquisicion, that.fechaAdquisicion)
                && Objects.equals(horasDeJuego, that.horasDeJuego)
                && Objects.equals(ultimaFechaDeJuego, that.ultimaFechaDeJuego);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBiblioteca,
                idUsuario,
                idJuego,
                fechaAdquisicion,
                horasDeJuego,
                ultimaFechaDeJuego,
                estadoInstalacion);
    }
}