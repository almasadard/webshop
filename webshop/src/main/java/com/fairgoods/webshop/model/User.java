package com.fairgoods.webshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Positive
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="admin")
    private boolean admin;

    @Column(name="title")
    private String title;

    @Column(name="firstname")
    private String firstname;

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

    //Password BCrypt Handling
    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
