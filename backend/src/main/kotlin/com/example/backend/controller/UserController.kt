package com.example.backend.controller

import com.example.backend.auth.model.CustomOAuth2User
import com.example.backend.model.request.RequestUpdateUser
import com.example.backend.model.response.ResponceUserInfo
import com.example.backend.service.UserService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UserController (
    val userService: UserService
) {
    @GetMapping("/users")
    fun getUserInfo (
        @AuthenticationPrincipal user: CustomOAuth2User
    ):ResponceUserInfo? {
        println("userInfo : " + user.getAttribute("oid"))
        return userService.getUserInfo(oid = user.getAttribute("oid"))
    }

    @PostMapping("/users")
    fun updateUserInfo (
        @AuthenticationPrincipal user: CustomOAuth2User,
        @RequestBody reqbody: RequestUpdateUser
    ) {
        userService.updateUserInfo(oid = user.getAttribute("oid"), remark = reqbody.remark)
    }
}