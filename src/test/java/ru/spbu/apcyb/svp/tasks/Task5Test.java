package ru.spbu.apcyb.svp.tasks;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task5Test {

    @Test
    void countWordsTest() {
        Path testFile = Paths.get("src/test/resources/Task5/testData/test1.txt");
        Task5 test = new Task5();
        Map<String,Long> expected = new HashMap<>();
        expected.put("apple", 4L);
        expected.put("grape", 3L);
        expected.put("orange", 1L);
        Map<String, Long> actual = test.countWords(testFile);
        assertEquals(new TreeMap<>(expected), actual);
    }

    @Test
    void writeWordCountToFileTest() throws IOException {
        Path testFile = Paths.get("src/test/resources/Task5/testData/test1.txt");
        File testFileActual = new File("src/test/resources/Task5/testDataActual/test1Actual.txt");
        File testFileExpected = new File("src/test/resources/Task5/testDataExpected/test1Expected.txt");
        Task5 test = new Task5();
        Map<String, Long> actual = test.countWords(testFile);
        test.writeWordCountToFile(actual, testFileActual);
        assertEquals(-1, Files.mismatch(testFileActual.toPath(), testFileExpected.toPath()));
    }






}
