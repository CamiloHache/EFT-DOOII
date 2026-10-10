package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.dao.PrestamoDAO;
import javax.swing.SwingUtilities;

public class PrestamoTask implements Runnable {
    private int idEstudiante;
    private int idLibro;
    private Runnable onSuccess;
    private Runnable onError;

    public PrestamoTask(int idEstudiante, int idLibro, Runnable onSuccess, Runnable onError) {
        this.idEstudiante = idEstudiante;
        this.idLibro = idLibro;
        this.onSuccess = onSuccess;
        this.onError = onError;
    }

    @Override
    public void run() {
        PrestamoDAO dao = new PrestamoDAO();
        boolean exito = dao.registrarPrestamoSincronizado(idEstudiante, idLibro);

        // Volvemos al hilo de la interfaz gráfica para mostrar el resultado
        SwingUtilities.invokeLater(() -> {
            if (exito) {
                if (onSuccess != null) onSuccess.run();
            } else {
                if (onError != null) onError.run();
            }
        });
    }
}