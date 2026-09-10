package org.example.eventprocessor;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        EventProcessor eventProcessor = new EventProcessor();
        String input;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter event in format: <identifier>,<timestamp>,<payload>,<checksum>");

        while (scanner.hasNextLine()) {
            input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }
            boolean shouldContinue = processCommand(input, eventProcessor);

            if (!shouldContinue) {
                break;
            }
        }
    }

    private static boolean processCommand(String input, EventProcessor eventProcessor) {
        String[] parts = input.split(",");

        if (parts.length != 4) {
            System.out.println("Invalid command. Process is Finished");
            return false;
        }
        try {
            int identifier = Integer.parseInt(parts[0].trim());
            int timestamp = Integer.parseInt(parts[1].trim());
            String payload = parts[2].trim();
            int checksum = Integer.parseInt(parts[3].trim());

            System.out.println(eventProcessor.processEvent(identifier, timestamp, payload, checksum));
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Identifier, timestamp and checksum must be integers.");
        }

        return true;

    }

}
