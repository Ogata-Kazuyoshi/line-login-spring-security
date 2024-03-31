package com.example.backend.auth.handler.provider

import com.example.backend.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.core.user.OAuth2User

class LineAuthenticationSuccessHandlerTests {
    private lateinit var lineAuth:LineAuthenticationSuccessHandler
    private lateinit var userService:UserService
    private lateinit var principal:OAuth2User

    @BeforeEach
    fun setUp () {
        userService = mockk()
        principal = mockk()
        lineAuth = LineAuthenticationSuccessHandler(userService)
    }

    @Test
    fun `getOidメソッドは正しい引数でgetAttributeを呼んでいて、正しい値を返す`(){
        every { principal.getAttribute<String>(any()) } returns "hogehoge"

        val res = lineAuth.getOid(principal)

        verify { principal.getAttribute<String>("userId") }
        assertEquals("hogehoge",res)
    }

    @Test
    fun `getDisplayNameメソッドは正しい引数でgetAttributeを呼んでいて、正しい値を返す`(){
        every { principal.getAttribute<String>(any()) } returns "hogehoge"

        val res = lineAuth.getDisplayName(principal)

        verify { principal.getAttribute<String>("displayName") }
        assertEquals("hogehoge",res)
    }
}