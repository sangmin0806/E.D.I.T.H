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

    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    private String vcsBaseUrl;
    private String vcsAccessToken;

    @Builder
    public User(String email, String password, String vcsBaseUrl, String vcsAccessToken) {
        this.email = email;
        this.password = password;
        this.vcsBaseUrl = vcsBaseUrl;
        this.vcsAccessToken = vcsAccessToken;
    }
}
