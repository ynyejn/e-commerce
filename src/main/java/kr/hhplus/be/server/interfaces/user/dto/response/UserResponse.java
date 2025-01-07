package kr.hhplus.be.server.interfaces.user.dto.response;

public record UserResponse(
        Long userId,
        String name,
        Long point
) {
}
