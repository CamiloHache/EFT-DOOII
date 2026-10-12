
package cl.duoc.biblioteca.view;

import cl.duoc.biblioteca.controller.PrestamoTask;
import cl.duoc.biblioteca.dao.CategoriaDAO;
import cl.duoc.biblioteca.dao.EstudianteDAO;
import cl.duoc.biblioteca.dao.LibroDAO;
import cl.duoc.biblioteca.dao.PrestamoDAO;
import cl.duoc.biblioteca.dao.ReporteDAO;
import cl.duoc.biblioteca.model.Categoria;
import cl.duoc.biblioteca.model.Estudiante;
import cl.duoc.biblioteca.model.Libro;
import cl.duoc.biblioteca.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    private final Usuario usuarioActual;

    private final LibroDAO libroDAO = new LibroDAO();
    private final EstudianteDAO estudianteDAO = new EstudianteDAO();
    private final PrestamoDAO prestamoDAO = new PrestamoDAO();
    private final ReporteDAO reporteDAO = new ReporteDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    private JTable tablaLibros;
    private JTable tablaEstudiantes;
    private JTable tablaPrestamos;
    private JTable tablaCategorias;

    private DefaultTableModel modeloLibros;
    private DefaultTableModel modeloEstudiantes;
    private DefaultTableModel modeloPrestamos;
    private DefaultTableModel modeloCategorias;

    public VentanaPrincipal(Usuario usuario) {
        usuarioActual = usuario;

        setTitle("Biblioteca Escolar - " + usuario.getNombre()
                + " (" + usuario.getRol() + ")");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel bienvenida = new JLabel(
                "Conectado como: " + usuario.getNombre()
                        + " | Rol: " + usuario.getRol()
        );
        bienvenida.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(bienvenida, BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();

        // Catálogo
        JPanel panelLibros = new JPanel(new BorderLayout());
        modeloLibros = new DefaultTableModel(
                new String[]{"ID", "Título", "Autor", "ISBN",
                        "Editorial", "Stock", "ID Categoría"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaLibros = new JTable(modeloLibros);
        panelLibros.add(new JScrollPane(tablaLibros), BorderLayout.CENTER);

        JPanel botonesLibros = new JPanel();
        JButton btnActualizarLibros = new JButton("Actualizar");
        JButton btnCrearLibro = new JButton("Registrar libro");
        JButton btnEditarLibro = new JButton("Modificar libro");
        JButton btnEliminarLibro = new JButton("Eliminar libro");
        JButton btnPrestar = new JButton("Registrar préstamo");

        botonesLibros.add(btnActualizarLibros);
        botonesLibros.add(btnCrearLibro);
        botonesLibros.add(btnEditarLibro);
        botonesLibros.add(btnEliminarLibro);
        botonesLibros.add(btnPrestar);
        panelLibros.add(botonesLibros, BorderLayout.SOUTH);

        pestañas.addTab("Catálogo y préstamos", panelLibros);

        // Estudiantes
        JPanel panelEstudiantes = new JPanel(new BorderLayout());
        modeloEstudiantes = new DefaultTableModel(
                new String[]{"ID", "Nombre", "RUT", "Curso", "Correo"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaEstudiantes = new JTable(modeloEstudiantes);
        panelEstudiantes.add(new JScrollPane(tablaEstudiantes), BorderLayout.CENTER);

        JPanel botonesEstudiantes = new JPanel();
        JButton btnActualizarEstudiantes = new JButton("Actualizar");
        JButton btnCrearEstudiante = new JButton("Registrar estudiante");
        JButton btnEditarEstudiante = new JButton("Modificar estudiante");
        JButton btnEliminarEstudiante = new JButton("Eliminar estudiante");

        botonesEstudiantes.add(btnActualizarEstudiantes);
        botonesEstudiantes.add(btnCrearEstudiante);
        botonesEstudiantes.add(btnEditarEstudiante);
        botonesEstudiantes.add(btnEliminarEstudiante);
        panelEstudiantes.add(botonesEstudiantes, BorderLayout.SOUTH);

        pestañas.addTab("Estudiantes", panelEstudiantes);


        // Categorías
        JPanel panelCategorias = new JPanel(new BorderLayout());

        modeloCategorias = new DefaultTableModel(
                new String[]{"ID", "Nombre"}, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCategorias = new JTable(modeloCategorias);
        panelCategorias.add(
                new JScrollPane(tablaCategorias), BorderLayout.CENTER);

        JPanel botonesCategorias = new JPanel();

        JButton btnActualizarCategorias = new JButton("Actualizar");
        JButton btnCrearCategoria = new JButton("Registrar categoría");
        JButton btnEditarCategoria = new JButton("Modificar categoría");
        JButton btnEliminarCategoria = new JButton("Eliminar categoría");

        botonesCategorias.add(btnActualizarCategorias);
        botonesCategorias.add(btnCrearCategoria);
        botonesCategorias.add(btnEditarCategoria);
        botonesCategorias.add(btnEliminarCategoria);

        panelCategorias.add(botonesCategorias, BorderLayout.SOUTH);
        pestañas.addTab("Categorías", panelCategorias);

        btnActualizarCategorias.addActionListener(e -> cargarCategorias());
        btnCrearCategoria.addActionListener(e -> registrarCategoria());
        btnEditarCategoria.addActionListener(e -> modificarCategoria());
        btnEliminarCategoria.addActionListener(e -> eliminarCategoria());


        // Préstamos pendientes
        JPanel panelPrestamos = new JPanel(new BorderLayout());
        modeloPrestamos = new DefaultTableModel(
                new String[]{
                        "ID préstamo",
                        "Estudiante",
                        "Libro",
                        "Fecha préstamo",
                        "Fecha devolución",
                        "Estado"
                }, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPrestamos = new JTable(modeloPrestamos);
        panelPrestamos.add(new JScrollPane(tablaPrestamos), BorderLayout.CENTER);

        JPanel botonesPrestamos = new JPanel();
        JButton btnActualizarPrestamos = new JButton("Actualizar préstamos");
        JButton btnDevolver = new JButton("Registrar devolución");

        botonesPrestamos.add(btnActualizarPrestamos);
        botonesPrestamos.add(btnDevolver);
        panelPrestamos.add(botonesPrestamos, BorderLayout.SOUTH);

        pestañas.addTab("Devoluciones", panelPrestamos);

        // Reportes
        JPanel panelReportes = new JPanel(new GridLayout(3, 1, 10, 10));
        panelReportes.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnLibrosMasPrestados = new JButton("Libros más prestados");
        JButton btnHistorial = new JButton("Historial por estudiante");
        JButton btnPrestamosActivos = new JButton("Libros actualmente en préstamo");

        panelReportes.add(btnLibrosMasPrestados);
        panelReportes.add(btnHistorial);
        panelReportes.add(btnPrestamosActivos);

        pestañas.addTab("Reportes", panelReportes);

        add(pestañas, BorderLayout.CENTER);

        boolean esBibliotecario =
                usuarioActual.getRol() != null
                        && usuarioActual.getRol().equalsIgnoreCase("bibliotecario");

        // Los estudiantes consultan y gestionan sus préstamos,
        // pero no administran libros ni estudiantes.
        btnCrearLibro.setVisible(esBibliotecario);
        btnEditarLibro.setVisible(esBibliotecario);
        btnEliminarLibro.setVisible(esBibliotecario);
        pestañas.setEnabledAt(1, esBibliotecario);
        pestañas.setEnabledAt(2, esBibliotecario);

        cargarLibros();
        cargarEstudiantes();
        cargarPrestamosActivos();
        cargarCategorias();

        btnActualizarLibros.addActionListener(e -> cargarLibros());
        btnActualizarEstudiantes.addActionListener(e -> cargarEstudiantes());
        btnActualizarPrestamos.addActionListener(e -> cargarPrestamosActivos());

        btnCrearLibro.addActionListener(e -> registrarLibro());
        btnEditarLibro.addActionListener(e -> modificarLibro());
        btnEliminarLibro.addActionListener(e -> eliminarLibro());

        btnCrearEstudiante.addActionListener(e -> registrarEstudiante());
        btnEditarEstudiante.addActionListener(e -> modificarEstudiante());
        btnEliminarEstudiante.addActionListener(e -> eliminarEstudiante());

        btnPrestar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> registrarDevolucion());

        btnLibrosMasPrestados.addActionListener(e -> mostrarLibrosMasPrestados());
        btnHistorial.addActionListener(e -> mostrarHistorial());
        btnPrestamosActivos.addActionListener(e -> mostrarPrestamosActivos());
    }

    private void cargarLibros() {
        modeloLibros.setRowCount(0);

        for (Libro libro : libroDAO.listarTodos()) {
            modeloLibros.addRow(new Object[]{
                    libro.getId(), libro.getTitulo(), libro.getAutor(),
                    libro.getIsbn(), libro.getEditorial(), libro.getStock(),
                    libro.getIdCategoria()
            });
        }
    }

    private void cargarEstudiantes() {
        modeloEstudiantes.setRowCount(0);

        for (Estudiante estudiante : estudianteDAO.listarTodos()) {
            modeloEstudiantes.addRow(new Object[]{
                    estudiante.getId(), estudiante.getNombre(),
                    estudiante.getRut(), estudiante.getCurso(),
                    estudiante.getCorreo()
            });
        }
    }

    private void cargarPrestamosActivos() {
        modeloPrestamos.setRowCount(0);

        for (String[] fila : reporteDAO.listarPrestamosActivos()) {
            modeloPrestamos.addRow(fila);
        }
    }

    private int obtenerIdSeleccionado(JTable tabla) {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro de la tabla.");
            return -1;
        }

        return Integer.parseInt(tabla.getValueAt(fila, 0).toString());
    }

    private String pedirTexto(String mensaje, String valorInicial) {
        return JOptionPane.showInputDialog(this, mensaje, valorInicial);
    }

    private void registrarLibro() {
        try {
            String titulo = pedirTexto("Título del libro:", "");
            if (titulo == null || titulo.trim().isEmpty()) return;

            String autor = pedirTexto("Autor:", "");
            if (autor == null) return;

            String isbn = pedirTexto("ISBN:", "");
            if (isbn == null) return;

            String editorial = pedirTexto("Editorial:", "");
            if (editorial == null) return;

            int stock = Integer.parseInt(pedirTexto("Stock disponible:", "1"));
            if (stock < 0) throw new IllegalArgumentException();

            List<Categoria> categorias = categoriaDAO.listarTodos();
            StringBuilder opciones = new StringBuilder("Categorías disponibles:\n");
            for (Categoria categoria : categorias) {
                opciones.append(categoria.getId()).append(" - ")
                        .append(categoria.getNombre()).append("\n");
            }

            int idCategoria = Integer.parseInt(
                    pedirTexto(opciones + "\nIngresa el ID de categoría:", "")
            );

            Libro libro = new Libro(0, titulo, autor, isbn, editorial,
                    stock, idCategoria);

            if (libroDAO.create(libro)) {
                JOptionPane.showMessageDialog(this, "Libro registrado correctamente.");
                cargarLibros();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar el libro.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Datos inválidos. Revisa el stock y el ID de categoría.");
        }
    }

    private void modificarLibro() {
        int id = obtenerIdSeleccionado(tablaLibros);
        if (id == -1) return;

        int fila = tablaLibros.getSelectedRow();

        String titulo = pedirTexto("Nuevo título:",
                tablaLibros.getValueAt(fila, 1).toString());
        if (titulo == null) return;

        String autor = pedirTexto("Nuevo autor:",
                tablaLibros.getValueAt(fila, 2).toString());
        if (autor == null) return;

        String isbn = pedirTexto("Nuevo ISBN:",
                tablaLibros.getValueAt(fila, 3).toString());
        if (isbn == null) return;

        String editorial = pedirTexto("Nueva editorial:",
                tablaLibros.getValueAt(fila, 4).toString());
        if (editorial == null) return;

        String textoStock = pedirTexto("Nuevo stock:",
                tablaLibros.getValueAt(fila, 5).toString());
        if (textoStock == null) return;

        String textoCategoria = pedirTexto("ID de categoría:",
                tablaLibros.getValueAt(fila, 6).toString());
        if (textoCategoria == null) return;

        if (titulo.trim().isEmpty() || autor.trim().isEmpty()
                || isbn.trim().isEmpty() || editorial.trim().isEmpty()
                || textoStock.trim().isEmpty()
                || textoCategoria.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios.");
            return;
        }

        try {
            int stock = Integer.parseInt(textoStock.trim());
            int categoria = Integer.parseInt(textoCategoria.trim());

            if (stock < 0) {
                JOptionPane.showMessageDialog(this,
                        "El stock no puede ser negativo.");
                return;
            }

            Libro libro = new Libro(id, titulo.trim(), autor.trim(),
                    isbn.trim(), editorial.trim(), stock, categoria);

            if (libroDAO.update(libro)) {
                JOptionPane.showMessageDialog(this, "Libro actualizado.");
                cargarLibros();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el libro. Verifica el ID de categoría.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El stock y el ID de categoría deben ser números enteros.");
        }
    }

    private void eliminarLibro() {
        int id = obtenerIdSeleccionado(tablaLibros);
        if (id == -1) return;

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el libro seleccionado?", "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (libroDAO.delete(id)) {
                JOptionPane.showMessageDialog(this, "Libro eliminado.");
                cargarLibros();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar. Puede tener préstamos asociados.");
            }
        }
    }

    private void registrarEstudiante() {
        try {
            String nombre = pedirTexto("Nombre:", "");
            if (nombre == null || nombre.trim().isEmpty()) return;

            String rut = pedirTexto("RUT:", "");
            if (rut == null) return;

            String curso = pedirTexto("Curso:", "");
            if (curso == null) return;

            String correo = pedirTexto("Correo:", "");
            if (correo == null) return;

            Estudiante estudiante = new Estudiante(0, nombre, rut, curso, correo);

            if (estudianteDAO.crear(estudiante)) {
                JOptionPane.showMessageDialog(this, "Estudiante registrado.");
                cargarEstudiantes();
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo registrar al estudiante.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar al estudiante.");
        }
    }

    private void modificarEstudiante() {
        int id = obtenerIdSeleccionado(tablaEstudiantes);
        if (id == -1) return;

        int fila = tablaEstudiantes.getSelectedRow();

        String nombre = pedirTexto("Nombre:",
                tablaEstudiantes.getValueAt(fila, 1).toString());
        if (nombre == null) return;

        String rut = pedirTexto("RUT:",
                tablaEstudiantes.getValueAt(fila, 2).toString());
        if (rut == null) return;

        String curso = pedirTexto("Curso:",
                tablaEstudiantes.getValueAt(fila, 3).toString());
        if (curso == null) return;

        String correo = pedirTexto("Correo:",
                tablaEstudiantes.getValueAt(fila, 4).toString());
        if (correo == null) return;

        if (nombre.trim().isEmpty() || rut.trim().isEmpty()
                || curso.trim().isEmpty() || correo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos son obligatorios.");
            return;
        }

        Estudiante estudiante = new Estudiante(id, nombre.trim(),
                rut.trim(), curso.trim(), correo.trim());

        if (estudianteDAO.actualizar(estudiante)) {
            JOptionPane.showMessageDialog(this, "Estudiante actualizado.");
            cargarEstudiantes();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el estudiante.");
        }
    }


    private void eliminarEstudiante() {
        int id = obtenerIdSeleccionado(tablaEstudiantes);
        if (id == -1) return;

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el estudiante seleccionado?", "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion == JOptionPane.YES_OPTION) {
            if (estudianteDAO.eliminar(id)) {
                JOptionPane.showMessageDialog(this, "Estudiante eliminado.");
                cargarEstudiantes();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar. Puede tener préstamos asociados.");
            }
        }
    }

    private void registrarPrestamo() {
        int idLibro = obtenerIdSeleccionado(tablaLibros);
        if (idLibro == -1) return;
        int filaSeleccionada = tablaLibros.getSelectedRow();
        int stockActual = Integer.parseInt(
                tablaLibros.getValueAt(filaSeleccionada, 5).toString()
        );

        if (stockActual <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Este libro no tiene ejemplares disponibles.",
                    "Stock agotado",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String textoId = pedirTexto("Ingresa el ID del estudiante:", "");
        if (textoId == null) return;

        try {
            int idEstudiante = Integer.parseInt(textoId);

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Registrar préstamo para estudiante " + idEstudiante
                            + " y libro " + idLibro + "?",
                    "Confirmar préstamo", JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) return;

            JOptionPane.showMessageDialog(this,
                    "El préstamo se está procesando en segundo plano.");

            PrestamoTask task = new PrestamoTask(
                    idEstudiante,
                    idLibro,
                    () -> {
                        JOptionPane.showMessageDialog(this,
                                "Préstamo registrado. Stock actualizado.");
                        cargarLibros();
                        cargarPrestamosActivos();
                    },
                    () -> JOptionPane.showMessageDialog(this,
                            "No se pudo registrar el préstamo. "
                                    + "Verifica el estudiante, el libro y el stock.",
                            "Préstamo no realizado", JOptionPane.WARNING_MESSAGE)
            );

            new Thread(task).start();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.");
        }
    }

    private void registrarDevolucion() {
        int idPrestamo = obtenerIdSeleccionado(tablaPrestamos);
        if (idPrestamo == -1) return;

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Registrar la devolución del préstamo " + idPrestamo + "?",
                "Confirmar devolución", JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) return;

        if (prestamoDAO.registrarDevolucion(idPrestamo)) {
            JOptionPane.showMessageDialog(this,
                    "Devolución registrada y stock restituido.");
            cargarPrestamosActivos();
            cargarLibros();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar. Comprueba que el préstamo siga pendiente.");
        }
    }

    private void mostrarLibrosMasPrestados() {
        List<String[]> datos = reporteDAO.librosMasPrestados();

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todavía no hay préstamos registrados.");
            return;
        }

        StringBuilder texto = new StringBuilder("LIBROS MÁS PRESTADOS\n\n");
        for (String[] fila : datos) {
            texto.append(fila[0]).append(" — ")
                    .append(fila[1]).append(" préstamo(s)\n");
        }

        JOptionPane.showMessageDialog(this, texto.toString());
    }

    private void mostrarHistorial() {
        String textoId = pedirTexto("ID del estudiante:", "");
        if (textoId == null) return;

        try {
            int idEstudiante = Integer.parseInt(textoId);
            List<String[]> datos = reporteDAO.historialEstudiante(idEstudiante);

            if (datos.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay historial para ese estudiante.");
                return;
            }

            StringBuilder texto = new StringBuilder("HISTORIAL DEL ESTUDIANTE\n\n");
            for (String[] fila : datos) {
                texto.append("Préstamo ").append(fila[0])
                        .append(" | Libro: ").append(fila[1])
                        .append(" | Fecha: ").append(fila[2])
                        .append(" | Vencimiento: ").append(fila[3])
                        .append(" | Estado: ").append(fila[4]).append("\n");
            }

            JOptionPane.showMessageDialog(this, texto.toString());

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "El ID debe ser un número.");
        }
    }

    private void mostrarPrestamosActivos() {
        List<String[]> datos = reporteDAO.listarPrestamosActivos();

        if (datos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay libros actualmente en préstamo.");
            return;
        }

        StringBuilder texto = new StringBuilder("PRÉSTAMOS VIGENTES\n\n");
        for (String[] fila : datos) {
            texto.append("Préstamo ").append(fila[0])
                    .append(" | Estudiante: ").append(fila[1])
                    .append(" | Libro: ").append(fila[2])
                    .append(" | Vencimiento: ").append(fila[4]).append("\n");
        }

        JOptionPane.showMessageDialog(this, texto.toString());
    }

    private void cargarCategorias() {
        modeloCategorias.setRowCount(0);

        for (Categoria categoria : categoriaDAO.listarTodos()) {
            modeloCategorias.addRow(new Object[]{
                    categoria.getId(),
                    categoria.getNombre()
            });
        }
    }

    private void registrarCategoria() {
        String nombre = pedirTexto("Nombre de la categoría:", "");

        if (nombre == null) return;

        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre de la categoría es obligatorio.");
            return;
        }

        Categoria categoria = new Categoria(0, nombre.trim());

        if (categoriaDAO.crear(categoria)) {
            JOptionPane.showMessageDialog(this,
                    "Categoría registrada correctamente.");
            cargarCategorias();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo registrar. Verifica que el nombre no exista.");
        }
    }

    private void modificarCategoria() {
        int id = obtenerIdSeleccionado(tablaCategorias);
        if (id == -1) return;

        int fila = tablaCategorias.getSelectedRow();

        String nombre = pedirTexto("Nuevo nombre:",
                tablaCategorias.getValueAt(fila, 1).toString());

        if (nombre == null) return;

        if (nombre.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El nombre de la categoría es obligatorio.");
            return;
        }

        Categoria categoria = new Categoria(id, nombre.trim());

        if (categoriaDAO.actualizar(categoria)) {
            JOptionPane.showMessageDialog(this,
                    "Categoría actualizada correctamente.");
            cargarCategorias();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar. Verifica que el nombre no exista.");
        }
    }

    private void eliminarCategoria() {
        int id = obtenerIdSeleccionado(tablaCategorias);
        if (id == -1) return;

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la categoría seleccionada?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirmacion != JOptionPane.YES_OPTION) return;

        if (categoriaDAO.eliminar(id)) {
            JOptionPane.showMessageDialog(this,
                    "Categoría eliminada correctamente.");
            cargarCategorias();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar. Puede tener libros asociados.");
        }
    }

}
