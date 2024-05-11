package com.example.backend.controller

import com.example.backend.auth.model.CustomOAuth2User
import com.example.backend.model.request.RequestTestBody
import com.example.backend.model.request.RequestUpdateUser
import com.example.backend.model.response.ResponceUserInfo
import com.example.backend.model.response.ResponseTestString
import com.example.backend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController (
    @Autowired val userService: UserService
) {
    @GetMapping
    fun getUserInfo (
        @AuthenticationPrincipal user: CustomOAuth2User
    ):ResponceUserInfo? {
        println("userOid :" + user.getAttribute("oid"))
        return userService.getUserInfo(oid = user.getAttribute("oid"))
    }

    @PostMapping
    fun updateUserInfo (
        @AuthenticationPrincipal user: CustomOAuth2User,
        @RequestBody reqbody: RequestUpdateUser
    ) {
        userService.updateUserInfo(oid = user.getAttribute("oid"), remark = reqbody.remark)
    }

    @PostMapping("/test")
    fun testBody (
        @AuthenticationPrincipal user:CustomOAuth2User,
        @RequestBody reqBody: RequestTestBody
    ):ResponseTestString {
        val user = user.getAttribute<String>("oid")
        return ResponseTestString("$user + reqBody = ${reqBody.bodyParam1} & ${reqBody.bodyParam2}")
    }

    @DeleteMapping("/test")
    fun testDelete (
        @AuthenticationPrincipal user:CustomOAuth2User,
        @RequestParam id:Int
    ){
        println("${id}でアクセスがありました")
//        return "$idを削除しますか？"
    }

    @DeleteMapping("/test/{pathId}")
    fun testGet (
        @AuthenticationPrincipal user:CustomOAuth2User,
        @PathVariable pathId:Int
    ):String {
        return "${pathId}でアクセスがありました"
    }
}