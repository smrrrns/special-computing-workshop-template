package ru.spbu.apcyb.svp.tasks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 *  Task 3.
 */
public class ScanDirectoryTree {

    public ScanDirectoryTree() {
    }

    /**
     *  Writes to the file a list of all files and folders in the given directory.
     */
    public void scanDirectory(String filePath, String directoryPath) throws FileNotFoundException {
        File file = fileCheck(filePath);
        File directory = directoryCheck(directoryPath);

        writeToFile(getFilesList(directory), file);
    }

    /**
     *  Checks if string is the file path, returns the file if true.
     */
    public File fileCheck(String filePathString) throws FileNotFoundException {
        Path filePath = Paths.get(filePathString);
        File file = filePath.toFile();

        if (!file.exists()) {
            throw new FileNotFoundException("There is no such file");
        }
        if (file.isDirectory()) {
            throw new RuntimeException("File path is expected, not directory path");
        }
        return file;
    }

    /**
     *  Checks if string is the directory path, returns the directory if true.
     */
    public File directoryCheck(String directoryPathString) throws FileNotFoundException {
        Path directoryPath = Paths.get(directoryPathString);
        File directory = directoryPath.toFile();

        if (directory.isFile()) {
            throw new RuntimeException("Directory is expected, not file");
        } else if (!directory.exists()) {
            throw new FileNotFoundException("There is no such directory");
        }
        return directory;
    }

    /**
     *  Gets a list of all files and folders in the directory.
     */
    public List<String> getFilesList(File directory) {
        List<String> files = new ArrayList<>();
        String[] directoryList = directory.list();

        if (directoryList == null) {
            throw new RuntimeException("Directory is empty");
        } else if (directoryList.length == 0) {
            files.add(directory.getPath());
        }
        for (String dir : directoryList) {
            String tempPath = directory.getPath() + File.separator + dir;
            File tempFile = new File(tempPath);

            if (tempFile.isFile()) {
                files.add(tempPath);
            } else {
                files.addAll(getFilesList(tempFile));
            }
        }
        return files;
    }

    /**
     *  Writes the list of strings to the file.
     */
    public void writeToFile(List<String> files, File file) {
        try (FileWriter writer = new FileWriter(file, false)) {
            for (String f : files) {
                writer.write(f);
                writer.append('\n');
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to file");
        }
    }

    /**
     *  Expects 2 strings: file path and directory path.
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 2) {
            throw new IndexOutOfBoundsException("Two string arguments are expected");
        }

        ScanDirectoryTree scan = new ScanDirectoryTree();
        scan.scanDirectory(args[0], args[1]);
    }
}
