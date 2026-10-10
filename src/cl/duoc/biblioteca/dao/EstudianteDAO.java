package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DataBaseConnection;
import cl.duoc.biblioteca.model.Estudiante;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    // 1. LISTAR todos los estudiantes
    public List<Estudiante> listarTodos() {
        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = "SELECT id, nombre, rut, curso, correo "
                + "FROM estudiantes ORDER BY id";

        try (Connection conexion =
                     DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Estudiante estudiante = new Estudiante(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("rut"),
                        rs.getString("curso"),
                        rs.getString("correo")
                );

                estudiantes.add(estudiante);
            }

        } catch (SQLException e) {
            System.out.println(
                    "Error al listar estudiantes: " + e.getMessage()
            );
        }

        return estudiantes;
    }

    // 2. CREAR un estudiante
    public boolean crear(Estudiante estudiante) {
        String sql = "INSERT INTO estudiantes "
                + "(nombre, rut, curso, correo) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection conexion =
                     DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al crear estudiante: " + e.getMessage()
            );
            return false;
        }
    }

    // 3. ACTUALIZAR un estudiante
    public boolean actualizar(Estudiante estudiante) {
        String sql = "UPDATE estudiantes "
                + "SET nombre = ?, rut = ?, curso = ?, correo = ? "
                + "WHERE id = ?";

        try (Connection conexion =
                     DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, estudiante.getNombre());
            ps.setString(2, estudiante.getRut());
            ps.setString(3, estudiante.getCurso());
            ps.setString(4, estudiante.getCorreo());
            ps.setInt(5, estudiante.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al actualizar estudiante: " + e.getMessage()
            );
            return false;
        }
    }

    // 4. ELIMINAR un estudiante
    public boolean eliminar(int id) {
        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (Connection conexion =
                     DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println(
                    "Error al eliminar estudiante: " + e.getMessage()
            );
            return false;
        }
    }
}