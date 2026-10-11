package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DataBaseConnection;
import cl.duoc.biblioteca.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public boolean crear(Categoria categoria) {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";

        try (Connection conexion =
                     DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, categoria.getNombre());

            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        categoria.setId(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al crear categoría: " + e.getMessage());
        }

        return false;
    }

    public List<Categoria> listarTodos() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre FROM categorias ORDER BY id";

        try (Connection conexion =
                     DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categorias.add(new Categoria(
                        rs.getInt("id"),
                        rs.getString("nombre")
                ));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar categorías: " + e.getMessage());
        }

        return categorias;
    }

    public boolean actualizar(Categoria categoria) {
        String sql = "UPDATE categorias SET nombre = ? WHERE id = ?";

        try (Connection conexion =
                     DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, categoria.getNombre());
            ps.setInt(2, categoria.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar categoría: " + e.getMessage());
        }

        return false;
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?";

        try (Connection conexion =
                     DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar categoría: " + e.getMessage());
        }

        return false;
    }
}