import java.util.Scanner;
import structures.Dictionary;
import utilities.Menu;

public class Main {
    public static void main(String[] args) {
        // Add chcp 65001 in terminal to see the Japanese characters
        Scanner scanner = new Scanner(System.in,"UTF-8");
        Menu menu = Menu.getInstance();
        Dictionary dict = new Dictionary(); 

        menu.handleMainMenu(scanner,dict);
    }
}