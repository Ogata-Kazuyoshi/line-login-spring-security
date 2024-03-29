package com.example.backend.controller

import com.example.backend.auth.model.CustomOAuth2User
import com.example.backend.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
class AuthController (
    val userService: UserService
) {
    @GetMapping("/check-auth")
    fun checkAuth (
        @AuthenticationPrincipal user: CustomOAuth2User,
    ) {
        println("userId : " + user.getAttribute("oid"))
    }

}