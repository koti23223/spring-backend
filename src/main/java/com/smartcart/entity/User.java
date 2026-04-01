package com.smartcart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // ✅ OPTIONAL (good for profile)
//    @Column(length = 15)
//    private String phone;

    // ✅ ADDRESS
    @Column(length = 500)
    private String address;

    // ✅ BIO
    @Column(length = 1000)
    private String bio;

    // ✅ PROFILE IMAGE URL
    @Column(length = 500)
    private String profileImage;
}