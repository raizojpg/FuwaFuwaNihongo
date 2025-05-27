package jdbc.dao;

import entities.Reading;
import java.io.IOException;
import java.sql.*;
import jdbc.config.Config;
import structures.Phonetic;

public class ReadingDAO {

     public static void main(String[] args) throws Exception {
        ReadingDAO dao = new ReadingDAO();

        Phonetic onyomi = Phonetic.createPhonetic("ニチ-nichi;ジツ-jitsu");
        Phonetic kunyomi = Phonetic.createPhonetic("ひ-hi;か-ka");

        Reading reading = new Reading(onyomi, kunyomi);
        int id = dao.insertAndGetId(reading);
        System.out.println("Inserted Reading with ID: " + id);

        Reading retrieved = dao.getById(id);
        System.out.println("Retrieved Reading:");
        System.out.println("Onyomi: " + retrieved.getOnyomi());
        System.out.println("Kunyomi: " + retrieved.getKunyomi());
    }

    public Reading getById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM reading WHERE id = ?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String onyomiStr = rs.getString("onyomi");
                    String kunyomiStr = rs.getString("kunyomi");
                    Phonetic onyomi = Phonetic.createPhonetic(onyomiStr);
                    Phonetic kunyomi = Phonetic.createPhonetic(kunyomiStr);
                    return new Reading(onyomi, kunyomi);
                }
            }
        }
        return null;
    }

    public int insertAndGetId(Reading r) throws SQLException, IOException {
        String sql = "INSERT INTO reading (onyomi, kunyomi) VALUES (?, ?)";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, r.getOnyomi().toDatabaseString());
            ps.setString(2, r.getKunyomi().toDatabaseString());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert Reading");
    }
}