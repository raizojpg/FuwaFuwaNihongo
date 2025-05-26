package utilities;
import java.text.Normalizer;
import java.util.Scanner;

public interface Guessable {

    String getTerm();

    String getMeaning();

    default boolean playGuessTerm(Scanner scanner){
        boolean result = checkTerm(guessTerm(scanner));
        if (result) {
            System.out.println("Bravo! You guessed it correctly.");
        } else {
            System.out.println("Sorry, that's incorrect. The correct term is: " + getTerm());
        }
        return result;
    }

    default boolean playGuessMeaning(Scanner scanner) {
        boolean result = checkMeaning(guessMeaning(scanner));
        if (result) {
            System.out.println("Bravo! You guessed it correctly.");
        } else {
            System.out.println("Sorry, that's incorrect. The correct meaning is: " + getMeaning());
        }
        return result;
    }

    default String guessTerm(Scanner scanner) {
        System.out.println("Guess the term used for: " + getMeaning());
        String guessedTerm = scanner.nextLine();
        return guessedTerm;
    }

    default String guessMeaning(Scanner scanner) {
        System.out.println("Guess the meaning of the term: " + getTerm());
        return scanner.nextLine();
    }

    default boolean checkTerm(String guessedTerm) {
        System.out.println("Stored Term: " + getTerm() + " | Length: " + getTerm().length());
        System.out.println("Guessed Term: " + guessedTerm + " | Length: " + guessedTerm.length());
        System.out.println("Stored Term Code Points: " + getTerm().codePoints().toArray());
        System.out.println("Guessed Term Code Points: " + guessedTerm.codePoints().toArray());
        return getTerm() != null && 
            Normalizer.normalize(getTerm(), Normalizer.Form.NFC).equalsIgnoreCase(
            Normalizer.normalize(guessedTerm.trim(), Normalizer.Form.NFC));
    }

    default boolean checkMeaning(String guessedMeaning) {
        return getMeaning() != null && getMeaning().equalsIgnoreCase(guessedMeaning);
    }
}