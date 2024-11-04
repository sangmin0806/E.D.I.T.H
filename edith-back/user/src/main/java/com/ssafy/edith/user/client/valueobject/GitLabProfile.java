package com.ssafy.edith.user.client.valueobject;

public record GitLabProfile(
        Long id,
        String username,
        String name,
        String avatar_url,
        String webUrl
){
}