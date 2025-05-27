package jdbc.dao;

import entities.Word;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jdbc.config.Config;
import utilities.PartOfSpeech;

public class WordDAO {

    public static void main(String[] args) throws Exception {
        WordDAO dao = new WordDAO();

        // 1. INSERT
        Word word = new Word.Builder()
            .setTerm("猫")
            .setMeaning("cat")
            .setExplanation("A small domesticated carnivorous mammal")
            .setDifficulty(1.0f)
            .setFrequency(100)
            .setPartOfSpeech(PartOfSpeech.NOUN)
            .build();
        dao.insert(word);
        System.out.println("Inserted Word.");

        // 2. GET ALL
        List<Word> words = dao.getAll();
        System.out.println("All Words:");
        for (Word w : words) {
            System.out.println(w.getTerm() + " - " + w.getMeaning() + " - " + w.getPartOfSpeech());
        }

        // 3. GET BY ID
        int id = 1;
        Word found = dao.getById(id);
        System.out.println("Get by ID (" + id + "): " +
            (found != null ? found.getTerm() + " - " + found.getMeaning() : "Not found"));

        // 4. UPDATE
        Word updatedWord = new Word.Builder()
            .setTerm("猫")
            .setMeaning("cat (updated)")
            .setExplanation("Updated explanation")
            .setDifficulty(2.0f)
            .setFrequency(200)
            .setPartOfSpeech(PartOfSpeech.NOUN)
            .build();
        dao.update(updatedWord, id);
        System.out.println("Updated Word with ID " + id);

        // 5. DELETE
        dao.delete(id);
        System.out.println("Deleted Word with ID " + id);

        // Confirm deletion
        Word afterDelete = dao.getById(id);
        System.out.println("After delete, getById(" + id + "): " + (afterDelete == null ? "Not found" : "Still exists!"));
    }

    public List<Word> getAll() throws SQLException, IOException {
        List<Word> list = new ArrayList<>();
        try (Connection conn = Config.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM word")) {
            while (rs.next()) {
                Word w = new Word.Builder()
                    .setTerm(rs.getString("term"))
                    .setMeaning(rs.getString("meaning"))
                    .setExplanation(rs.getString("explanation"))
                    .setDifficulty(rs.getFloat("difficulty"))
                    .setFrequency(rs.getInt("frequency"))
                    .setPartOfSpeech(PartOfSpeech.valueOf(rs.getString("partOfSpeech").toUpperCase()))
                    .build();
                list.add(w);
            }
        }
        return list;
    }

    public Word getById(int id) throws SQLException, IOException {
        String sql = "SELECT * FROM word WHERE id = ?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Word.Builder()
                        .setTerm(rs.getString("term"))
                        .setMeaning(rs.getString("meaning"))
                        .setExplanation(rs.getString("explanation"))
                        .setDifficulty(rs.getFloat("difficulty"))
                        .setFrequency(rs.getInt("frequency"))
                        .setPartOfSpeech(PartOfSpeech.valueOf(rs.getString("partOfSpeech").toUpperCase()))
                        .build();
                }
            }
        }
        return null;
    }

    public void insert(Word w) throws SQLException, IOException {
        String sql = "INSERT INTO word (term, meaning, explanation, difficulty, frequency, partOfSpeech) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, w.getTerm());
            ps.setString(2, w.getMeaning());
            ps.setString(3, w.getExplanation());
            ps.setFloat(4, w.getDifficulty());
            ps.setInt(5, w.getFrequency());
            ps.setString(6, w.getPartOfSpeech().name());
            ps.executeUpdate();
        }
    }

    public void update(Word w, int id) throws SQLException, IOException {
        String sql = "UPDATE word SET term=?, meaning=?, explanation=?, difficulty=?, frequency=?, partOfSpeech=? WHERE id=?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, w.getTerm());
            ps.setString(2, w.getMeaning());
            ps.setString(3, w.getExplanation());
            ps.setFloat(4, w.getDifficulty());
            ps.setInt(5, w.getFrequency());
            ps.setString(6, w.getPartOfSpeech().name());
            ps.setInt(7, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException, IOException {
        String sql = "DELETE FROM word WHERE id = ?";
        try (Connection conn = Config.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}