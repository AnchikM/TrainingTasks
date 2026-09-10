package org.example.eventprocessor;

import java.util.Comparator;
import java.util.PriorityQueue;

public class EventProcessor {
    private final PriorityQueue<Event> events;
    private int totalPayloadLengthQueue;
    private int windowEnd = Integer.MIN_VALUE;

//    private static final String RED = "\u001B[31m";
//    private static final String YELLOW = "\u001B[33m";
//    private static final String GREEN = "\u001B[32m";
//    private static final String RESET = "\u001B[0m";

    public EventProcessor() {
        this.events = new PriorityQueue<>(Comparator.comparingInt(Event::timestamp));
    }

    public String processEvent(Integer id, Integer timestamp, String payload, Integer checksum) {
        if (!isChecksumValid(payload, checksum)) {
            //System.out.println(RED + "Event " + id + ": Invalid checksum" + RESET);
            return "Event " + id + ": Invalid checksum";
        }

        if (isTooLate(timestamp)) {
            //System.out.println(YELLOW + "Event " + id +
            //       ": Ignored, too late received, window starts " + (this.windowEnd - 60) + RESET);
            return "Event " + id + ": Ignored, too late received, window starts " + (this.windowEnd - 60);
        }

        this.events.offer(new Event(id, timestamp, payload, checksum));
        this.totalPayloadLengthQueue += payload.length();
        this.windowEnd = Math.max(windowEnd, timestamp);

        removeExpiredEventsIfNeeded();

        double average = (double) this.totalPayloadLengthQueue / events.size();

//        System.out.println(GREEN + "Event " + id +
//                ": average " + average +
//                ", window ends " + this.windowEnd + RESET);
        return "Event " + id + ": average " + average + ", window ends " + this.windowEnd;
    }

    private boolean isChecksumValid(String payload, Integer checksum) {
        int sum = 0;
        for (char c : payload.toCharArray()) {
            sum += c;
        }
        return (sum % 10) == checksum;

    }

    private boolean isTooLate(Integer timestamp) {
        if (events.isEmpty()) return false;

        return timestamp < windowEnd - 60;
    }

    private void removeExpiredEventsIfNeeded() {
        while (!this.events.isEmpty() && this.events.peek() != null && this.events.peek().timestamp() < windowEnd - 60) {
            Event event = this.events.poll();
            this.totalPayloadLengthQueue -= event.payload().length();
        }
    }
}
