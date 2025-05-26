package entities;
import java.util.Objects;
import java.util.Scanner;
import utilities.PartOfSpeech;
import utilities.Splitable;
import utilities.Utils;

final public class Word extends Concept implements Splitable {
    private String reading;
    private float difficulty;
    private int frequency;
    private PartOfSpeech partOfSpeech;

    public Word(String term, String meaning, String explanation, String reading, float difficulty, int frequency, PartOfSpeech partOfSpeech) {
        super(term, meaning, explanation); 
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency must be non-negative.");
        }
        this.reading = reading;
        this.difficulty = difficulty;
        this.frequency = frequency;
        this.partOfSpeech = partOfSpeech;
    }

    public String getReading() {
        return reading;
    }

    public float getDifficulty() {
        return difficulty;
    }

    public int getFrequency() {
        return frequency;
    }

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public void setDifficulty(float difficulty) {
        this.difficulty = difficulty;
    }

    public void setFrequency(int frequency) {
        if (frequency < 0) {
            throw new IllegalArgumentException("Frequency must be non-negative.");
        }
        this.frequency = frequency;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    @Override
    public String toString() {
        return "Word: " + term + "\nMeaning: " + meaning + "\nExplanation: " + explanation +
               "\nReading: " + reading + "\nDifficulty: " + difficulty +
               "\nFrequency: " + frequency + "\nPart of Speech: " + partOfSpeech;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        if (!super.equals(obj)) return false;
        Word word = (Word) obj;
        return Float.compare(word.difficulty, difficulty) == 0 &&
               frequency == word.frequency &&
               reading.equals(word.reading) &&
               partOfSpeech.equals(word.partOfSpeech);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), reading, difficulty, frequency, partOfSpeech);
    }

    @Override
    public void displayDetails() {
        System.out.println(toString());
    }

    public static Word parse(String line){
        String[] parts = line.split(",");
        if (parts.length < 7) {
            System.out.println("Invalid Word entry: " + line);
            return null;
        }
        Word word = new Word.Builder()
                .setTerm(parts[0].trim())
                .setMeaning(parts[1].trim())
                .setExplanation(parts[2].trim())
                .setReading(parts[3].trim())
                .setDifficulty(Float.parseFloat(parts[4].trim()))
                .setFrequency(Integer.parseInt(parts[5].trim()))
                .setPartOfSpeech(PartOfSpeech.valueOf(parts[6].trim().toUpperCase()))
                .build();
        return word;
    }

    public static Word parse(Scanner scanner){
        Word word = new Word.Builder()
            .setTerm(Utils.prompt(scanner, "Enter Word:"))
            .setMeaning(Utils.prompt(scanner, "Enter Meaning:"))
            .setExplanation(Utils.prompt(scanner, "Enter Explanation:"))
            .setDifficulty(Float.parseFloat(Utils.prompt(scanner, "Enter Difficulty (0.0 - 10.0):")))
            .setFrequency(Integer.parseInt(Utils.prompt(scanner, "Enter Frequency:")))
            .setPartOfSpeech(PartOfSpeech.valueOf(Utils.prompt(scanner, "Enter Part of Speech:").toUpperCase()))
            .build();
        return word;
    }

    public static class Builder extends Concept.Builder<Builder> {
        private String reading;
        private float difficulty;
        private int frequency;
        private PartOfSpeech partOfSpeech;
    
        public Builder setReading(String reading) {
            this.reading = reading;
            return this;
        }
    
        public Builder setDifficulty(float difficulty) {
            this.difficulty = difficulty;
            return this;
        }
    
        public Builder setFrequency(int frequency) {
            this.frequency = frequency;
            return this;
        }
    
        public Builder setPartOfSpeech(PartOfSpeech partOfSpeech) {
            this.partOfSpeech = partOfSpeech;
            return this;
        }
    
        @Override
        protected Builder self() {
            return this;
        }
    
        public Word build() {
            validate(); 
            return new Word(term, meaning, explanation, reading, difficulty, frequency, partOfSpeech);
        }
    }
}