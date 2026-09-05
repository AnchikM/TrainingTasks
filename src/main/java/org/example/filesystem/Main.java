package org.example.filesystem;

import org.example.filesystem.exception.DirectoryNotFoundException;
import org.example.filesystem.exception.FileNotFoundException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);

        printCommands();

        while (scanner.hasNextLine()) {
            System.out.print("> ");

            String line = scanner.nextLine().trim();

            if (line.isEmpty()) continue;

            if (!processCommand(fileSystem, line))
                break;
        }

        scanner.close();
        System.out.println("File system closed.");
    }

    private static boolean processCommand(FileSystem fileSystem, String line) {
        String[] parts = line.split("\\s+", 3);
        String command = parts[0].toUpperCase();

        if (command.equals("EXIT"))
            return false;

        try {
            executeCommand(fileSystem, command, parts);
        } catch (DirectoryNotFoundException | FileNotFoundException e) {
            System.out.println("ERROR: " + e.getMessage());
        }

        return true;
    }

    private static void executeCommand(FileSystem fileSystem, String command, String[] parts) {
        switch (command) {
            case "MKDIR" -> {
                if (isInvalid(parts, 2, "MKDIR <path>"))
                    return;

                System.out.println(fileSystem.createDirectory(parts[1]));
            }
            case "CREATE" -> {
                if (isInvalid(parts, 3, "CREATE <path> <content>"))
                    return;

                System.out.println(fileSystem.createFile(parts[1], parts[2]));
            }
            case "READ" -> {
                if (isInvalid(parts, 2, "READ <path>"))
                    return;

                System.out.println(fileSystem.readFile(parts[1]));
            }
            case "DELETE" -> {
                if (isInvalid(parts, 2, "DELETE <path>"))
                    return;

                System.out.println(fileSystem.delete(parts[1]));
            }
            case "LIST" -> {
                if (isInvalid(parts, 2, "LIST <path>"))
                    return;

                System.out.println(fileSystem.list(parts[1]));
            }
            default -> System.out.println("ERROR: Unknown command: " + command);
        }
    }

    private static boolean isInvalid(String[] parts, int expectedLength, String usage) {
        if (parts.length == expectedLength) return false;

        System.out.println("ERROR: Invalid command. Usage: " + usage);
        return true;
    }

    private static void printCommands() {
        System.out.println("In-Memory File System");
        System.out.println("Available commands:");
        System.out.println("MKDIR <path>");
        System.out.println("CREATE <path> <content>");
        System.out.println("READ <path>");
        System.out.println("DELETE <path>");
        System.out.println("LIST <path>");
        System.out.println("EXIT");
    }
}
