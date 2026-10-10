package cl.duoc.biblioteca.dao;

import cl.duoc.biblioteca.model.Categoria;
import cl.duoc.biblioteca.config.DataBaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> listarTodos() {
        List<Categoria> categorias = new ArrayList<>();

        String sql = "SELECT id, nombre FROM categorias ORDER BY id";

        try (Connection conexion =
                     DataBaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id"),
                        rs.getString("nombre")
                );

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }

        return categorias;
    }

}
