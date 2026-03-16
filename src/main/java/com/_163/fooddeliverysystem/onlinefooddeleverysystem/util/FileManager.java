package com._163.fooddeliverysystem.onlinefooddeleverysystem.util;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileManager is a utility class that handles reading and writing data to text files.
 *
 * Since this app does NOT use a database, all data (users, foods, orders, etc.)
 * is stored in plain .txt files in the project root directory.
 *
 * Each line in a file represents one record (e.g. one user, one food item).
 * Fields within a record are separated by commas: "F-1,Burger,5.99,Main Course"
 */
public class FileManager {

    /**
     * Resolves the full path to a data file.
     *
     * System.getProperty("user.dir") returns the directory where the app
     * was launched from (the project root), so files like "foods.txt" are
     * always found in the right place whether run from IntelliJ or terminal.
     *
     * @param filename  the name of the file, e.g. "foods.txt"
     * @return          the full Path to that file
     */
    private static Path resolvePath(String filename) {
        String baseDir = System.getProperty("user.dir");
        return Paths.get(baseDir, "db", filename);
    }

    /**
     * Reads all non-empty lines from a file and returns them as a List.
     *
     * If the file doesn't exist yet (first run), it returns an empty list
     * instead of throwing an error — so the app starts cleanly.
     *
     * @param filename  the data file to read (e.g. "users.txt")
     * @return          a list of non-blank lines from the file
     */
    public static List<String> readLines(String filename) {
        Path path = resolvePath(filename);
        List<String> lines = new ArrayList<>();

        // If the file doesn't exist, just return empty list (no crash)
        if (!Files.exists(path)) {
            return lines;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {   // skip blank lines
                    lines.add(line.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file " + path + ": " + e.getMessage());
        }
        return lines;
    }

    /**
     * Overwrites a file completely with the given list of lines.
     *
     * Used when we need to update or delete a record — we rewrite all lines
     * except the one we want to remove/change.
     *
     * @param filename  the data file to overwrite
     * @param lines     the new list of lines to write
     */
    public static void writeLines(String filename, List<String> lines) {
        Path path = resolvePath(filename);
        try {
            // Create parent directories if they don't exist
            Files.createDirectories(path.getParent());

            // Write all lines (overwrites existing content)
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing file " + path + ": " + e.getMessage());
        }
    }

    /**
     * Appends a single new line to the end of a file.
     *
     * Used when adding a new record (new user, food item, order, etc.).
     * Much faster than rewriting the whole file just to add one line.
     *
     * @param filename  the data file to append to
     * @param line      the new record line to add
     */
    public static void appendLine(String filename, String line) {
        Path path = resolvePath(filename);
        try {
            Files.createDirectories(path.getParent());

            // StandardOpenOption.APPEND means we add to the end, not overwrite
            // StandardOpenOption.CREATE means create the file if it doesn't exist
            try (BufferedWriter writer = Files.newBufferedWriter(path,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error appending to file " + path + ": " + e.getMessage());
        }
    }
}
