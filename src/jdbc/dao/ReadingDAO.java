package jdbc.dao;

import entities.Reading;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jdbc.config.Config;
import structures.Phonetic;

public class ReadingDAO {

    public static void main(String[] args) throws Exception {
        ReadingDAO dao = new ReadingDAO();

        // 1. INSERT
        Phonetic onyomi = Phonetic.createPhonetic("ニチ-nichi;ジツ-jitsu");
        Phonetic kunyomi = Phonetic.createPhonetic("ひ-hi;か-ka");
        Reading reading = new Reading(onyomi, kunyomi);
        int id = dao.insertAndGetId(reading);
        System.out.println("Inserted Reading with ID: " + id);

        // 2. GET ALL
        List<Reading> readings = dao.getAll();
        System.out.println("All Readings:");
        for (Reading r : readings) {
            System.out.println(" - Onyomi: " + r.getOnyomi() + " - Kunyomi: " + r.getKunyomi());
        }

        // 3. GET BY ID
        Reading found = dao.getById(id);
        System.out.println("Get by ID (" + id + "): " +
            (found != null ? "Onyomi: " + found.getOnyomi() + ", Kunyomi: " + found.getKunyomi() : "Not found"));

        // 4. UPDATE
        Reading updatedReading = new Reading(
            Phonetic.createPhonetic("ニチ-nichi;ジツ-jitsu;ニッ-nitsu"),
            Phonetic.createPhonetic("ひ-hi;か-ka;び-bi")
        );
        dao.update(updatedReading, id);
        System.out.println("Updated Reading with ID " + id);

        // 5. DELETE
        dao.delete(id);
        System.out.println("Deleted Reading with ID " + id);

        // Confirm deletion
        Reading afterDelete = dao.getById(id);
        System.out.println("After delete, getById(" + id + "): " + (afterDelete == null ? "Not found" : "Still exists!"));
    }

    public List<Reading> getAll() throws SQLException, IOException {
        List<Reading> list = new ArrayList<>();
        String sql = "SELECT * FROM reading";
        try (Connection conn = Config.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String onyomiStr = rs.getString("onyomi");
                String kunyomiStr = rs.getString("kunyomi");
                Phonetic onyomi = Phonetic.createPhonetic(onyomiStr);
                Phonetic kunyomi = Phonetic.createPhonetic(kunyomiStr);
                Reading reading = new Reading(onyomi, kunyomi);
                list.add(reading);
            }
        }
        return list;
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
                    Reading reading = new Reading(onyomi, kunyomi);
                    return reading;
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

    public void update(Reading r, int id) throws SQLException, IOException {
        String sql = "UPDATE reading SET onyomi=?, kunyomi=? WHERE id=?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.getOnyomi().toDatabaseString());
            ps.setString(2, r.getKunyomi().toDatabaseString());
            ps.setInt(3, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException, IOException {
        String sql = "DELETE FROM reading WHERE id = ?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}