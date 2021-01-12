package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Setter
    @Column(name = "name", length = 30, nullable = false)
    private String name;

    @Column(name = "pw", length = 100, nullable = false)
    private String pw;

    @Column(name = "userid", length = 30, nullable = false)
    private String userid;

    @Setter
    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Setter
    @Column(name = "email", length = 100, nullable = true)
    private String email;

    @Builder
    public User(String name, String pw, String userid, Role role, String email) {
        this.name = name;
        this.pw = pw;
        this.userid  = userid;
        this.role = role;
        this.email = email;
    }
}