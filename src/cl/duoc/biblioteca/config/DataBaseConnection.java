package cl.duoc.biblioteca.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static DataBaseConnection instance;
    private Connection connection;
    private static final String URL = "jdbc:mysql://localhost:3306/biblioteca_escolar?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DataBaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: No se encontró la librería del conector MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error de credenciales o servidor MySQL apagado: " + e.getMessage());
        }
    }

    public static synchronized DataBaseConnection getInstance() {
        try {
            if (instance == null || instance.getConnection().isClosed()) {
                instance = new DataBaseConnection();
            }
        } catch (SQLException e) {
            instance = new DataBaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}

