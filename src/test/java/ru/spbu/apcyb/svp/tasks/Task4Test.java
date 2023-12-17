package ru.spbu.apcyb.svp.tasks;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Task4Test {

    final File numbers1 = new File("src/test/resources/Task4/testData/numbers1.txt");
    final File numbers100 = new File("src/test/resources/Task4/testData/numbers100.txt");
    final File numbers10000 = new File("src/test/resources/Task4/testData/numbers10000.txt");
    final File numbers1000000 = new File("src/test/resources/Task4/testData/numbers1000000.txt");

    final File result1 = new File("src/test/resources/Task4/testDataExpected/result1.txt");
    final File result100 = new File("src/test/resources/Task4/testDataExpected/result100.txt");
    final File resultExpected = new File("src/test/resources/Task4/testDataExpected/resultExpected.txt");
    final File result1000000 = new File("src/test/resources/Task4/testDataExpected/result1000000.txt");

    @Test
    void performanceSingleThreadTest() throws IOException {
       Task4 test = new Task4();

       File resultActual = test.writeNumbersToFile(Task4.calculateTangent(test.readFile(numbers10000)),
               new File("src/test/resources/Task4/testDataActual/resultActual.txt"));

       assertEquals(Files.mismatch(resultExpected.toPath(), resultActual.toPath()), -1);
    }

    @Test
    void performanceMultipleThreadsTest() throws IOException {
        Task4 test = new Task4();

        File resultActual = test.writeNumbersToFile(test.calculateTangentMultipleThreads(test.readFile(numbers10000), 10),
                new File("src/test/resources/Task4/testDataActual/resultActual.txt"));

        assertEquals(Files.mismatch(resultExpected.toPath(), resultActual.toPath()), -1);
    }

    @Test
    void comparePerformance() throws IOException {
        Task4 test = new Task4();
        File resultActual1 = new File("src/test/resources/Task4/testDataActual/result1.txt");
        File resultActual100 = new File("src/test/resources/Task4/testDataActual/result100.txt");
        File resultActual1000000 = new File("src/test/resources/Task4/testDataActual/result1000000.txt");


        System.out.println("  Threads  |  Numbers  |  Time(ms)  |");
        System.out.println("-------------------------------------");

        System.out.printf("%-10s | %-9s | %-10s |\n", 1, 1,
                test.performanceCheckSingleTread(numbers1, resultActual1));
        assertEquals(Files.mismatch(resultActual1.toPath(), result1.toPath()), -1);

        System.out.printf("%-10s | %-9s | %-10s |\n", 1, 100,
                test.performanceCheckSingleTread(numbers100, resultActual100));
        assertEquals(Files.mismatch(resultActual100.toPath(), result100.toPath()), -1);

        System.out.printf("%-10s | %-9s | %-10s |\n", 1, 1000000,
                test.performanceCheckSingleTread(numbers1000000, resultActual1000000));
        assertEquals(Files.mismatch(resultActual1000000.toPath(), result1000000.toPath()), -1);

        System.out.printf("%-10s | %-9s | %-10s |\n", 10, 1,
                test.performanceCheckMultipleTreads(numbers1, resultActual1, 10));
        assertEquals(Files.mismatch(resultActual1.toPath(), result1.toPath()), -1);

        System.out.printf("%-10s | %-9s | %-10s |\n", 10, 100,
                test.performanceCheckMultipleTreads(numbers100, resultActual100, 10));
        assertEquals(Files.mismatch(resultActual100.toPath(), result100.toPath()), -1);

        System.out.printf("%-10s | %-9s | %-10s |\n", 10, 1000000,
                test.performanceCheckMultipleTreads(numbers1000000, resultActual1000000, 10));
        assertEquals(Files.mismatch(resultActual1000000.toPath(), result1000000.toPath()), -1);

    }
}
