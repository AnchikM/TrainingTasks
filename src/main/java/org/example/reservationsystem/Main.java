package org.example.reservationsystem;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        RoomReservationSystem roomReservationSystem = new RoomReservationSystem();
        Scanner scanner = new Scanner(System.in);

        printCommands();

        while (scanner.hasNextLine()) {
            System.out.print("> ");

            String line = scanner.nextLine().trim();

            if (line.isEmpty()) continue;

            if (!processCommand(roomReservationSystem, line))
                break;
        }

        scanner.close();
        System.out.println("Room Reservation System closed.");
    }

    private static void printCommands() {
        System.out.println("Room Reservation System");
        System.out.println("Available commands:");

        System.out.println("BOOK <room> <startTime> <endTime>");
        System.out.println("CANCEL <reservationId>");
        System.out.println("LIST <room>");
        System.out.println("EXIT");
    }

    private static boolean isValid(String command, int length) {
        return switch (command) {
            case "BOOK" -> length == 4;
            case "CANCEL", "LIST" -> length == 2;
            case "EXIT" -> length == 1;
            default -> true;
        };
    }

    private static boolean processCommand(RoomReservationSystem system, String line){
        String[] parts = line.split("\\s+", 4);
        String command = parts[0].toUpperCase();

        if (!isValid(command, parts.length)) {
            System.out.println("Invalid command");
            return true;
        }

        try {
            switch (command) {
                case "BOOK" -> {
                    boolean booked = system.book(parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                    System.out.println(booked ? "BOOKED" : "NOT BOOKED");
                    return true;
                }
                case "CANCEL" -> {
                    boolean canceled = system.cancel(Integer.parseInt(parts[1]));
                    System.out.println(canceled ? "CANCELLED" : "NOT CANCELLED");
                    return true;
                }
                case "LIST" -> {
                    List<Reservation> list = system.list(parts[1]);
                    for (Reservation reservation : list)
                        System.out.println(reservation);

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
        } catch (NumberFormatException e) {
            System.out.println("Invalid number");
            return true;
        }
    }
}
