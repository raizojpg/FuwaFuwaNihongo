package entities;
import java.util.Objects;
import utilities.Guessable;

public abstract class Concept implements Comparable<Concept>, Guessable {
    protected String term; // Represents the Japanese concept
    protected String meaning;
    protected String explanation;

    public Concept(String term, String meaning, String explanation) {
        this.term = term;
        this.meaning = meaning;
        this.explanation = explanation;
    }

    @Override
    public String getTerm() {
        return term;
    }

    @Override
    public String getMeaning() {
        return meaning;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Concept concept = (Concept) obj;
        return Objects.equals(term, concept.term) &&
               Objects.equals(meaning, concept.meaning) &&
               Objects.equals(explanation, concept.explanation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(term, meaning, explanation);
    }

    @Override
    public int compareTo(Concept other) {
        // Prioritize Kanji first, then Word, then Phrase
        if (this instanceof Kanji && !(other instanceof Kanji)) {
            return -1; 
        } else if (other instanceof Kanji && !(this instanceof Kanji)) {
            return 1; 
        } else if (this instanceof Word && !(other instanceof Word)) {
            return -1; 
        } else if (other instanceof Word && !(this instanceof Word)) {
            return 1; 
        }
        return this.term.compareTo(other.term); // Alphabetically after term
    }

    public abstract void displayDetails();

    public abstract static class Builder<T extends Builder<T>> {
        protected String term;
        protected String meaning;
        protected String explanation;

        public T setTerm(String term) {
            this.term = term;
            return self();
        }

        public T setMeaning(String meaning) {
            this.meaning = meaning;
            return self();
        }

        public T setExplanation(String explanation) {
            this.explanation = explanation;
            return self();
        }

        protected abstract T self();

        protected void validate() {
            if (term == null || term.isEmpty()) {
                throw new IllegalArgumentException("Term cannot be null or empty.");
            }
            if (meaning == null || meaning.isEmpty()) {
                throw new IllegalArgumentException("Meaning cannot be null or empty.");
            }
        }
    }

}