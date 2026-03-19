package org.davidparada.modelo.entidad;

import jakarta.persistence.*;
import org.davidparada.modelo.enums.EstadoCuentaEnum;
import org.davidparada.modelo.enums.PaisEnum;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
public class UsuarioEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(nullable = false, unique = true)
    private String nombreUsuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String nombreReal;

    @Enumerated(EnumType.STRING)
    private PaisEnum pais;

    private LocalDate fechaNacimiento;

    private Instant fechaRegistro;

    private String avatar;

    private Double saldo;

    @Enumerated(EnumType.STRING)
    private EstadoCuentaEnum estadoCuenta;

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
}
