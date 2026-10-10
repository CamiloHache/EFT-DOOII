package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.controller.PrestamoTask;
import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.model.Libro;
import cl.duoc.biblioteca.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private Usuario usuarioActual;
    private JTable tablaLibros;
    private DefaultTableModel modeloTabla;

    public VentanaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("Sistema Biblioteca Escolar - " + usuario.getNombre() + " (" + usuario.getRol() + ")");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 1. Barra de Menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPrestamos = new JMenu("Préstamos");
        JMenuItem itemPrestar = new JMenuItem("Registrar Préstamo (Ejecutar Hilo)");
        menuPrestamos.add(itemPrestar);
        menuBar.add(menuPrestamos);
        setJMenuBar(menuBar);

        // 2. Panel Superior de Bienvenida
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JLabel lblBienvenida = new JLabel("👤 Conectado como: " + usuario.getNombre() + " [" + usuario.getRol() + "]");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 14));
        panelNorte.add(lblBienvenida);
        add(panelNorte, BorderLayout.NORTH);

        // 3. Panel Central con Tabla de Libros (Bloque 4)
        String[] columnas = {"ID", "Título", "Autor", "ISBN", "Editorial", "Stock", "Categoría"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaLibros = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);

        JPanel panelCentro = new JPanel(new BorderLayout());
        panelCentro.setBorder(BorderFactory.createTitledBorder(" Catálogo de Libros en Sistema "));
        panelCentro.add(scrollPane, BorderLayout.CENTER);
        add(panelCentro, BorderLayout.CENTER);

        // Cargar los datos reales de la BD en la tabla al abrir la ventana
        cargarLibros();

        // 4. Acción del menú para probar concurrencia con hilos
        itemPrestar.addActionListener(e -> ejecutarPrestamoConHilo());
    }

    private void cargarLibros() {
        LibroDAO libroDAO = new LibroDAO();
        List<Libro> libros = libroDAO.listarTodos();
        modeloTabla.setRowCount(0); // Limpiar datos previos

        for (Libro l : libros) {
            Object[] fila = {
                    l.getId(),
                    l.getTitulo(),
                    l.getAutor(),
                    l.getIsbn(),
                    l.getEditorial(),
                    l.getStock(),
                    l.getIdCategoria()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void ejecutarPrestamoConHilo() {
        // Validación rápida seleccionando el primer libro de la lista por defecto
        int idLibroEjemplo = 1;
        int idEstudianteEjemplo = 1;

        JOptionPane.showMessageDialog(this, "Procesando préstamo en segundo plano (Thread)...");

        PrestamoTask task = new PrestamoTask(idEstudianteEjemplo, idLibroEjemplo,
                () -> {
                    JOptionPane.showMessageDialog(this, "¡Préstamo registrado y stock descontado con éxito!");
                    cargarLibros(); // Refrescar la tabla para ver el stock actualizado
                },
                () -> JOptionPane.showMessageDialog(this, "Error: El libro no cuenta con stock disponible.", "Atención", JOptionPane.WARNING_MESSAGE)
        );
        new Thread(task).start();
    }
}