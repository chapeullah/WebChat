import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DB_PATH = "data/database.db";
    private Connection connection;

    public DatabaseManager() {
        
    }

    public void connect() {
        try {
            try { 
                Class.forName("org.sqlite.JDBC");
            } catch (Exception e){
                System.err.println(e.getMessage());
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            System.out.println("Database connected.");
            executeSchema();
        } catch (SQLException e) {
            System.err.println("Failed to connect database: " + e.getMessage());
        }
        return;
    }

    private void executeSchema() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    passwordHash TEXT NOT NULL
                );
            """);
            
            statement.executeUpdate("""
                CREATE TABLE IF NOT EXISTS messages (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    sender TEXT NOT NULL,
                    receiver TEXT NOT NULL,
                    text TEXT NOT NULL,
                    timestamp TEXT NOT NULL
                );
            """);

            System.out.println("Tables initialized.");
        } catch (SQLException e) {
            System.err.println("Failed to initialize schema: " + e.getMessage());
        }
    }
}
