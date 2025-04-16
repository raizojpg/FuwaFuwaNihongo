import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private List<Concept> concepts = new ArrayList<>();
    private static Menu instance;

    private Menu() {}

    public static Menu getInstance() {
        if (instance == null) {
            instance = new Menu();
        }
        return instance;
    }

    public void displayMainMenu() {
        System.out.println("=== Main Menu ===");
        System.out.println("1. Add Kanji");
        System.out.println("2. Add Word");
        System.out.println("3. Add Phrase");
        System.out.println("4. Print All Concepts");
        System.out.println("5. Exit");
    }
    
    public void addKanji(Scanner scanner) {
        Kanji kanji = new Kanji.Builder()
                .setTerm(prompt(scanner, "Enter Kanji:"))
                .setMeaning(prompt(scanner, "Enter Meaning:"))
                .setExplanation(prompt(scanner, "Enter Explanation:"))
                .setReading(Reading.createReading(scanner))
                .setLevel(Integer.parseInt(prompt(scanner, "Enter Level (1-5):")))
                .build();
        concepts.add(kanji);
        System.out.println("Kanji added: " + kanji.getTerm() + " (" + kanji.getMeaning() + ")");
    }

    public void addWord(Scanner scanner) {
        Word word = new Word.Builder()
                .setTerm(prompt(scanner, "Enter Word:"))
                .setMeaning(prompt(scanner, "Enter Meaning:"))
                .setExplanation(prompt(scanner, "Enter Explanation:"))
                .setDifficulty(Float.parseFloat(prompt(scanner, "Enter Difficulty (0.0 - 10.0):")))
                .setFrequency(Integer.parseInt(prompt(scanner, "Enter Frequency:")))
                .setPartOfSpeech(PartOfSpeech.valueOf(prompt(scanner, "Enter Part of Speech:").toUpperCase()))
                .build();
        concepts.add(word);
        System.out.println("Word added: " + word.getTerm() + " (" + word.getMeaning() + ")");
    }

    public void addPhrase(Scanner scanner) {
        Phrase phrase = new Phrase.Builder()
                .setTerm(prompt(scanner, "Enter Phrase:"))
                .setMeaning(prompt(scanner, "Enter Meaning:"))
                .setExplanation(prompt(scanner, "Enter Explanation:"))
                .setReading(prompt(scanner, "Enter Reading:"))
                .setDifficulty(Float.parseFloat(prompt(scanner, "Enter Difficulty (0.0 - 10.0):")))
                .build();
        concepts.add(phrase);
        System.out.println("Phrase added: " + phrase.getTerm() + " (" + phrase.getMeaning() + ")");
    }

    public void loadKanjiFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    System.out.println("Invalid Kanji entry: " + line);
                    continue;
                }
                Kanji kanji = new Kanji.Builder()
                        .setTerm(parts[0].trim())
                        .setMeaning(parts[1].trim())
                        .setExplanation(parts[2].trim())
                        .setReading(Reading.createReading(parts[3].trim()))
                        .setLevel(Integer.parseInt(parts[4].trim()))
                        .build();
                concepts.add(kanji);
            }
            System.out.println("Kanji loaded successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading Kanji file: " + e.getMessage());
        }
    }

    public void loadWordsFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 7) {
                    System.out.println("Invalid Word entry: " + line);
                    continue;
                }
                Word word = new Word.Builder()
                        .setTerm(parts[0].trim())
                        .setMeaning(parts[1].trim())
                        .setExplanation(parts[2].trim())
                        .setReading(parts[3].trim())
                        .setDifficulty(Float.parseFloat(parts[4].trim()))
                        .setFrequency(Integer.parseInt(parts[5].trim()))
                        .setPartOfSpeech(PartOfSpeech.valueOf(parts[6].trim().toUpperCase()))
                        .build();
                concepts.add(word);
            }
            System.out.println("Words loaded successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading Words file: " + e.getMessage());
        }
    }

    public void loadPhrasesFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 5) {
                    System.out.println("Invalid Phrase entry: " + line);
                    continue;
                }
                Phrase phrase = new Phrase.Builder()
                        .setTerm(parts[0].trim())
                        .setMeaning(parts[1].trim())
                        .setExplanation(parts[2].trim())
                        .setReading(parts[3].trim())
                        .setDifficulty(Float.parseFloat(parts[4].trim()))
                        .build();
                concepts.add(phrase);
            }
            System.out.println("Phrases loaded successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading Phrases file: " + e.getMessage());
        }
    }

    public void printAllConcepts() {
        if (concepts.isEmpty()) {
            System.out.println("No concepts available.");
            return;
        }
    
        System.out.println("=== All Concepts ===");
        for (Concept concept : concepts) {
            concept.displayDetails();
            System.out.println("--------------------");
        }
    }

    private String prompt(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }
}