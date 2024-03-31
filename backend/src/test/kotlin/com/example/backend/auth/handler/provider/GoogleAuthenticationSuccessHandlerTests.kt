package com.example.backend.auth.handler.provider

import com.example.backend.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.oauth2.core.user.OAuth2User

class GoogleAuthenticationSuccessHandlerTests {
    private lateinit var googleAuth:GoogleAuthenticationSuccessHandler
    private lateinit var userService:UserService
    private lateinit var principal:OAuth2User

    @BeforeEach
    fun setUp () {
        userService = mockk()
        principal = mockk()
        googleAuth = GoogleAuthenticationSuccessHandler(userService)
    }

    @Test
    fun `clientResistrationedIdがgoogleであること` () {
        val resistrationedId = googleAuth.getClientRegistrationId()

        assertEquals("google",resistrationedId)
    }

    @Test
    fun `getOidメソッドは正しい引数でgetAttributeを呼んでいて、正しい値を返す`(){
        every { principal.getAttribute<String>(any()) } returns "hogehoge"

        val res = googleAuth.getOid(principal)

        verify { principal.getAttribute<String>("sub") }
        assertEquals("hogehoge",res)
    }

    @Test
    fun `getDisplayNameメソッドは正しい引数でgetAttributeを呼んでいて、正しい値を返す`(){
        every { principal.getAttribute<String>(any()) } returns "hogehoge"

        val res = googleAuth.getDisplayName(principal)

        verify { principal.getAttribute<String>("name") }
        assertEquals("hogehoge",res)
    }
}