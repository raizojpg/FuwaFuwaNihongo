import java.util.ArrayList;
import java.util.List;

public class Phonetic {
    private List<Pair<String, String>> readings;

    public Phonetic(List<Pair<String, String>> readings) {
        this.readings = readings;
    }

    public List<Pair<String, String>> getReadings() {
        return readings;
    }

    public void setReadings(List<Pair<String, String>> readings) {
        this.readings = readings;
    }

    public void addReading(String japanese, String english) {
        readings.add(new Pair<>(japanese, english));
    }

    public boolean removeReading(String japanese, String english) {
        return readings.remove(new Pair<>(japanese, english));
    }

    public List<String> getJapaneseReadings() {
        List<String> japaneseReadings = new ArrayList<>();
        for (Pair<String, String> reading : readings) {
            japaneseReadings.add(reading.getFirst());
        }
        return japaneseReadings;
    }

    public List<String> getEnglishReadings() {
        List<String> englishReadings = new ArrayList<>();
        for (Pair<String, String> reading : readings) {
            englishReadings.add(reading.getSecond());
        }
        return englishReadings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(" \n");
        for (Pair<String, String> reading : readings) {
            sb.append("\t(Japanese: ").append(reading.getFirst())
              .append(", English: ").append(reading.getSecond()).append(")\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Phonetic phonetic = (Phonetic) o;
        return readings.equals(phonetic.readings);
    }
    
    @Override
    public int hashCode() {
        return readings.hashCode();
    }

    static public Phonetic createPhonetic(String input) {
        List<Pair<String, String>> readings = new ArrayList<>();
        if (!input.isEmpty()) {
            String[] pairs = input.split(";");
            for (String pair : pairs) {
                String[] parts = pair.trim().split("-");
                if (parts.length == 2) {
                    readings.add(new Pair<>(parts[0].trim(), parts[1].trim()));
                } else {
                    System.out.println("Invalid format for reading: " + pair);
                }
            }
        }
        return new Phonetic(readings);
    }

}