package org.davidparada.vista.panel;

import org.davidparada.controlador.interfaceControlador.IUsuarioControlador;
import org.davidparada.excepcion.ValidationException;
import org.davidparada.modelo.dto.UsuarioDto;
import org.davidparada.modelo.formulario.validacion.ErrorModel;
import org.davidparada.vista.componente.BotonPrincipal;
import org.davidparada.vista.componente.BotonSecundario;
import org.davidparada.vista.estilo.EstiloUIEnum;
import org.davidparada.vista.util.Navegador;
import org.davidparada.vista.util.Sesion;

import javax.swing.*;
import java.awt.*;

public class PanelLogin extends JPanel {

    public PanelLogin(
            Navegador nav,
            PanelMenu panelMenu,
            IUsuarioControlador usuarioControlador
    ) {

        setLayout(new BorderLayout());

        // =========================
        // TITULO
        // =========================

        JLabel titulo = new JLabel("Iniciar sesión");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setFont(EstiloUIEnum.TITULO.font());
        titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        add(titulo, BorderLayout.NORTH);

        // =========================
        // PANEL CENTRAL
        // =========================

        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setBorder(BorderFactory.createEmptyBorder(20, 150, 20, 150));
        centro.setFont(EstiloUIEnum.SUBTITULO.font());


        // =========================
        // FORMULARIO
        // =========================

        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 10));
        formulario.setFont(EstiloUIEnum.SUBTITULO.font());

        JTextField txtUsuario = new JTextField();
        JPasswordField txtPassword = new JPasswordField();
        txtUsuario.setFont(EstiloUIEnum.SUBTITULO.font());


        formulario.add(new JLabel("Nombre de usuario"));
        formulario.add(txtUsuario);

        formulario.add(new JLabel("Contraseña"));
        formulario.add(txtPassword);

        centro.add(formulario);
        centro.add(Box.createRigidArea(new Dimension(0, 20)));

        // =========================
        // BOTONES
        // =========================

        JPanel botones = new JPanel(new FlowLayout());

        BotonPrincipal btnLogin = new BotonPrincipal(
                "Entrar",
                200,
                40,
                e -> {

                    try {

                        String usuario = txtUsuario.getText();
                        String password = new String(txtPassword.getPassword());

                        UsuarioDto usuarioDto =
                                usuarioControlador.login(usuario, password);

                        // iniciar sesión
                        Sesion.iniciarSesion(usuarioDto);

                        // actualizar menú
                        panelMenu.refrescar();

                        // ir al menú
                        nav.irA("menu");

                    } catch (ValidationException ex) {

                        StringBuilder mensaje = new StringBuilder();

                        for (ErrorModel err : ex.getErrores()) {
                            mensaje.append(err.campo())
                                    .append(" - ")
                                    .append(err.error())
                                    .append("\n");
                        }

                        JOptionPane.showMessageDialog(
                                this,
                                mensaje.toString(),
                                "Error de login",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
        );

        BotonSecundario btnVolver = new BotonSecundario(
                "Volver",
                200,
                40,
                e -> nav.irA("inicio")
        );

        botones.add(btnLogin);
        botones.add(btnVolver);

        centro.add(botones);

        add(centro, BorderLayout.CENTER);
    }
}