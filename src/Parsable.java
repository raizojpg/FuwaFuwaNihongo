import java.util.Scanner;

public interface Parsable {
    public static String prompt(Scanner scanner, String message) {
        System.out.println(message);
        return scanner.nextLine();
    }

    public static Parsable parse(String line) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public static Parsable parse(Scanner scanner) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
