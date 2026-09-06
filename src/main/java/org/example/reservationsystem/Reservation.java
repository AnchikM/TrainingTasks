package org.example.reservationsystem;

public class Reservation {

    private int id;
    private String room;
    private int startTime;
    private int endTime;

    public Reservation(int id, String room, int startTime, int endTime) {
        this.id = id;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return id + " " + room + " " + startTime + " " + endTime;
    }
}
