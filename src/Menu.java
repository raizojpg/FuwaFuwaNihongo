import java.util.Scanner;

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
        System.out.println("1. Guess Kanji");
        System.out.println("2. Guess Word");
        System.out.println("3. Guess Phrase");
        System.out.println("4. Back to Main Menu");
    }

    public void handleMainMenu(Scanner scanner, Dictionary dict) {
        while (true) {
            clearConsole();
            displayMainMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> handleAddMenu(scanner,dict);
                case 2 -> handleGuessingGameMenu(scanner);
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

    public void handleGuessingGameMenu(Scanner scanner) {
        while (true) {
            clearConsole();
            displayGuessingGameMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> playGuessKanji(scanner);
                case 2 -> playGuessWord(scanner);
                case 3 -> playGuessPhrase(scanner);
                case 4 -> {
                    return; 
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    public void playGuessKanji(Scanner scanner) {
        System.out.println("Guess Kanji game is not implemented yet.");
    }

    public void playGuessWord(Scanner scanner) {
        System.out.println("Guess Word game is not implemented yet.");
    }

    public void playGuessPhrase(Scanner scanner) {
        System.out.println("Guess Phrase game is not implemented yet.");
    }

    private static void clearConsole() {
        // ANSI escape code to clear the console (works on most terminals)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}