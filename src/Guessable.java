import java.util.Scanner;

public interface Guessable {

    String getTerm();

    String getMeaning();

    default void guessTerm(Scanner scanner) {
        System.out.println("Guess the term used for: " + getMeaning());
        String guessedTerm = scanner.nextLine();

        if (checkTerm(guessedTerm)) {
            System.out.println("Bravo! You guessed it correctly.");
        } else {
            System.out.println("Sorry, that's incorrect. The correct term is: " + getTerm());
        }
    }

    default void guessMeaning(Scanner scanner) {
        System.out.println("Guess the meaning of the term: " + getTerm());
        String guessedMeaning = scanner.nextLine();

        if (checkMeaning(guessedMeaning)) {
            System.out.println("Bravo! You guessed it correctly.");
        } else {
            System.out.println("Sorry, that's incorrect. The correct meaning is: " + getMeaning());
        }
    }

    default boolean checkTerm(String guessedTerm) {
        return getTerm() != null && getTerm().equalsIgnoreCase(guessedTerm);
    }

    default boolean checkMeaning(String guessedMeaning) {
        return getMeaning() != null && getMeaning().equalsIgnoreCase(guessedMeaning);
    }
}