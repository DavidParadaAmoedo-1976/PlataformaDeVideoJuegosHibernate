package org.davidparada.modelo.entidad;

import jakarta.persistence.*;
import org.davidparada.modelo.enums.EstadoCuentaEnum;
import org.davidparada.modelo.enums.PaisEnum;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "usuarios")
public class UsuarioEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nombre_real", nullable = false, length = 100)
    private String nombreReal;

    @Enumerated(EnumType.STRING)
    @Column(name = "pais", nullable = false, length = 50)
    private PaisEnum pais;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_registro")
    private Instant fechaRegistro = Instant.now();

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "saldo")
    private Double saldo = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_cuenta", length = 20)
    private EstadoCuentaEnum estadoCuenta = EstadoCuentaEnum.ACTIVA;

    // Constructor completo
    public UsuarioEntidad(Long idUsuario,
                          String nombreUsuario,
                          String email,
                          String password,
                          String nombreReal,
                          PaisEnum pais,
                          LocalDate fechaNacimiento,
                          Instant fechaRegistro,
                          String avatar,
                          Double saldo,
                          EstadoCuentaEnum estadoCuenta) {

        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.nombreReal = nombreReal;
        this.pais = pais;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.avatar = avatar;
        this.saldo = saldo;
        this.estadoCuenta = estadoCuenta;
    }

    // Constructor vacio
    public UsuarioEntidad() {
    }

    // Getters
    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNombreReal() {
        return nombreReal;
    }

    public PaisEnum getPais() {
        return this.pais;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Instant getFechaRegistro() {
        return fechaRegistro;
    }

    public String getAvatar() {
        return avatar;
    }

    public Double getSaldo() {
        return saldo;
    }

    public EstadoCuentaEnum getEstadoCuenta() {
        return estadoCuenta;
    }

    // Setters
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNombreReal(String nombreReal) {
        this.nombreReal = nombreReal;
    }

    public void setPais(PaisEnum pais) {
        this.pais = pais;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setFechaRegistro(Instant fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public void setEstadoCuenta(EstadoCuentaEnum estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    // toString

    @Override
    public String toString() {
        return "UsuarioEntidad{" +
                "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nombreReal='" + nombreReal + '\'' +
                ", pais=" + pais +
                ", fechaNacimiento=" + fechaNacimiento +
                ", fechaRegistro=" + fechaRegistro +
                ", avatar='" + avatar + '\'' +
                ", saldo=" + saldo +
                ", estadoCuenta=" + estadoCuenta +
                '}';
    }

    // Equals y hashCode
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UsuarioEntidad that = (UsuarioEntidad) o;
        return Objects.equals(idUsuario, that.idUsuario) && Objects.equals(nombreUsuario, that.nombreUsuario) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(nombreReal, that.nombreReal) && pais == that.pais && Objects.equals(fechaNacimiento, that.fechaNacimiento) && Objects.equals(fechaRegistro, that.fechaRegistro) && Objects.equals(avatar, that.avatar) && Objects.equals(saldo, that.saldo) && estadoCuenta == that.estadoCuenta;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario, nombreUsuario, email, password, nombreReal, pais, fechaNacimiento, fechaRegistro, avatar, saldo, estadoCuenta);
    }
}
