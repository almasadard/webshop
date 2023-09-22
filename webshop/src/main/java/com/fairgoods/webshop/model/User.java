package com.fairgoods.webshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class User {
    @Positive
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="title")
    private String title;
    @NotBlank
    @Column(name="firstname")
    private String firstname;

    @NotBlank
    @Column(name="lastname")
    private String lastname;

    @Column(name="streetname")
    private String streetname;

    @Column(name="postcode")
    private String postcode;

    @Column(name="city")
    private String city;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    public User(long l, String john) {
    }
}
