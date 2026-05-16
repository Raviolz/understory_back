package raviolz.understory_back.payloads;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorsDTO(
        String message,
        LocalDateTime timestamp,
        List<String> errors
) {
    public ErrorsDTO(String message, LocalDateTime timestamp) {
        this(message, timestamp, null);
    }
}