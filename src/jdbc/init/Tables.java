package jdbc.init;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import jdbc.config.Config;

public class Tables {
    public static void main(String[] args) {
        try (Connection conn = Config.connect(); Statement stmt = conn.createStatement()) {
            createAllTables(stmt);
            System.out.println("Tables created successfully!");
            //insertExamples(stmt);
            printAllTables();
            clearAllTables();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void createAllTables(Statement stmt) throws SQLException {
        // Reading table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS reading (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "onyomi TEXT," +    // Serialized List<Pair<String, String>>
            "kunyomi TEXT" +    // Serialized List<Pair<String, String>>
            ")"
        );

        // Kanji table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS kanji (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "term VARCHAR(10) NOT NULL," +
            "meaning VARCHAR(255) NOT NULL," +
            "explanation TEXT," +
            "level INT," +
            "frequency INT," +
            "reading_id INT," +
            "FOREIGN KEY (reading_id) REFERENCES reading(id)" +
            ")"
        );

        // Word table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS word (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "term VARCHAR(50) NOT NULL," +
            "meaning VARCHAR(255) NOT NULL," +
            "explanation TEXT," +
            "difficulty FLOAT," +
            "frequency INT," +
            "partOfSpeech VARCHAR(50)" +
            ")"
        );

        // Phrase table
        stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS phrase (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "term VARCHAR(255) NOT NULL," +
            "meaning VARCHAR(255) NOT NULL," +
            "explanation TEXT," +
            "reading VARCHAR(255)," +    
            "difficulty FLOAT" +        
            ")"
        );
    }

    private static void insertExamples(Statement stmt) throws SQLException {
        stmt.executeUpdate(
            "INSERT INTO reading (onyomi, kunyomi) VALUES (" +
            "'オン-meaning1;ヨミ-meaning2', 'くん-meaningA;よみ-meaningB')"
        );

        stmt.executeUpdate(
            "INSERT INTO kanji (term, meaning, explanation, level, frequency, reading_id) VALUES (" +
            "'日', 'sun; day', 'Represents the sun or a day', 1, 100, 1)"
        );

        stmt.executeUpdate(
            "INSERT INTO word (term, meaning, explanation, difficulty, frequency, partOfSpeech) VALUES (" +
            "'学生', 'student', 'A person who studies', 0.5, 200, 'noun')"
        );

        stmt.executeUpdate(
            "INSERT INTO phrase (term, meaning, explanation, reading, difficulty) VALUES (" +
            "'おはようございます', 'Good morning', 'Common greeting in the morning', 'おはよう', 0.2)"
        );
    }

    public static void printAllTables() {
        try (Connection conn = Config.connect(); Statement stmt = conn.createStatement()) {
            String[] tables = {"reading", "kanji", "word", "phrase"};
            for (String table : tables) {
                System.out.println("\nContents of table: " + table);
                var rs = stmt.executeQuery("SELECT * FROM " + table);
                int colCount = rs.getMetaData().getColumnCount();
                while (rs.next()) {
                    for (int i = 1; i <= colCount; i++) {
                        System.out.print(rs.getMetaData().getColumnName(i) + "=" + rs.getString(i) + "  ");
                    }
                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Error printing all tables: " + e.getMessage());
        }
    }

    public static void clearAllTables() {
        try (Connection conn = Config.connect(); Statement stmt = conn.createStatement()) {
            // Disable foreign key checks to allow truncating tables with dependencies
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("TRUNCATE TABLE kanji");
            stmt.executeUpdate("TRUNCATE TABLE word");
            stmt.executeUpdate("TRUNCATE TABLE phrase");
            stmt.executeUpdate("TRUNCATE TABLE reading");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            System.out.println("All tables were cleared");
        } catch (Exception e) {
            System.out.println("Error clearing all tables: " + e.getMessage());
        }
    }

}