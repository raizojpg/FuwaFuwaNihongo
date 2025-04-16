import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Add chcp 65001 in terminal to see the Japanese characters
        Scanner scanner = new Scanner(System.in);
        Menu menu = Menu.getInstance();
        Dictionary dict = new Dictionary();

        dict.loadFromFile("../\\resources\\kanji.txt", Kanji::parse);
        dict.loadFromFile("../\\resources\\words.txt", Word::parse);
        dict.loadFromFile("../\\resources\\phrases.txt", Phrase::parse);
        menu.handleMainMenu(scanner,dict);

    }

}