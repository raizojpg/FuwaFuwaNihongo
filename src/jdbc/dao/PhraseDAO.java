package jdbc.dao;

import entities.Phrase;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jdbc.config.Config;

public class PhraseDAO {

    public static void main(String[] args) throws Exception {
        PhraseDAO dao = new PhraseDAO();

        // 1. INSERT
        Phrase phrase = new Phrase.Builder()
            .setTerm("おはようございます")
            .setMeaning("Good morning")
            .setExplanation("Common greeting in the morning")
            .setReading("おはよう")
            .setDifficulty(0.2f)
            .build();
        dao.insert(phrase);
        System.out.println("Inserted Phrase.");

        // 2. GET ALL
        List<Phrase> phrases = dao.getAll();
        System.out.println("All Phrases:");
        for (Phrase p : phrases) {
            System.out.println(
                p.getTerm() + " - " +
                p.getMeaning() + " - " +
                p.getReading() + " - " +
                p.getDifficulty()
            );
        }

        // 3. GET BY ID 
        int id = 1;
        Phrase found = dao.getById(id);
        System.out.println("Get by ID (" + id + "): " +
            (found != null ? found.getTerm() + " - " + found.getMeaning() : "Not found"));

        // 4. UPDATE
        Phrase updatedPhrase = new Phrase.Builder()
            .setTerm("おはようございます")
            .setMeaning("Good morning (updated)")
            .setExplanation("Updated explanation")
            .setReading("おはよう")
            .setDifficulty(0.5f)
            .build();
        dao.update(updatedPhrase, id);
        System.out.println("Updated Phrase with ID " + id);

        // 5. DELETE
        dao.delete(id);
        System.out.println("Deleted Phrase with ID " + id);

        // Confirm deletion
        Phrase afterDelete = dao.getById(id);
        System.out.println("After delete, getById(" + id + "): " + (afterDelete == null ? "Not found" : "Still exists!"));
    }

    public List<Phrase> getAll() throws SQLException, IOException {
        List<Phrase> list = new ArrayList<>();
        try (Connection conn = Config.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM phrase")) {
            while (rs.next()) {
                Phrase p = new Phrase.Builder()
                    .setTerm(rs.getString("term"))
                    .setMeaning(rs.getString("meaning"))
                    .setExplanation(rs.getString("explanation"))
                    .setReading(rs.getString("reading"))
                    .setDifficulty(rs.getFloat("difficulty"))
                    .build();
                list.add(p);
            }
        }
        return list;
    }

    public Phrase getById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM phrase WHERE id = ?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Phrase.Builder()
                        .setTerm(rs.getString("term"))
                        .setMeaning(rs.getString("meaning"))
                        .setExplanation(rs.getString("explanation"))
                        .setReading(rs.getString("reading"))
                        .setDifficulty(rs.getFloat("difficulty"))
                        .build();
                }
            }
        }
        return null;
    }

    public void insert(Phrase p) throws SQLException, IOException {
        String sql = "INSERT INTO phrase (term, meaning, explanation, reading, difficulty) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getTerm());
            ps.setString(2, p.getMeaning());
            ps.setString(3, p.getExplanation());
            ps.setString(4, p.getReading());
            ps.setFloat(5, p.getDifficulty());
            ps.executeUpdate();
        }
    }

    public void update(Phrase p, int id) throws SQLException, IOException {
        String sql = "UPDATE phrase SET term=?, meaning=?, explanation=?, reading=?, difficulty=? WHERE id=?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getTerm());
            ps.setString(2, p.getMeaning());
            ps.setString(3, p.getExplanation());
            ps.setString(4, p.getReading());
            ps.setFloat(5, p.getDifficulty());
            ps.setInt(6, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException, IOException {
        String sql = "DELETE FROM phrase WHERE id = ?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int getIdByTerm(String term) throws Exception {
        String sql = "SELECT id FROM phrase WHERE term = ?";
        try (Connection conn = Config.connect();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, term);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new Exception("Phrase with term '" + term + "' not found!");
    }
}