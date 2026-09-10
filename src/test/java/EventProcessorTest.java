import org.example.eventprocessor.EventProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventProcessorTest {

    private EventProcessor eventProcessor;

    @BeforeEach
    void setUp() {
        this.eventProcessor = new EventProcessor();
    }

    @Test
    void processValidEvent() {
        String result = eventProcessor.processEvent(21, 450, "abcd", 4);

        assertEquals("Event 21: average 4.0, window ends 450", result);
    }

    @Test
    void processInvalidChecksum() {
        String result = eventProcessor.processEvent(21, 450, "abcd", 5);

        assertEquals("Event 21: Invalid checksum", result);
    }

    @Test
    void calculateAverageForMultipleEvents() {
        eventProcessor.processEvent(21, 450, "abcd", 4);

        String result = eventProcessor.processEvent(22, 460, "abcde", 5);

        assertEquals("Event 22: average 4.5, window ends 460", result);
    }

    @Test
    void ignoreTooLateEvent() {
        eventProcessor.processEvent(21, 450, "abcd", 4);
        eventProcessor.processEvent(22, 460, "abcde", 5);
        eventProcessor.processEvent(23, 530, "abcdef", 7);

        String result = eventProcessor.processEvent(24, 460, "a", 7);

        assertEquals("Event 24: Ignored, too late received, window starts 470", result);
    }
}
