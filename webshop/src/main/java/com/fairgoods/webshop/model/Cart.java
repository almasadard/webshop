package com.fairgoods.webshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity(name = "cart")
@Data
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @OneToMany(mappedBy = "cart")
    @JsonBackReference
    private Set<Position> positions;

    public Cart(User user) {
        this.user = user;
    }
}