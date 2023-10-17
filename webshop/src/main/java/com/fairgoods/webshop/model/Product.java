package com.fairgoods.webshop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;


@Entity(name="product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column (name = "productname")
    private String productname;

    @Length(min = 10, max = 200)
    @Column (name = "description")
    private String description;

    @Positive
    @Column (name = "price")
    private double price;

    @NotNull
    @Column (name = "quantity")
    private int quantity;

    @Column(name = "imageUrl")
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;
}