
package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.config.DataBaseConnection;
import cl.duoc.biblioteca.model.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    // CREATE: registrar un libro nuevo.
    public boolean create(Libro libro) {
        String sql = "INSERT INTO libros " +
                "(titulo, autor, isbn, editorial, stock, id_categoria) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(
                     sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        libro.setId(rs.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error al registrar libro: " + e.getMessage());
        }

        return false;
    }

    // READ: obtener todos los libros registrados.
    public List<Libro> readAll() {
        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT id, titulo, autor, isbn, editorial, " +
                "stock, id_categoria FROM libros";

        try (Connection con = DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("id"),
                        rs.getString("titulo"),
                        rs.getString("autor"),
                        rs.getString("isbn"),
                        rs.getString("editorial"),
                        rs.getInt("stock"),
                        rs.getInt("id_categoria")
                );

                libros.add(libro);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar libros: " + e.getMessage());
        }

        return libros;
    }

    // UPDATE: modificar los datos de un libro existente.
    public boolean update(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, autor = ?, " +
                "isbn = ?, editorial = ?, stock = ?, id_categoria = ? " +
                "WHERE id = ?";

        try (Connection con = DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, libro.getTitulo());
            ps.setString(2, libro.getAutor());
            ps.setString(3, libro.getIsbn());
            ps.setString(4, libro.getEditorial());
            ps.setInt(5, libro.getStock());
            ps.setInt(6, libro.getIdCategoria());
            ps.setInt(7, libro.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar libro: " + e.getMessage());
        }

        return false;
    }

    // DELETE: eliminar un libro por su ID.
    public boolean delete(int id) {
        String sql = "DELETE FROM libros WHERE id = ?";

        try (Connection con = DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar libro: " + e.getMessage());
        }

        return false;
    }

    // Mantiene la compatibilidad con la ventana que ya utiliza listarTodos().
    public List<Libro> listarTodos() {
        return readAll();
    }
}
