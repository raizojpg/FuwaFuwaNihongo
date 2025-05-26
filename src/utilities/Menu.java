package utilities;
import entities.Concept;
import entities.Kanji;
import entities.Phrase;
import entities.Word;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import structures.Dictionary;

public class Menu {
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
        System.out.println("1. Add Items");
        System.out.println("2. Guessing Game");
        System.out.println("3. Print All Concepts");
        System.out.println("4. Exit");
    }

    public void displayAddMenu() {
        System.out.println("=== Add Menu ===");
        System.out.println("1. Add Kanji");
        System.out.println("2. Add Word");
        System.out.println("3. Add Phrase");
        System.out.println("4. Back to Main Menu");
    }

    public void displayGuessingGameMenu() {
        System.out.println("=== Guessing Game Menu ===");
        System.out.println("1. Guess Meaning");
        System.out.println("2. Guess Term");
        System.out.println("3. Back to Main Menu");
    }

    public void handleMainMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayMainMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> handleAddMenu(scanner,dict);
                case 2 -> handleGuessingGameMenu(scanner, dict);
                case 3 -> dict.printAllItems();
                case 4 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    public void handleAddMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayAddMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> dict.loadItem(scanner, Kanji::parse);
                case 2 -> dict.loadItem(scanner, Word::parse);
                case 3 -> dict.loadItem(scanner, Phrase::parse);
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    public void handleGuessingGameMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayGuessingGameMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> playGuess(scanner, dict, GuessType.MEANING);
                case 2 -> playGuess(scanner, dict, GuessType.TERM);
                case 3 -> {
                    return; 
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    public void playGuess(Scanner scanner, Dictionary dict, GuessType type) {
        if (dict.size() == 0) {
            System.out.println("No items available for guessing.");
            return;
        }
    
        List<Concept> concepts = dict.getAllItems();
        Random random = new Random();
    
        int totalGuesses = 0;
        int correctGuesses = 0;
        int streak = 0;
    
        while (true) {
            Concept randomConcept = concepts.get(random.nextInt(concepts.size()));
    
            totalGuesses++;
            boolean result = false;
            switch (type) {
                case MEANING -> result = randomConcept.playGuessMeaning(scanner);
                case TERM -> result = randomConcept.playGuessTerm(scanner);
                default -> {
                    break;
                }
            }

            if (result) {
                correctGuesses++;
                streak++;
            } else {
                streak = 0;
            }
    
            double guessRate = (double) correctGuesses / totalGuesses * 100;
            System.out.printf("Current Streak: %d | Correct Guesses: %d/%d (%.2f%%)%n", streak, correctGuesses, totalGuesses, guessRate);
    
            System.out.println("Do you want to continue? (press q if you want to exit)");
            String continueGame = scanner.nextLine().trim().toLowerCase();
            if (continueGame.equals("q")) {
                System.out.println("Exiting the guessing game. Final Score:");
                System.out.printf("Correct Guesses: %d/%d (%.2f%%)%n", correctGuesses, totalGuesses, guessRate);
                break;
            }
        }
    }

    // public void playGuessKanji(Scanner scanner) {
    //     System.out.println("Guess Kanji game is not implemented yet.");
    // }

    // public void playGuessWord(Scanner scanner) {
    //     System.out.println("Guess Word game is not implemented yet.");
    // }

    // public void playGuessPhrase(Scanner scanner) {
    //     System.out.println("Guess Phrase game is not implemented yet.");
    // }

    private static void clearConsole() {
        // ANSI escape code to clear the console
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public enum GuessType {MEANING,TERM}
    
}