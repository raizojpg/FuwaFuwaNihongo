package utilities;
import java.util.Scanner;

public interface Utils {
    public static String prompt(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }
}
