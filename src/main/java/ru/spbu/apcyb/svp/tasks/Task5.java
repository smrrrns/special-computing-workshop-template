package ru.spbu.apcyb.svp.tasks;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Task5 {
    Task5(){
    }

    /**
     * Counts the number of occurrences of words in the text.
     */
    public Map<String, Long> countWords(Path pathToFile) {
        Map<String, Long> map;
        try (Stream<String> lines = Files.lines(pathToFile)) {

            map = lines.flatMap(line -> Arrays.stream(line.trim().split("[\\p{Punct}\\s]")))
                    .map(word -> word.replaceAll("\\P{L}", "").trim())
                    .map(String::toLowerCase)
                    .filter(word -> word.length() > 0).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        } catch (IOException e) {
            throw new RuntimeException("Failed to count words");
        }

        return new TreeMap<>(map);
    }

    /**
     * Writes words and the number of occurrences of words to files.
     */
    public void writeWordCountToFile(Map<String, Long> words, File file) {
        try (FileWriter writer = new FileWriter(file, false)) {
            words.forEach((k,v) -> {
                try {
                    writer.write(k + " " + v.toString());
                    writer.write(System.lineSeparator());
                    CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                        try {
                            writeWordToSeparateFile(k,v);
                        } catch (ExecutionException | InterruptedException ex) {
                            throw new RuntimeException("Failed to write words to files");
                        }
                    });
                    future.get();

                } catch (IOException | InterruptedException | ExecutionException e) {
                    throw new RuntimeException("Failed to write word count to file");
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file");
        }
    }

    /**
     * Writes words from the text to file.
     */
    public void writeWordToSeparateFile(String word, Long wordCount) throws ExecutionException, InterruptedException {
        File file = new File("src/test/resources/Task5/wordFiles/" + word + ".txt");
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file, true)) {
                for (int i = 0; i < wordCount; i++) {
                    writer.write(word + "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException("Failed to write to file");
            }
        }
    }

    public static void main(String[] args) {
        File counts = new File("src/test/resources/Task5/counts.txt");
        Path textFilePath = Paths.get("src/test/resources/Task5/bigTextFile.txt");

        Task5 t = new Task5();

        Map<String,Long> map = t.countWords(textFilePath);
        t.writeWordCountToFile(map, counts);
    }

}
