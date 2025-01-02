package kr.hhplus.be.server.interfaces.dto.request;

public record OrderProductRequest (
        Long productId,
        Long quantity
){}
