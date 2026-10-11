package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    public List<String[]> listarPrestamosActivos() {
        List<String[]> lista = new ArrayList<>();

        String sql = "SELECT p.id, e.nombre, l.titulo, " +
                "p.fecha_prestamo, p.fecha_devolucion, " +
                "CASE " +
                "WHEN p.fecha_devolucion < CURDATE() " +
                "THEN CONCAT('ATRASADO: ', " +
                "DATEDIFF(CURDATE(), p.fecha_devolucion), ' días') " +
                "ELSE 'VIGENTE' " +
                "END AS estado_prestamo " +
                "FROM prestamos p " +
                "INNER JOIN estudiantes e ON p.id_estudiante = e.id " +
                "INNER JOIN libros l ON p.id_libro = l.id " +
                "WHERE p.devuelto = 0 " +
                "ORDER BY p.fecha_devolucion ASC";

        try (
                Connection con = DataBaseConnection.getInstance().getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("titulo"),
                        rs.getString("fecha_prestamo"),
                        rs.getString("fecha_devolucion"),
                        rs.getString("estado_prestamo")
                });
            }
        } catch (SQLException e) {
            System.err.println(
                    "Error al listar préstamos activos: " + e.getMessage()
            );
        }

        return lista;
    }

    public List<String[]> librosMasPrestados() {
        List<String[]> lista = new ArrayList<>();

        String sql = "SELECT l.titulo, COUNT(p.id) AS cantidad "
                + "FROM prestamos p "
                + "INNER JOIN libros l ON p.id_libro = l.id "
                + "GROUP BY l.id, l.titulo "
                + "ORDER BY cantidad DESC LIMIT 10";

        try (Connection con = DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new String[]{
                        rs.getString("titulo"),
                        rs.getString("cantidad")
                });
            }

        } catch (SQLException e) {
            System.err.println("Error en reporte de libros: " + e.getMessage());
        }

        return lista;
    }

    public List<String[]> historialEstudiante(int idEstudiante) {
        List<String[]> lista = new ArrayList<>();

        String sql = "SELECT p.id, l.titulo, p.fecha_prestamo, "
                + "p.fecha_devolucion, p.devuelto "
                + "FROM prestamos p "
                + "INNER JOIN libros l ON p.id_libro = l.id "
                + "WHERE p.id_estudiante = ? ORDER BY p.id DESC";

        try (Connection con = DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idEstudiante);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String estado = rs.getBoolean("devuelto")
                            ? "Devuelto" : "Pendiente";

                    lista.add(new String[]{
                            rs.getString("id"),
                            rs.getString("titulo"),
                            rs.getString("fecha_prestamo"),
                            rs.getString("fecha_devolucion"),
                            estado
                    });
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en historial: " + e.getMessage());
        }

        return lista;
    }
}