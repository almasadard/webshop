package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @PostMapping("/secured")
    public String secured(@RequestBody @Valid UserPrincipal principal){
        System.out.println("Das ist das principal: " + principal);
        return "If you can see this, you're logged in as " + principal.getEmail()
                + " with UserId: " + principal.getUserId();
    }
}
