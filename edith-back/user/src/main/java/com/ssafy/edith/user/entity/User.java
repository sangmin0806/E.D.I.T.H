package com.ssafy.edith.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`user`")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String gitlabBaseUrl;
    private String gitlabPersonalAccessToken;

    @Builder
    public User(String email, String password, String gitlabBaseUrl, String gitlabPersonalAccessToken) {
        this.email = email;
        this.password = password;
        this.gitlabBaseUrl = gitlabBaseUrl;
        this.gitlabPersonalAccessToken = gitlabPersonalAccessToken;
    }
}
