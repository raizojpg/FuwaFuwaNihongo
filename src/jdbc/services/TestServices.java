package jdbc.services;

import entities.Kanji;
import entities.Phrase;
import entities.Reading;
import entities.Word;
import structures.Phonetic;
import utilities.PartOfSpeech;

import java.util.List;

public class TestServices {
    public static void main(String[] args) throws Exception {
        // --- Test ReadingService ---
        ReadingService readingService = ReadingService.getInstance();
        Reading reading = new Reading(
            Phonetic.createPhonetic("ニチ-nichi;ジツ-jitsu"),
            Phonetic.createPhonetic("ひ-hi;か-ka")
        );
        readingService.create(reading);
        List<Reading> readings = readingService.readAll();
        System.out.println("All Readings: " + readings.size());
        int readingId = readingService.getIdByOnyomiKunyomi(
            "ニチ-nichi;ジツ-jitsu",
            "ひ-hi;か-ka"
        );
        Reading updatedReading = new Reading(
            Phonetic.createPhonetic("ニチ-nichi;ジツ-jitsu;ニッ-nitsu"),
            Phonetic.createPhonetic("ひ-hi;か-ka;び-bi")
        );
        readingService.update(updatedReading, readingId);
        System.out.println("Updated Reading with ID: " + readingId);
        readingService.delete(readingId);
        System.out.println("Deleted Reading with ID: " + readingId);
        System.out.println("----------------------------\n");

        // --- Test KanjiService ---
        KanjiService kanjiService = KanjiService.getInstance();
        Kanji kanji = new Kanji.Builder()
            .setTerm("日")
            .setMeaning("sun; day")
            .setExplanation("Represents the sun or a day")
            .setReading(reading)
            .setLevel(1)
            .setFrequency(100)
            .build();
        kanjiService.create(kanji);
        List<Kanji> kanjis = kanjiService.readAll();
        System.out.println("All Kanji: " + kanjis.size());
        int kanjiId = kanjiService.getIdByTerm("日");
        Kanji updatedKanji = new Kanji.Builder()
            .setTerm("日")
            .setMeaning("sun; day (updated)")
            .setExplanation("Represents the sun or a day (updated)")
            .setReading(reading)
            .setLevel(2)
            .setFrequency(200)
            .build();
        kanjiService.update(updatedKanji, kanjiId);
        System.out.println("Updated Kanji with ID: " + kanjiId);
        kanjiService.delete(kanjiId);
        System.out.println("Deleted Kanji with ID: " + kanjiId);
        System.out.println("----------------------------\n");

        // --- Test WordService ---
        WordService wordService = WordService.getInstance();
        Word word = new Word.Builder()
            .setTerm("猫")
            .setMeaning("cat")
            .setExplanation("A small domesticated carnivorous mammal")
            .setDifficulty(1.0f)
            .setFrequency(100)
            .setPartOfSpeech(PartOfSpeech.NOUN)
            .build();
        wordService.create(word);
        List<Word> words = wordService.readAll();
        System.out.println("All Words: " + words.size());
        int wordId = wordService.getIdByTerm("猫");
        Word updatedWord = new Word.Builder()
            .setTerm("猫")
            .setMeaning("cat (updated)")
            .setExplanation("Updated explanation")
            .setDifficulty(2.0f)
            .setFrequency(200)
            .setPartOfSpeech(PartOfSpeech.NOUN)
            .build();
        wordService.update(updatedWord, wordId);
        System.out.println("Updated Word with ID: " + wordId);
        wordService.delete(wordId);
        System.out.println("Deleted Word with ID: " + wordId);
        System.out.println("----------------------------\n");

        // --- Test PhraseService ---
        PhraseService phraseService = PhraseService.getInstance();
        Phrase phrase = new Phrase.Builder()
            .setTerm("おはようございます")
            .setMeaning("Good morning")
            .setExplanation("Common greeting in the morning")
            .setReading("おはよう")
            .setDifficulty(0.2f)
            .build();
        phraseService.create(phrase);
        List<Phrase> phrases = phraseService.readAll();
        System.out.println("All Phrases: " + phrases.size());
        int phraseId = phraseService.getIdByTerm("おはようございます");
        Phrase updatedPhrase = new Phrase.Builder()
            .setTerm("おはようございます")
            .setMeaning("Good morning (updated)")
            .setExplanation("Updated explanation")
            .setReading("おはよう")
            .setDifficulty(0.5f)
            .build();
        phraseService.update(updatedPhrase, phraseId);
        System.out.println("Updated Phrase with ID: " + phraseId);
        phraseService.delete(phraseId);
        System.out.println("Deleted Phrase with ID: " + phraseId);
        System.out.println("----------------------------\n");
    }
}