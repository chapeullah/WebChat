import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;

public class DatabaseManager {

    private static final String DB_PATH = "data/database.db";
    private Connection connection;

    public DatabaseManager() {
        
    }

    public void connect() {
        Enumeration<java.sql.Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            System.out.println(WebChat.timeNow() + " Loaded driver: " + drivers.nextElement().getClass().getName());
        }
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
            System.out.println(WebChat.timeNow() + " Database connected.");
            executeSchema();
        } catch (SQLException e) {
            System.err.println(WebChat.timeNow() + " Failed to connect database: " + e.getMessage());
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

            System.out.println(WebChat.timeNow() + " Tables initialized.");
        } catch (SQLException e) {
            System.err.println(WebChat.timeNow() + " Failed to initialize schema: " + e.getMessage());
        }
    }
}
