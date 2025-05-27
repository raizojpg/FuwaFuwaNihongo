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

        Word word = new Word.Builder()
            .setTerm("猫")
            .setMeaning("cat")
            .setExplanation("A small domesticated carnivorous mammal")
            .setDifficulty(1.0f)
            .setFrequency(100)
            .setPartOfSpeech(PartOfSpeech.NOUN)
            .build();
        dao.insert(word);

        List<Word> words = dao.getAll();
        for (Word w : words) {
            System.out.println(w.getTerm() + " - " + w.getMeaning() + " - " + w.getPartOfSpeech());
        }
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
}