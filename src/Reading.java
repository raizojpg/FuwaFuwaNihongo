import java.util.Scanner;

public class Reading {
    private Phonetic onyomi; 
    private Phonetic kunyomi;

    public Reading(Phonetic onyomi, Phonetic kunyomi) {
        this.onyomi = onyomi;
        this.kunyomi = kunyomi;
    }

    public Phonetic getOnyomi() {
        return onyomi;
    }

    public Phonetic getKunyomi() {
        return kunyomi;
    }

    public void setOnyomi(Phonetic onyomi) {
        this.onyomi = onyomi;
    }

    public void setKunyomi(Phonetic kunyomi) {
        this.kunyomi = kunyomi;
    }

    @Override
    public String toString() {
        return "On'yomi: " + onyomi + "Kun'yomi: " + kunyomi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reading reading = (Reading) o;
        return onyomi.equals(reading.onyomi) && kunyomi.equals(reading.kunyomi);
    }

    @Override
    public int hashCode() {
        int result = onyomi.hashCode();
        result = 31 * result + kunyomi.hashCode();
        return result;
    }

    public boolean isEmpty() {
        return (onyomi == null || onyomi.getReadings().isEmpty()) &&
               (kunyomi == null || kunyomi.getReadings().isEmpty());
    }

    public static Reading createReading(Scanner scanner) {
        System.out.println("Enter On'yomi Readings (format: Japanese-English, separate multiple with commas):");
        String onyomiInput = scanner.nextLine();
        Phonetic onyomi = Phonetic.createPhonetic(onyomiInput);

        System.out.println("Enter Kun'yomi Readings (format: Japanese-English, separate multiple with commas):");
        String kunyomiInput = scanner.nextLine();
        Phonetic kunyomi = Phonetic.createPhonetic(kunyomiInput);

        return new Reading(onyomi, kunyomi);
    }

    public static Reading createReading(String input) {
        if (input == null || input.isEmpty()) {
            return new Reading(null, null); 
        }
    
        String[] parts = input.split("/");
        Phonetic onyomi = null;
        Phonetic kunyomi = null;
    
        if (parts.length > 0 && !parts[0].isEmpty()) {
            onyomi = Phonetic.createPhonetic(parts[0]);
        }
        if (parts.length > 1 && !parts[1].isEmpty()) {
            kunyomi = Phonetic.createPhonetic(parts[1]); 
        }
    
        return new Reading(onyomi, kunyomi);
    }

}