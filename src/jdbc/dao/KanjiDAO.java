package jdbc.dao;

import entities.Kanji;
import entities.Reading;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jdbc.config.Config;
import structures.Phonetic;

public class KanjiDAO {
    private final ReadingDAO readingDAO = new ReadingDAO();

    public static void main(String[] args) throws Exception {
        KanjiDAO dao = new KanjiDAO();

        // 1. INSERT
        Phonetic onyomi = Phonetic.createPhonetic("ニチ-nichi;ジツ-jitsu");
        Phonetic kunyomi = Phonetic.createPhonetic("ひ-hi;か-ka");
        Reading reading = new Reading(onyomi, kunyomi);

        Kanji kanji = new Kanji.Builder()
            .setTerm("日")
            .setMeaning("sun; day")
            .setExplanation("Represents the sun or a day")
            .setReading(reading)
            .setLevel(1)
            .setFrequency(100)
            .build();
        dao.insert(kanji);
        System.out.println("Inserted Kanji.");

        // 2. GET ALL
        List<Kanji> kanjiList = dao.getAll();
        System.out.println("All Kanji:");
        for (Kanji k : kanjiList) {
            System.out.println(
                k.getTerm() + " - " +
                k.getMeaning() + " - " +
                (k.getReading() != null ?
                    "Onyomi: " + k.getReading().getOnyomi() + ", Kunyomi: " + k.getReading().getKunyomi()
                    : "No Reading"
                )
            );
        }

        // 3. GET BY ID 
        if (!kanjiList.isEmpty()) {
            int id = 1;
            Kanji found = dao.getById(id);
            System.out.println("Get by ID (" + id + "): " +
                (found != null ? found.getTerm() + " - " + found.getMeaning() : "Not found"));
            
            // 4. UPDATE
            Kanji updatedKanji = new Kanji.Builder()
                .setTerm("日")
                .setMeaning("sun; day (updated)")
                .setExplanation("Represents the sun or a day (updated)")
                .setReading(reading)
                .setLevel(2)
                .setFrequency(200)
                .build();
            dao.update(updatedKanji, id);
            System.out.println("Updated Kanji with ID " + id);

            // 5. DELETE
            dao.delete(id);
            System.out.println("Deleted Kanji with ID " + id);

            // Confirm deletion
            Kanji afterDelete = dao.getById(id);
            System.out.println("After delete, getById(" + id + "): " + (afterDelete == null ? "Not found" : "Still exists!"));
        }
    }

    public List<Kanji> getAll() throws SQLException, IOException {
        List<Kanji> list = new ArrayList<>();
        try (Connection conn = Config.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM kanji")) {
            while (rs.next()) {
                int readingId = rs.getInt("reading_id");
                Reading reading = readingDAO.getById(readingId);

                Kanji k = new Kanji.Builder()
                    .setTerm(rs.getString("term"))
                    .setMeaning(rs.getString("meaning"))
                    .setExplanation(rs.getString("explanation"))
                    .setReading(reading)
                    .setLevel(rs.getInt("level"))
                    .setFrequency(rs.getInt("frequency"))
                    .build();
                list.add(k);
            }
        }
        return list;
    }

    public Kanji getById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM kanji WHERE id = ?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int readingId = rs.getInt("reading_id");
                    Reading reading = readingDAO.getById(readingId);
                    return new Kanji.Builder()
                        .setTerm(rs.getString("term"))
                        .setMeaning(rs.getString("meaning"))
                        .setExplanation(rs.getString("explanation"))
                        .setReading(reading)
                        .setLevel(rs.getInt("level"))
                        .setFrequency(rs.getInt("frequency"))
                        .build();
                }
            }
        }
        return null;
    }

    public void insert(Kanji k) throws SQLException, IOException {
        Reading reading = k.getReading();
        int readingId = readingDAO.insertAndGetId(reading);

        String sql = "INSERT INTO kanji (term, meaning, explanation, level, frequency, reading_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k.getTerm());
            ps.setString(2, k.getMeaning());
            ps.setString(3, k.getExplanation());
            ps.setInt(4, k.getLevel());
            ps.setInt(5, k.getFrequency());
            ps.setInt(6, readingId);
            ps.executeUpdate();
        }
    }

    public void update(Kanji k, int id) throws SQLException, IOException {
            int readingId;
            try {
                readingId = getReadingIdByKanjiId(id);
            } catch (Exception e) {
                throw new IOException("Failed to get old reading ID", e);
            }

            Reading reading = k.getReading();
            readingDAO.update(reading, readingId);

            String sql = "UPDATE kanji SET term=?, meaning=?, explanation=?, level=?, frequency=?, reading_id=? WHERE id=?";
            try (Connection conn = Config.connect();
                PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, k.getTerm());
                ps.setString(2, k.getMeaning());
                ps.setString(3, k.getExplanation());
                ps.setInt(4, k.getLevel());
                ps.setInt(5, k.getFrequency());
                ps.setInt(6, readingId);
                ps.setInt(7, id);
                ps.executeUpdate();
            }

    }

    public void delete(int id) throws SQLException, IOException {
        String sql = "DELETE FROM kanji WHERE id = ?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int getIdByTerm(String term) throws Exception {
        String sql = "SELECT id FROM kanji WHERE term = ?";
        try (Connection conn = Config.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, term);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new Exception("Kanji with term '" + term + "' not found!");
    }

    public int getReadingIdByKanjiId(int kanjiId) throws Exception {
        String sql = "SELECT reading_id FROM kanji WHERE id = ?";
        try (Connection conn = Config.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, kanjiId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("reading_id");
                }
            }
        }
        return -1;
    }

}