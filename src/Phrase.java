import java.util.Objects;
import java.util.Scanner;

final public class Phrase extends Concept implements Splitable{
    private String reading;
    private float difficulty;

    public Phrase(String term, String meaning, String explanation, String reading, float difficulty) {
        super(term, meaning, explanation); 
        this.reading = reading;
        this.difficulty = difficulty;
    }

    public String getReading() {
        return reading;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    @Override
    public String toString() {
        return "Phrase: " + term + "\nMeaning: " + meaning + "\nExplanation: " + explanation +
               "\nReading: " + reading + "\nDifficulty: " + difficulty;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Phrase phrase = (Phrase) obj;
        return Float.compare(phrase.difficulty, difficulty) == 0 &&
               Objects.equals(reading, phrase.reading);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reading, difficulty);
    }

    @Override
    public void displayDetails() {
        System.out.println(toString());
    }

    public static Phrase parse(String line){
        String[] parts = line.split(",");
        if (parts.length < 5) {
            System.out.println("Invalid Phrase entry: " + line);
            return null;
        }
        Phrase phrase = new Phrase.Builder()
                .setTerm(parts[0].trim())
                .setMeaning(parts[1].trim())
                .setExplanation(parts[2].trim())
                .setReading(parts[3].trim())
                .setDifficulty(Float.parseFloat(parts[4].trim()))
                .build();
        return phrase;
    }

    public static Phrase parse(Scanner scanner){
        Phrase phrase = new Phrase.Builder()
            .setTerm(Parsable.prompt(scanner, "Enter Phrase:"))
            .setMeaning(Parsable.prompt(scanner, "Enter Meaning:"))
            .setExplanation(Parsable.prompt(scanner, "Enter Explanation:"))
            .setReading(Parsable.prompt(scanner, "Enter Reading:"))
            .setDifficulty(Float.parseFloat(Parsable.prompt(scanner, "Enter Difficulty (0.0 - 10.0):")))
            .build();
        return phrase;
    }

    public static class Builder extends Concept.Builder<Builder> {
        private String reading;
        private float difficulty;

        public Builder setReading(String reading) {
            this.reading = reading;
            return this;
        }

        public Builder setDifficulty(float difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        public Phrase build() {
            validate();
            return new Phrase(term, meaning, explanation, reading, difficulty);
        }
    }

}