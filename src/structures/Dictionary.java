package structures;

import entities.Concept;
import entities.Kanji;
import entities.Phrase;
import entities.Word;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import jdbc.services.KanjiService;
import jdbc.services.PhraseService;
import jdbc.services.WordService;
import utilities.Splitable;

public class Dictionary {
    protected Map<String, Concept> dict;

    public Dictionary() {
        this.dict = new HashMap<>();
    }

    public void addItem(String key, Concept item) {
        dict.put(key, item);
    }

    public Concept getItem(String key) {
        return dict.get(key);
    }

    public void removeItem(String key) {
        dict.remove(key);
    }

    public boolean containsItem(String key) {
        return dict.containsKey(key);
    }

    public int size() {
        return dict.size();
    }

    public List<Concept> getAllItems(){
        List<Concept> concepts = new ArrayList<>(dict.values());
        Collections.sort(concepts);
        return concepts;
    }

    public void printAllItems() {
        if (dict.isEmpty()) {
            System.out.println("No concepts available.");
            return;
        }
        System.out.println("=== All Concepts ===");
        for (Concept concept : getAllItems()) {
            concept.displayDetails();
            System.out.println("--------------------");
        }
    }

    public void printSplitableItems() {
        System.out.println("=== Splitable Items ===");
        int count = 0;
        for (Concept item : getAllItems()) {
            if (item instanceof Splitable splitable) {
                System.out.println("[" + (++count) + "] " + item);
                splitable.display();
                System.out.println("--------------------");
            }
        }
        if (count == 0) {
            System.out.println("No splitable items found.");
        }
    }


    public void loadFromFile(String filePath, Function<String, Concept> parser) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Concept item = parser.apply(line); 
                if (item != null) {
                    addItem(item.getTerm(), item); 
                }
            }
            System.out.println("Entries loaded successfully from " + filePath);
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    public void loadItem(Scanner scanner, Function<Scanner,Concept> parser){
        Concept item = parser.apply(scanner); 
        addItem(item.getTerm(), item); 
        System.out.println("Concept added: " + item.getTerm() + " (" + item.getMeaning() + ")");
    }        

    public void loadFromDatabase() {
        dict.clear();
        try {
            List<Kanji> kanjiList = KanjiService.getInstance().readAll();
            for (Kanji k : kanjiList) {
                addItem(k.getTerm(), k);
            }
            List<Word> wordList = WordService.getInstance().readAll();
            for (Word w : wordList) {
                addItem(w.getTerm(), w);
            }
            List<Phrase> phraseList = PhraseService.getInstance().readAll();
            for (Phrase p : phraseList) {
                addItem(p.getTerm(), p);
            }
            System.out.println("Entries loaded successfully from database!");
        } catch (Exception e) {
            System.out.println("Error loading from database: " + e.getMessage());
        }
    }

}