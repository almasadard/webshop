package com.fairgoods.webshop.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserPrincipal {

    private final Long userId;
    private final String email;
    private final boolean admin;

}
