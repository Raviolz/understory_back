package raviolz.understory_back.payloads;

import jakarta.validation.constraints.Size;

public record UserNoteDTO(
        @Size(max = 1000, message = "User note cannot exceed 1000 characters")
        String userNote
) {
}