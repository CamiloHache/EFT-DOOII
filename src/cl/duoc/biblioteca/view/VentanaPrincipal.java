package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.model.Usuario;
import cl.duoc.biblioteca.controller.PrestamoTask;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private Usuario usuarioActual;

    public VentanaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Sistema Biblioteca - " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Barra de Menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menuGestiones = new JMenu("Gestión");
        JMenuItem itemLibros = new JMenuItem("Gestión de Libros");
        menuGestiones.add(itemLibros);

        JMenu menuPrestamos = new JMenu("Préstamos");
        JMenuItem itemPrestar = new JMenuItem("Registrar Préstamo Rápido (Prueba Hilo)");
        menuPrestamos.add(itemPrestar);

        menuBar.add(menuGestiones);
        menuBar.add(menuPrestamos);
        setJMenuBar(menuBar);

        // Acción de prueba para el hilo (presta el libro 1 al estudiante 1)
        itemPrestar.addActionListener(e -> probarHiloPrestamo());
    }

    private void probarHiloPrestamo() {
        JOptionPane.showMessageDialog(this, "Iniciando préstamo en segundo plano...");

        // Ejecutamos el hilo
        PrestamoTask task = new PrestamoTask(1, 1,
                () -> JOptionPane.showMessageDialog(this, "¡Préstamo registrado y stock descontado con éxito!"),
                () -> JOptionPane.showMessageDialog(this, "Error: Sin stock o datos inválidos.")
        );
        new Thread(task).start();
    }
}