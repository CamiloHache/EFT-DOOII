package cl.duoc.biblioteca.main;

import cl.duoc.biblioteca.view.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Lanzamos la ventana de Login de manera segura
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}