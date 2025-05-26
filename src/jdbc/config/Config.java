package jdbc.config;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Config {
    private static final String PROPERTIES_FILE = "/jdbc/config/db.properties";

    private static Properties loadProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = Config.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new IOException("Properties file not found: " + PROPERTIES_FILE);
            }
            props.load(input);
        }
        return props;
    }

    public static Connection connect() throws SQLException, IOException {
        Properties props = loadProperties();
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    public static void main(String[] args) {
        try (Connection conn = connect()) {
            System.out.println("Connected to MySQL!");

            try (java.sql.Statement stmt = conn.createStatement();
                 java.sql.ResultSet rs = stmt.executeQuery("SELECT * FROM test")) {

                while (rs.next()) {
                    int id = rs.getInt(1); 
                    String name = rs.getString(2); 
                    System.out.println("id: " + id + ", name: " + name);
                }
            }

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}