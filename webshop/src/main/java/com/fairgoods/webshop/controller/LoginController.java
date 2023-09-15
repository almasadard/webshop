package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping("/secured")
    public String secured(@AuthenticationPrincipal UserPrincipal principal){
        return "If you can see this, you're logged in as " + principal.getEmail()
                + " with UserId: " + principal.getUserId();
    }
}
