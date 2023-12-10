package ru.spbu.apcyb.svp.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Task4 {

    Task4() {
    }

    /**
     * Measures execution time for single thread.
     *
     * @param fileToRead file with numbers
     * @param fileToWrite file to write calculated values
     * @return time of execution
     */
    public long performanceCheckSingleTread(File fileToRead, File fileToWrite) {
        long startTime = System.currentTimeMillis();

        writeNumbersToFile(calculateTangent(readFile(fileToRead)), fileToWrite);

        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    /**
     * Measures execution time for multiple threads.
     * @param fileToRead file with numbers
     * @param fileToWrite file to write calculated values
     * @param numberOfThreads number of threads
     * @return time of execution
     */
    public long performanceCheckMultipleTreads(File fileToRead, File fileToWrite, int numberOfThreads) {
        long startTime = System.currentTimeMillis();

        writeNumbersToFile(calculateTangentMultipleThreads(readFile(fileToRead), numberOfThreads), fileToWrite);

        long endTime = System.currentTimeMillis();

        return endTime - startTime;
    }

    /**
     * Calculates tangent of numbers in list.
     *
     * @param numbers list of numbers
     * @return list of calculated tangents of numbers
     */
    public static List<Double> calculateTangent(List<Double> numbers) {
        List<Double> result = new ArrayList<>();
        for (double num : numbers) {
            result.add(Math.tan(num));
        }
        return result;
    }

    /**
     * Calculates tangent of numbers in list using multiple threads.
     *
     * @param numbers list of numbers
     * @param numberOfThreads number of threads
     * @return list of calculated tangents of numbers
     */
    public List<Double> calculateTangentMultipleThreads(List<Double> numbers, int numberOfThreads) {
        ExecutorService service = Executors.newFixedThreadPool(numberOfThreads);
        try {
            Callable<List<Double>> task = () -> numbers.parallelStream().map(Math::tan).collect(Collectors.toList());
            Future<List<Double>> result = service.submit(task);

            return result.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate using multiple treads");
        } finally {
            service.shutdown();
            try {
                if (!service.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                    service.shutdownNow();
                }
            } catch (InterruptedException e) {
                service.shutdownNow();
            }
        }
    }

    /**
     * Reads numbers from file.
     *
     * @param file file with numbers
     * @return list of numbers from file
     */
    public List<Double> readFile(File file){

        try (Scanner input = new Scanner(file)) {
            List<Double> result = new ArrayList<>();
            while (input.hasNextLine()) {
                result.add(Double.parseDouble(input.nextLine()));
            }
            return result;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Failed to read numbers from file");
        }
    }

    /**
     * Writes list of numbers to file.
     *
     * @param numbers list of numbers
     * @param file file in which to write numbers
     */
    public File writeNumbersToFile(List<Double> numbers, File file) {
        try (FileWriter writer = new FileWriter(file, false)) {
            for (double num : numbers) {
                writer.write(Double.toString(num));
                writer.write(System.lineSeparator());
            }
            return file;

        } catch (IOException e) {
            throw new RuntimeException("Failed to write numbers to file");
        }
    }

    /**
     * Generates N random numbers in the range [A,B).
     * @param A lower boundary
     * @param B upper boundary
     * @param N amount of numbers to generate
     * @return list of generated numbers
     */
    public static List<Double> generateNumbers(int A, int B, int N){
        List<Double> numbers = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            numbers.add(A + Math.random() * (B - A));
        }
        return numbers;
    }

    /**
     * Generates file with random numbers N random numbers in the range [A,B).
     * @param A lower boundary
     * @param B upper boundary
     * @param N amount of numbers to generate
     * @return file with numbers
     */
    public File generateFile(int A, int B, int N) {
        return writeNumbersToFile(generateNumbers(A, B, N), new File("src/test/resources/Task4/numbers" + N + ".txt"));
    }

    public static void main(String[] args) {
        Task4 t = new Task4();
        File fileToRead = t.generateFile(-100,100,10000);

        File fileToWrite = new File("src/test/resources/Task4/result10000.txt");

        t.writeNumbersToFile(calculateTangent(t.readFile(fileToRead)), fileToWrite);
    }
}
