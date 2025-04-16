import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Add chcp 65001 in terminal to see the Japanese characters
        Scanner scanner = new Scanner(System.in);
        Menu menu = Menu.getInstance();

        menu.loadKanjiFromFile("../\\resources\\kanji.txt");
        menu.loadWordsFromFile("../\\resources\\words.txt");
        menu.loadPhrasesFromFile("../\\resources\\phrases.txt");

        while (true) {
            clearConsole();
            menu.displayMainMenu();
            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    menu.addKanji(scanner);
                    break;
                case 2:
                    menu.addWord(scanner);
                    break;
                case 3:
                    menu.addPhrase(scanner);
                    break;
                case 4:
                    menu.printAllConcepts();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }

    private static void clearConsole() {
        // ANSI escape code to clear the console (works on most terminals)
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}