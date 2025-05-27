package entities;
import java.util.Objects;
import java.util.Scanner;
import utilities.Utils;

public final class Kanji extends Concept {
    private Reading reading; 
    private int level;
    private int frequency;       

    public Kanji(String character, String meaning, String explanation, Reading reading, int level, int frequency) {
        super(character, meaning, explanation); 
        if (level < 1 || level > 5) {
            throw new IllegalArgumentException("Level must be between 1 and 5.");
        }
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency must be non-negative.");
        }
        this.reading = reading;
        this.level = level;
        this.frequency = frequency;
    }

    public Reading getReading() {
        return reading;
    }

    public int getLevel() {
        return level;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setReading(Reading reading) {
        this.reading = reading;
    }

    public void setLevel(int level) {
        if (level < 1 || level > 5) {
            throw new IllegalArgumentException("Level must be between 1 and 5.");
        }
        this.level = level;
    }

    public void setFrequency(int frequency) {
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency must be non-negative.");
        }
        this.frequency = frequency;
    }

    @Override
    public String toString() {
        return "Kanji: " + term + "\nMeaning: " + meaning + "\nExplanation: " + explanation +
               "\nLevel: " + level + "\nFrequency: " + frequency + "\nReading:\n" + reading;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Kanji kanji = (Kanji) obj;
        return level == kanji.level &&
               frequency == kanji.frequency &&
               reading.equals(kanji.reading);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reading, level, frequency);
    }

    @Override
    public void displayDetails() {
        System.out.println(toString());
    }

    public static Kanji parse(String line){
        String[] parts = line.split(",");
        if (parts.length < 5) {
            System.out.println("Invalid Kanji entry: " + line);
            return null;
        }
        Kanji kanji = new Kanji.Builder()
                .setTerm(parts[0].trim())
                .setMeaning(parts[1].trim())
                .setExplanation(parts[2].trim())
                .setReading(Reading.createReading(parts[3].trim()))
                .setLevel(Integer.parseInt(parts[4].trim()))
                .build();
        return kanji;
    }

    public static Kanji parse(Scanner scanner) {
        Kanji kanji = new Kanji.Builder()
                .setTerm(Utils.prompt(scanner, "Enter Kanji:"))
                .setMeaning(Utils.prompt(scanner, "Enter Meaning:"))
                .setExplanation(Utils.prompt(scanner, "Enter Explanation:"))
                .setReading(Reading.createReading(scanner))
                .setLevel(Integer.parseInt(Utils.prompt(scanner, "Enter Level (1-5):")))
                .build();
        return kanji;
    }

    public static class Builder extends Concept.Builder<Builder> {
        private Reading reading;
        private int level;
        private int frequency;
    
        public Builder setReading(Reading reading) {
            this.reading = reading;
            return this;
        }
    
        public Builder setLevel(int level) {
            this.level = level;
            return this;
        }
    
        public Builder setFrequency(int frequency) {
            this.frequency = frequency;
            return this;
        }
    
        @Override
        protected Builder self() {
            return this;
        }
    
        public Kanji build() {
            validate();
            return new Kanji(term, meaning, explanation, reading, level, frequency);
        }
    }
}