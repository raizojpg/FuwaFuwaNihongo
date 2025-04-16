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
}