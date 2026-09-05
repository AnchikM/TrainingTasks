package org.example.database;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Database database = new Database();
        String input;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter command: ");

        while (scanner.hasNextLine()) {
            input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }
            boolean shouldContinue = processCommand(input, database);

            if (!shouldContinue) {
                break;
            }
        }
    }

    private static boolean processCommand(String input, Database database) {
        String[] parts = input.split("\\s+");
        String command = parts[0].toUpperCase();

        if (!isValid(command, parts.length)) {
            System.out.println("Invalid command");
            return true;
        }

        switch (command) {
            case "SET" -> {
                database.set(parts[1],parts[2]);
                return true;
            }
            case "DELETE" -> {
                database.delete(parts[1]);
                return true;
            }
            case "GET" -> {
                String value = database.get(parts[1]);
                System.out.println(value == null ? "NULL" : value);
                return true;
            }
            case "GETCOUNT" -> {
                System.out.println(database.getCount(parts[1]));
                return true;
            }
            case "BEGIN" -> {
                database.begin();
                return true;
            }
            case "ROLLBACK" -> {
                if (!database.rollback()) {
                    System.out.println("NO TRANSACTION");
                }
                return true;
            }
            case "COMMIT" -> {
                if (!database.commit()) {
                    System.out.println("NO TRANSACTION");
                }
                return true;
            }
            case "EXIT" -> {
                return false;
            }
            default -> {
                System.out.println("Unknown command");
                return true;
            }
        }
    }

    private static boolean isValid(String command, int length) {
        return switch (command) {
            case "SET" -> length == 3;

            case "GET", "DELETE", "GETCOUNT" -> length == 2;

            case "BEGIN", "ROLLBACK", "COMMIT", "EXIT" -> length == 1;

            default -> true;
        };
    }
}