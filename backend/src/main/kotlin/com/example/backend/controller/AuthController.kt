package com.example.backend.controller

import com.example.backend.config.CustomOAuth2User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController {
    @GetMapping("/check-auth")
    fun checkAuth (
        @AuthenticationPrincipal user: CustomOAuth2User,
    ) {
        println("userId : " + user.getSub())
        println("checkAuthが叩かれました")
    }

}