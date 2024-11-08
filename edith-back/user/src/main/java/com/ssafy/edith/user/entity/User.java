package com.ssafy.edith.user.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "`user`")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    private String vcsBaseUrl;
    private String password;
    private boolean vcs;
    private String vcsAccessToken;

    @Builder
    public User(String email, String password, boolean vcs, String vcsAccessToken, String vcsBaseUrl) {
        this.email = email;
        this.password = password;
        this.vcs = vcs;
        this.vcsAccessToken = vcsAccessToken;
        this.vcsBaseUrl = vcsBaseUrl;
    }
}
