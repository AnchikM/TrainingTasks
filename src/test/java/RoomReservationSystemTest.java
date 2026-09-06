import org.example.reservationsystem.Reservation;
import org.example.reservationsystem.RoomReservationSystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomReservationSystemTest {

    RoomReservationSystem system;

    @BeforeEach
    void setUp() {
        system = new RoomReservationSystem();
    }

    @Test
    void bookSuccess() {
        assertTrue(system.book("A", 10, 12));
    }

    @Test
    void bookOverlappingReservation() {
        system.book("A", 11, 13);
        assertFalse(system.book("A", 10, 12));
    }

    @Test
    void bookAdjacentReservations() {
        assertTrue(system.book("A", 10, 12));
        assertTrue(system.book("A", 12, 14));
    }

    @Test
    void bookInvalidTime() {
        assertFalse(system.book("A", -1, 12));
    }

    @Test
    void cancelExistingReservation() {
        system.book("A", 10, 12);
        assertTrue(system.cancel(1));
    }

    @Test
    void cancelUnknownReservation() {
        assertFalse(system.cancel(1));
    }

    @Test
    void listReservationsSortedByStartTime() {
        system.book("A", 15, 17);
        system.book("A", 10, 12);
        system.book("A", 12, 14);

        List<Reservation> result = system.list("A");

        assertEquals(3, result.size());
        assertEquals(10, result.get(0).getStartTime());
        assertEquals(12, result.get(1).getStartTime());
        assertEquals(15, result.get(2).getStartTime());
    }

    @Test
    void reservationsInDifferentRoomsCanOverlap() {
        assertTrue(system.book("A", 10, 12));
        assertTrue(system.book("B", 10, 12));
    }
}
