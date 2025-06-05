package utilities;
import java.util.ArrayList;
import java.util.List;

public interface Splitable {

    String getTerm();

    default List<String> splitIntoKanji() {
        String term = getTerm();
        if (term == null || term.isEmpty()) {
            return new ArrayList<>(); 
        }

        List<String> kanjiList = new ArrayList<>();
        for (char c : term.toCharArray()) {
            // Check if the character is a Kanji
            if ((c >= '\u4E00' && c <= '\u9FFF') || // CJK Unified Ideographs
                (c >= '\u3400' && c <= '\u4DBF')) { // CJK Unified Ideographs Extension A
                kanjiList.add(String.valueOf(c));
            }
        }

        return kanjiList; 
    }

    default void display() {
        List<String> kanjiList = splitIntoKanji();
        if (!kanjiList.isEmpty()){
            StringBuilder sb = new StringBuilder("\nI am composed of the following Kanji:\n");
            for (String kanji : kanjiList) {
                sb.append(kanji).append("\n");
            }
            System.out.println(sb.toString());
        }
        else{
            System.out.println("\nI am not composed of any kanji, I am just kana.");
        }
    }   
}