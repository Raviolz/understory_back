package raviolz.understory_back.payloads.responses;

import raviolz.understory_back.entities.User;

import java.util.UUID;

public record UserResponseDTO(
        UUID userId,
        String username,
        String name,
        String surname,
        String email,
        String avatarUrl,
        int xp,
        int level,
        String role
) {
    public static UserResponseDTO fromEntity(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getSurname(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getXp(),
                user.getLevel(),
                user.getRole().getCode()
        );
    }
}
