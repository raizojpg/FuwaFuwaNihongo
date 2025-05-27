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

        Phrase phrase = new Phrase.Builder()
            .setTerm("おはようございます")
            .setMeaning("Good morning")
            .setExplanation("Common greeting in the morning")
            .setReading("おはよう")
            .setDifficulty(0.2f)
            .build();
        dao.insert(phrase);

        List<Phrase> phrases = dao.getAll();
        for (Phrase p : phrases) {
            System.out.println(
                p.getTerm() + " - " +
                p.getMeaning() + " - " +
                p.getReading() + " - " +
                p.getDifficulty()
            );
        }
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
}