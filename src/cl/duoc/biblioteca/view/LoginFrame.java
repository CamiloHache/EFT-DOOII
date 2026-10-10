package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.controller.AuthController;
import cl.duoc.biblioteca.model.Usuario;
import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private JTextField txtCorreo;
    private JPasswordField txtPassword;
    private AuthController authController;

    public LoginFrame() {
        authController = new AuthController();
        setTitle("Login - Biblioteca Escolar");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 5, 5));

        add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        add(txtCorreo);

        add(new JLabel("Contraseña:"));
        txtPassword = new JPasswordField();
        add(txtPassword);

        JButton btnLogin = new JButton("Iniciar Sesión");
        add(new JLabel("")); // Espacio vacío
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String correo = txtCorreo.getText();
            String pass = new String(txtPassword.getPassword());
            Usuario user = authController.iniciarSesion(correo, pass);

            if (user != null) {
                new VentanaPrincipal(user).setVisible(true);
                this.dispose(); // Cierra el login
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas");
            }
        });
    }
}