package utilities;
import entities.Concept;
import entities.Kanji;
import entities.Phrase;
import entities.Word;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import jdbc.init.Tables;
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
        System.out.println("1. Manage Database");
        System.out.println("2. Manage Dictionary");
        System.out.println("3. Guessing Game");
        System.out.println("4. Exit");
    }

    public void handleMainMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayMainMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> handleDatabaseMenu(scanner,dict);
                case 2 -> handleDictionaryMenu(scanner, dict);
                case 3 -> handleGuessingGameMenu(scanner, dict);
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


    public void displayDatabaseMenu() {
        System.out.println("=== Database Menu ===");
        System.out.println("1. Add Item");
        System.out.println("2. Update Item");
        System.out.println("3. Delete Item");
        System.out.println("4. View All Items");
        System.out.println("5. Load Items from Resources");
        System.out.println("6. Clear Database");
        System.out.println("7. Test Services");
        System.out.println("8. Back to Main Menu");
    }

    public void handleDatabaseMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayDatabaseMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> handleAddMenu(scanner, dict);
                case 2 -> handleUpdateMenu(scanner, dict);
                case 3 -> handleDeleteMenu(scanner, dict);
                case 4 -> Tables.printAllTables();
                case 5 -> {
                    try {
                        jdbc.init.Entries.main(new String[0]);
                    } catch (Exception e) {
                        System.out.println("Error loading items from resources: " + e.getMessage());
                    }
                }
                case 6 -> Tables.clearAllTables();
                case 7 -> {
                    try {
                        jdbc.services.TestServices.main(new String[0]);
                    } catch (Exception e) {
                        System.out.println("Error testing services: " + e.getMessage());
                    }
                }
                case 8 -> { return; }
                default -> System.out.println("Invalid option. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }


    public void displayAddMenu() {
        System.out.println("=== Add Menu ===");
        System.out.println("1. Add Kanji");
        System.out.println("2. Add Word");
        System.out.println("3. Add Phrase");
        System.out.println("4. Back to Main Menu");
    }

    public void handleAddMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayAddMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter Kanji data (format as in file):");
                    String line = scanner.nextLine();
                    Kanji kanji = Kanji.parse(line);
                    if (kanji != null) {
                        try {
                            jdbc.services.KanjiService.getInstance().create(kanji);
                            System.out.println("Kanji added successfully to database!");
                        } catch (Exception e) {
                            System.out.println("Error adding Kanji: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input. Kanji not added.");
                    }
                }
                case 2 -> {
                    System.out.println("Enter Word data (format as in file):");
                    String line = scanner.nextLine();
                    Word word = Word.parse(line);
                    if (word != null) {
                        try {
                            jdbc.services.WordService.getInstance().create(word);
                            System.out.println("Word added successfully to database!");
                        } catch (Exception e) {
                            System.out.println("Error adding Word: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input. Word not added.");
                    }
                }
                case 3 -> {
                    System.out.println("Enter Phrase data (format as in file):");
                    String line = scanner.nextLine();
                    Phrase phrase = Phrase.parse(line);
                    if (phrase != null) {
                        try {
                            jdbc.services.PhraseService.getInstance().create(phrase);
                            System.out.println("Phrase added successfully to database!");
                        } catch (Exception e) {
                            System.out.println("Error adding Phrase: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input. Phrase not added.");
                    }
                }
                case 4 -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }


    public void displayUpdateMenu() {
        System.out.println("=== Update Menu ===");
        System.out.println("1. Update Kanji");
        System.out.println("2. Update Word");
        System.out.println("3. Update Phrase");
        System.out.println("4. Back to Database Menu");
    }

    public void handleUpdateMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayUpdateMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Kanji ID to update: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Kanji oldKanji = null;
                    try {
                        oldKanji = jdbc.services.KanjiService.getInstance().read(id);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        break;
                    }
                    System.out.println("Current Kanji:");
                    oldKanji.displayDetails();
                    System.out.println("Enter new Kanji data (format as in file):");
                    String line = scanner.nextLine();
                    Kanji newKanji = Kanji.parse(line);
                    if (newKanji != null) {
                        try {
                            jdbc.services.KanjiService.getInstance().update(newKanji, id);
                            System.out.println("Kanji updated successfully!");
                        } catch (Exception e) {
                            System.out.println("Error updating Kanji: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input. Update cancelled.");
                    }
                }
                case 2 -> {
                    System.out.print("Enter Word ID to update: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Word oldWord = null;
                    try {
                        oldWord = jdbc.services.WordService.getInstance().read(id);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        break;
                    }
                    System.out.println("Current Word:");
                    oldWord.displayDetails();
                    System.out.println("Enter new Word data (format as in file):");
                    String line = scanner.nextLine();
                    Word newWord = Word.parse(line);
                    if (newWord != null) {
                        try {
                            jdbc.services.WordService.getInstance().update(newWord, id);
                            System.out.println("Word updated successfully!");
                        } catch (Exception e) {
                            System.out.println("Error updating Word: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input. Update cancelled.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter Phrase ID to update: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    Phrase oldPhrase = null;
                    try {
                        oldPhrase = jdbc.services.PhraseService.getInstance().read(id);
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        break;
                    }
                    System.out.println("Current Phrase:");
                    oldPhrase.displayDetails();
                    System.out.println("Enter new Phrase data (format as in file):");
                    String line = scanner.nextLine();
                    Phrase newPhrase = Phrase.parse(line);
                    if (newPhrase != null) {
                        try {
                            jdbc.services.PhraseService.getInstance().update(newPhrase, id);
                            System.out.println("Phrase updated successfully!");
                        } catch (Exception e) {
                            System.out.println("Error updating Phrase: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid input. Update cancelled.");
                    }
                }
                case 4 -> { return; }
                default -> System.out.println("Invalid option. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }


    public void displayDeleteMenu() {
        System.out.println("=== Delete Menu ===");
        System.out.println("1. Delete Kanji");
        System.out.println("2. Delete Word");
        System.out.println("3. Delete Phrase");
        System.out.println("4. Back to Database Menu");
    }

    public void handleDeleteMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayDeleteMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Kanji ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    try {
                        jdbc.services.KanjiService.getInstance().delete(id);
                        System.out.println("Kanji deleted successfully!");
                    } catch (Exception e) {
                        System.out.println("Error deleting Kanji: " + e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Enter Word ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    try {
                        jdbc.services.WordService.getInstance().delete(id);
                        System.out.println("Word deleted successfully!");
                    } catch (Exception e) {
                        System.out.println("Error deleting Word: " + e.getMessage());
                    }
                }
                case 3 -> {
                    System.out.print("Enter Phrase ID to delete: ");
                    int id = Integer.parseInt(scanner.nextLine());
                    try {
                        jdbc.services.PhraseService.getInstance().delete(id);
                        System.out.println("Phrase deleted successfully!");
                    } catch (Exception e) {
                        System.out.println("Error deleting Phrase: " + e.getMessage());
                    }
                }
                case 4 -> { return; }
                default -> System.out.println("Invalid option. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }


    public void displayDictionaryMenu() {
        System.out.println("=== Dictionary Menu ===");
        System.out.println("1. Load Items from Database");
        System.out.println("2. Print All Concepts");
        System.out.println("3. Print Composed Concepts");
        System.out.println("4. Back to Main Menu");
    }

    public void handleDictionaryMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayDictionaryMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    try {
                        dict.loadFromDatabase();
                        System.out.println("Items loaded from database into dictionary!");
                    } catch (Exception e) {
                        System.out.println("Error loading items from database: " + e.getMessage());
                    }
                }
                case 2 -> dict.printAllItems();
                case 3 -> dict.printSplitableItems();
                case 4 -> { return; }
                default -> System.out.println("Invalid option. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }


    public void displayGuessingGameMenu() {
        System.out.println("=== Guessing Game Menu ===");
        System.out.println("1. Guess Meaning");
        System.out.println("2. Guess Term");
        System.out.println("3. Back to Main Menu");
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


    private static void clearConsole() {
        // ANSI escape code to clear the console
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public enum GuessType {MEANING,TERM}
    
}