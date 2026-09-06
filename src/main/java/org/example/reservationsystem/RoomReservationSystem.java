package org.example.reservationsystem;

import java.util.*;

public class RoomReservationSystem {

    private final Map<Integer, Reservation> reservationById;
    private final Map<String, List<Reservation>> reservationByRoom;
    private int indexCount;


    public RoomReservationSystem() {
        this.reservationById = new HashMap<>();
        this.reservationByRoom = new HashMap<>();
        this.indexCount = 1;
    }

    public boolean book(String room, int startTime, int endTime) {
        if (startTime < 0 || endTime > 24 || startTime >= endTime) return false;
        List<Reservation> reservationList = this.reservationByRoom.get(room);
        if (Objects.nonNull(reservationList)) {
            for (Reservation rez : reservationList) {
                if (overlaps(startTime, endTime, rez))
                    return false;
            }
        }
        Reservation reservation = new Reservation(indexCount++, room, startTime, endTime);
        this.reservationById.put(reservation.getId(), reservation);
        this.reservationByRoom.computeIfAbsent(room, k -> new ArrayList<>()).add(reservation);
        return true;
    }

    public boolean cancel(int reservationId) {
        Reservation reservationToDelete = this.reservationById.get(reservationId);
        if (reservationToDelete == null) return false;

        this.reservationById.remove(reservationId);
        List<Reservation> reservationList = this.reservationByRoom.get(reservationToDelete.getRoom());
        reservationList.remove(reservationToDelete);
        if (reservationList.isEmpty())
            this.reservationByRoom.remove(reservationToDelete.getRoom());

        return true;
    }

    public List<Reservation> list(String room) {
        List<Reservation> reservationList = this.reservationByRoom.get(room);
        if (Objects.nonNull(reservationList)) {
            List<Reservation> result = new ArrayList<>(reservationList);
            result.sort(Comparator.comparingInt(Reservation::getStartTime));
            return result;
        }
        return List.of();
    }

    private boolean overlaps(int startTime, int endTime, Reservation reservation) {
        //start1 < end2 && start2 < end1  -  true - есть пересечение
        return startTime < reservation.getEndTime() && reservation.getStartTime() < endTime;
    }
}
