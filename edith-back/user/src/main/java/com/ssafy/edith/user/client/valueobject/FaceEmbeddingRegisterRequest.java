package com.ssafy.edith.user.client.valueobject;

public record FaceEmbeddingRegisterRequest(
        Long userId,
        float[] embeddingVector) {

}
