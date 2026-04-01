package org.davidparada.modelo.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.davidparada.modelo.enums.ClasificacionJuegoEnum;
import org.davidparada.modelo.enums.EstadoJuegoEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "juegos")
public class JuegoEntidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_juego")
    private Long idJuego;

    @Column(name = "titulo", nullable = false, unique = true, length = 100)
    private String titulo;

    @Column(name = "descripcion", length = 2000)
    private String descripcion;

    @Size(min = 2, max = 100)
    @Column(name = "desarrollador", nullable = false, length = 100)
    private String desarrollador;

    @Column(name = "fecha_lanzamiento", nullable = false)
    private LocalDate fechaLanzamiento;

    @Column(name = "precio_base", nullable = false)
    @DecimalMin(value = "0.0")
    @Digits(integer = 8, fraction = 2)
    private Double precioBase;

    @Min(0)
    @Max(100)
    @Column(name = "descuento")
    private Integer descuento = 0;

    @Column(name = "categoria")
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "clasificacion_por_edad", nullable = false)
    private ClasificacionJuegoEnum clasificacionPorEdad;

    @ElementCollection
    @CollectionTable(name = "juego_idiomas", joinColumns = @JoinColumn(name = "id_juego"))
    @Column(name = "idioma")
    private List<String> idiomas;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoJuegoEnum estado;

    // Constructor completo
    public JuegoEntidad(Long idJuego,
                        String titulo,
                        String descripcion,
                        String desarrollador,
                        LocalDate fechaLanzamiento,
                        Double precioBase,
                        Integer descuento,
                        String categoria,
                        ClasificacionJuegoEnum clasificacionPorEdad,
                        List<String> idiomas,
                        EstadoJuegoEnum estado) {

        this.idJuego = idJuego;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.desarrollador = desarrollador;
        this.fechaLanzamiento = fechaLanzamiento;
        this.precioBase = precioBase;
        this.descuento = descuento;
        this.categoria = categoria;
        this.clasificacionPorEdad = clasificacionPorEdad;
        this.idiomas = idiomas;
        this.estado = estado;
    }

    // Constructor vacio
    public JuegoEntidad() {
    }

    // Getters
    public Long getIdJuego() {
        return idJuego;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public Double getPrecioBase() {
        return precioBase;
    }

    public Integer getDescuento() {
        return descuento;
    }

    public String getCategoria() {
        return categoria;
    }

    public ClasificacionJuegoEnum getClasificacionPorEdad() {
        return clasificacionPorEdad;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public EstadoJuegoEnum getEstado() {
        return estado;
    }

    // Setters
    public void setIdJuego(Long idJuego) {
        this.idJuego = idJuego;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public void setPrecioBase(Double precioBase) {
        this.precioBase = precioBase;
    }

    public void setDescuento(Integer descuento) {
        this.descuento = descuento;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setClasificacionPorEdad(ClasificacionJuegoEnum clasificacionPorEdad) {
        this.clasificacionPorEdad = clasificacionPorEdad;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public void setEstado(EstadoJuegoEnum estado) {
        this.estado = estado;
    }

    // toString
    @Override
    public String toString() {
        return "JuegoEntidad{" +
                "idJuego=" + idJuego +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", desarrollador='" + desarrollador + '\'' +
                ", fechaLanzamiento=" + fechaLanzamiento +
                ", precioBase=" + precioBase +
                ", descuento=" + descuento +
                ", categoria='" + categoria + '\'' +
                ", clasificacionPorEdad=" + clasificacionPorEdad +
                ", idiomas=" + idiomas +
                ", estado=" + estado +
                '}';
    }

    // Equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JuegoEntidad that = (JuegoEntidad) o;
        return Objects.equals(idJuego, that.idJuego)
                && Objects.equals(titulo, that.titulo)
                && Objects.equals(descripcion, that.descripcion)
                && Objects.equals(desarrollador, that.desarrollador)
                && Objects.equals(fechaLanzamiento, that.fechaLanzamiento)
                && Objects.equals(precioBase, that.precioBase)
                && Objects.equals(descuento, that.descuento)
                && Objects.equals(categoria, that.categoria)
                && clasificacionPorEdad == that.clasificacionPorEdad
                && Objects.equals(idiomas, that.idiomas)
                && estado == that.estado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idJuego,
                titulo,
                descripcion,
                desarrollador,
                fechaLanzamiento,
                precioBase,
                descuento,
                categoria,
                clasificacionPorEdad,
                idiomas,
                estado);
    }
}
