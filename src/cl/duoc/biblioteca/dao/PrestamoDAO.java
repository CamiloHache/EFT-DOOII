package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DataBaseConnection;
import java.sql.*;

public class PrestamoDAO {

    // Registrar un prestamo y descontar stock.
    public synchronized boolean registrarPrestamoSincronizado(
            int idEstudiante, int idLibro) {

        String sqlVerificarStock =
                "SELECT stock FROM libros WHERE id = ? FOR UPDATE";

        String sqlInsertPrestamo =
                "INSERT INTO prestamos "
                        + "(id_estudiante, id_libro, fecha_prestamo, "
                        + "fecha_devolucion) "
                        + "VALUES (?, ?, CURRENT_DATE, "
                        + "DATE_ADD(CURRENT_DATE, INTERVAL 7 DAY))";

        String sqlDescontarStock =
                "UPDATE libros SET stock = stock - 1 "
                        + "WHERE id = ? AND stock > 0";

        Connection con = null;

        try {
            con = DataBaseConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            // 1. Verificar que el libro exista y tenga stock.
            try (PreparedStatement ps =
                         con.prepareStatement(sqlVerificarStock)) {

                ps.setInt(1, idLibro);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next() || rs.getInt("stock") <= 0) {
                        con.rollback();
                        return false;
                    }
                }
            }

            // 2. Registrar el prestamo.
            try (PreparedStatement ps =
                         con.prepareStatement(sqlInsertPrestamo)) {

                ps.setInt(1, idEstudiante);
                ps.setInt(2, idLibro);
                ps.executeUpdate();
            }

            // 3. Descontar una unidad del stock.
            try (PreparedStatement ps =
                         con.prepareStatement(sqlDescontarStock)) {

                ps.setInt(1, idLibro);

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            System.out.println(
                    "Error en transaccion de prestamo: " + e.getMessage()
            );
            return false;

        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Registrar la devolucion de un prestamo.
    public synchronized boolean registrarDevolucion(int idPrestamo) {

        Connection con = null;

        try {
            con = DataBaseConnection.getInstance().getConnection();
            con.setAutoCommit(false);

            int idLibro;
            boolean devuelto;

            // 1. Buscar el prestamo.
            String sqlBuscar =
                    "SELECT id_libro, devuelto "
                            + "FROM prestamos WHERE id = ? FOR UPDATE";

            try (PreparedStatement ps = con.prepareStatement(sqlBuscar)) {
                ps.setInt(1, idPrestamo);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        con.rollback();
                        return false;
                    }

                    idLibro = rs.getInt("id_libro");
                    devuelto = rs.getBoolean("devuelto");
                }
            }

            // 2. Impedir una segunda devolucion.
            if (devuelto) {
                con.rollback();
                return false;
            }

            // 3. Marcar el prestamo como devuelto.
            String sqlDevolver =
                    "UPDATE prestamos SET devuelto = 1 "
                            + "WHERE id = ? AND devuelto = 0";

            try (PreparedStatement ps = con.prepareStatement(sqlDevolver)) {
                ps.setInt(1, idPrestamo);

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            // 4. Aumentar el stock del libro.
            String sqlStock =
                    "UPDATE libros SET stock = stock + 1 WHERE id = ?";

            try (PreparedStatement ps = con.prepareStatement(sqlStock)) {
                ps.setInt(1, idLibro);

                if (ps.executeUpdate() != 1) {
                    con.rollback();
                    return false;
                }
            }

            // 5. Confirmar la transaccion.
            con.commit();
            return true;

        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            System.out.println(
                    "Error al registrar la devolucion: " + e.getMessage()
            );
            return false;

        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}