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

        List<Kanji> kanjiList = dao.getAll();
        for (Kanji k : kanjiList) {
            System.out.println(
                k.getTerm() + " - " +
                k.getMeaning() + " - " +
                (k.getReading() != null ? 
                    "\nOnyomi: " + k.getReading().getOnyomi() + ", Kunyomi: " + k.getReading().getKunyomi()
                    : "No Reading"
                )
            );
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
}