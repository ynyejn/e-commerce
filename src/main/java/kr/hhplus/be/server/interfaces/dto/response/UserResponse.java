package kr.hhplus.be.server.interfaces.dto.response;

public record UserResponse(
        Long userId,
        String name,
        Long point
) {
}
