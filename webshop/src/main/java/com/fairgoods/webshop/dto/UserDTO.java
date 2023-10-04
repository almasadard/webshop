package com.fairgoods.webshop.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private boolean admin;
    private String title;
    private String firstname;
    private String lastname;
    private String streetname;
    private String postcode;
    private String city;
    private String email;
    private String password;
}