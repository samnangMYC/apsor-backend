package com.backend.apsor.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/public/ping")
    public String ping() {
        return "public ok";
    }

    @GetMapping("/user/me")
    public String me() {
        return "authenticated ok";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/ping")
    public String admin() {
        return "admin ok";
    }
}
