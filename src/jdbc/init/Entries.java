package jdbc.init;

import entities.Concept;
import entities.Kanji;
import entities.Phrase;
import entities.Word;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import jdbc.services.KanjiService;
import jdbc.services.PhraseService;
import jdbc.services.WordService;

public class Entries {

    public static void main(String[] args) {
        try {

            List<Concept> kanjiList = loadFromFile("resources/kanji.txt", Kanji::parse);
            KanjiService kanjiService = KanjiService.getInstance();
            for (Concept k : kanjiList) {
                if (k instanceof Kanji) {
                    try {
                        kanjiService.create((Kanji) k);
                    } catch (Exception ex) {
                        System.out.println("\nSkipped Kanji: " + k + " | Reason: " + ex.getMessage());
                    }
                } else {
                    System.out.println("\nSkipped non-Kanji concept: " + k);
                }
            }

            List<Concept> wordList = loadFromFile("resources/words.txt", Word::parse);
            WordService wordService = WordService.getInstance();
            for (Concept w : wordList) {
                if (w instanceof Word) {
                    try {
                        wordService.create((Word) w);
                    } catch (Exception ex) {
                        System.out.println("\nSkipped Word: " + w + " | Reason: " + ex.getMessage());
                    }
                } else {
                    System.out.println("\nSkipped non-Word concept: " + w);
                }
            }

            List<Concept> phraseList = loadFromFile("resources/phrases.txt", Phrase::parse);
            PhraseService phraseService = PhraseService.getInstance();
            for (Concept p : phraseList) {
                if (p instanceof Phrase) {
                    try {
                        phraseService.create((Phrase) p);
                    } catch (Exception ex) {
                        System.out.println("\nSkipped Phrase: " + p + " | Reason: " + ex.getMessage());
                    }
                } else {
                    System.out.println("\nSkipped non-Phrase concept: " + p);
                }
            }

            System.out.println("All entries imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Concept> loadFromFile(String filePath, Function<String, Concept> parser) {
        List<Concept> result = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Concept item = parser.apply(line); 
                if (item != null) {
                    result.add(item); 
                }
            }
            System.out.println("Entries loaded successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return result;
    }
}