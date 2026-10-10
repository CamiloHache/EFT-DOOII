package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DataBaseConnection;
import java.sql.*;

public class PrestamoDAO {

    // El modificador 'synchronized' previene condiciones de carrera
    public synchronized boolean registrarPrestamoSincronizado(int idEstudiante, int idLibro) {
        String sqlVerificarStock = "SELECT stock FROM libros WHERE id = ?";
        String sqlInsertPrestamo = "INSERT INTO prestamos (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion) VALUES (?, ?, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY))";
        String sqlDescontarStock = "UPDATE libros SET stock = stock - 1 WHERE id = ?";

        Connection con = null;
        try {
            con = DataBaseConnection.getInstance().getConnection();
            con.setAutoCommit(false); // Iniciamos transacción

            // 1. Verificar stock
            try (PreparedStatement stmStock = con.prepareStatement(sqlVerificarStock)) {
                stmStock.setInt(1, idLibro);
                ResultSet rs = stmStock.executeQuery();
                if (rs.next()) {
                    if (rs.getInt("stock") <= 0) {
                        con.rollback(); // No hay stock, abortar
                        return false;
                    }
                } else {
                    con.rollback();
                    return false;
                }
            }

            // 2. Registrar el préstamo
            try (PreparedStatement stmInsert = con.prepareStatement(sqlInsertPrestamo)) {
                stmInsert.setInt(1, idEstudiante);
                stmInsert.setInt(2, idLibro);
                stmInsert.executeUpdate();
            }

            // 3. Descontar stock
            try (PreparedStatement stmUpdate = con.prepareStatement(sqlDescontarStock)) {
                stmUpdate.setInt(1, idLibro);
                stmUpdate.executeUpdate();
            }

            con.commit(); // Confirmamos los cambios en la BD
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            System.out.println("Error en transacción de préstamo: " + e.getMessage());
            return false;
        } finally {
            if (con != null) {
                try { con.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
}