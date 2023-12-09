package ru.spbu.apcyb.svp.tasks;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class ScanDirectoryTreeTest {
    @Test
    public void fileCheckTest() throws FileNotFoundException {
        ScanDirectoryTree test = new ScanDirectoryTree();
        String testPathString = "src/test/resources/task3.txt";
        File expected = new File(testPathString);
        assertEquals(expected, test.fileCheck(testPathString));
    }

    @Test
    public void fileCheckIsDirectoryTest() {
        ScanDirectoryTree test = new ScanDirectoryTree();
        String testPathString = "src/test/resources/Task3/";
        assertThrows(RuntimeException.class, () -> test.fileCheck(testPathString));
    }

    @Test
    public void fileCheckNotFoundTest() {
        ScanDirectoryTree test = new ScanDirectoryTree();
        String testPathString = "src/test/resources/task3.1.txt";
        assertThrows(FileNotFoundException.class, () -> test.fileCheck(testPathString));
    }

    @Test
    public void fileCheckInvalidPathTest() {
        ScanDirectoryTree test = new ScanDirectoryTree();
        String testPathString = "Not a path";
        assertThrows(FileNotFoundException.class, () -> test.fileCheck(testPathString));
    }

    @Test
    public void directoryCheckTest() throws FileNotFoundException {
        ScanDirectoryTree test = new ScanDirectoryTree();
        String testPathString = "src/test/resources/Task3/";
        File expected = new File(testPathString);
        assertEquals(expected, test.directoryCheck(testPathString));
    }

    @Test
    public void directoryCheckIsFileTest() {
        ScanDirectoryTree test = new ScanDirectoryTree();
        String testPathString = "src/test/resources/task3.txt";
        assertThrows(RuntimeException.class, () -> test.directoryCheck(testPathString));
    }

    @Test
    public void directoryCheckNotFoundTest() {
        ScanDirectoryTree test = new ScanDirectoryTree();
        String testPathString = "src/test/resources/Task4/";
        assertThrows(FileNotFoundException.class, () -> test.directoryCheck(testPathString));
    }

    @Test
    public void directoryCheckInvalidPathTest() {
        ScanDirectoryTree test = new ScanDirectoryTree();
        String testPathString = "Not a path";
        assertThrows(FileNotFoundException.class, () -> test.directoryCheck(testPathString));
    }

    @Test
    public void scanDirectoryTest() throws IOException {
        String file = "src/test/resources/task3.txt";
        String directory = "src/test/resources/Task3/";
        String[] args = {file, directory};
        ScanDirectoryTree.main(args);
        Path actual = Paths.get(file);
        Path expected = Paths.get("src/test/resources/result.txt");
        boolean equals = true;
        try (BufferedReader bf1 = Files.newBufferedReader(actual);
             BufferedReader bf2 = Files.newBufferedReader(expected)) {

            String line1, line2;
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();
                if (!line1.equals(line2)) {
                    equals = false;
                }
            }
        }
        assertTrue(equals);
    }

    @Test
    public void scanDirectoryIncorrectFilePathTest() {
        String file = "src/test/resources/";
        String directory = "src/test/resources/Task3/";
        String[] args = {file, directory};
        assertThrows(RuntimeException.class, () -> ScanDirectoryTree.main(args));
    }

    @Test
    public void scanDirectoryFilePathNotFoundTest() {
        String file = "src/test/resources/task4.txt";
        String directory = "src/test/resources/Task3/";
        String[] args = {file, directory};
        assertThrows(FileNotFoundException.class, () -> ScanDirectoryTree.main(args));
    }

    @Test
    public void scanDirectoryInvalidFilePathTest() {
        String file = "not a path";
        String directory = "src/test/resources/Task3/";
        String[] args = {file, directory};
        assertThrows(FileNotFoundException.class, () -> ScanDirectoryTree.main(args));
    }

    @Test
    public void scanDirectoryIncorrectDirectoryPathTest() {
        String file = "src/test/resources/task3.txt";
        String directory = "src/test/resources/task3.txt";
        String[] args = {file, directory};
        assertThrows(RuntimeException.class, () -> ScanDirectoryTree.main(args));
    }

    @Test
    public void scanDirectoryPathNotFoundTest() {
        String file = "src/test/resources/task3.txt";
        String directory = "src/test/resources/Task333/";
        String[] args = {file, directory};
        assertThrows(FileNotFoundException.class, () -> ScanDirectoryTree.main(args));
    }

    @Test
    public void scanDirectoryInvalidDirectoryPathTest() {
        String file = "src/test/resources/task3.txt";
        String directory = "not a path";
        String[] args = {file, directory};
        assertThrows(FileNotFoundException.class, () -> ScanDirectoryTree.main(args));
    }

}
