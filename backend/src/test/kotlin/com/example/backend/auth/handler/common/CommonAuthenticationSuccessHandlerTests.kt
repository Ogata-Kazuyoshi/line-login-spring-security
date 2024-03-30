package com.example.backend.auth.handler.common

import com.example.backend.auth.model.CustomOAuth2User
import com.example.backend.model.response.ResponceUserInfo
import com.example.backend.service.UserService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User

class CommonAuthenticationSuccessHandlerTests {
    private lateinit var userService: UserService
    private lateinit var request: HttpServletRequest
    private lateinit var response: HttpServletResponse
    private lateinit var authentication: Authentication
    private lateinit var oAuth2User: OAuth2User
    private lateinit var oAuth2AuthenticationToken: OAuth2AuthenticationToken
    private lateinit var handler: CommonAuthenticationSuccessHandler
    private lateinit var principal: OAuth2User
    private lateinit var securityContext: SecurityContext

    @BeforeEach
    fun setUp() {
        userService = mockk()
        request = mockk(relaxed = true)
        response = mockk(relaxed = true)
//        authentication = mockk()
        oAuth2User = mockk()
        oAuth2AuthenticationToken = mockk()

        // OAuth2AuthenticationTokenとしてauthenticationをモックする　←型キャストでエラーが出たので、キャストできる形にする
        authentication = mockk<OAuth2AuthenticationToken>()

        // oAuth2UserのgetAuthoritiesメソッドの振る舞いを定義
        every { oAuth2User.getAuthorities() } returns emptyList()

        // OAuth2AuthenticationTokenのgetAuthoritiesメソッドの振る舞いを定義
        every { authentication.getAuthorities() } returns emptyList()

//securityContextHolderへのセットを保証するテスト用。うまく行ってない
//        principal = mockk()
//        securityContext = mockk()
//
//        // SecurityContextHolderをモック化
//        SecurityContextHolder.setContext(securityContext)

        handler = object : CommonAuthenticationSuccessHandler(userService, "testClient") {
            override fun getOid(principal: OAuth2User): String = "testOid"
            override fun getDisplayName(principal: OAuth2User): String = "testDisplayName"
        }
    }

    @Nested
    inner class `supportsメソッドは` () {

        @Test
        fun `引数で受け取った値と、authorizedClientResistrationIdが一致する場合trueを返す`() {
            every { oAuth2AuthenticationToken.authorizedClientRegistrationId } returns "testClient"

            val result = handler.supports(oAuth2AuthenticationToken)

            assertTrue(result)
        }

        @Test
        fun `引数で受け取った値と、authorizedClientResistrationIdが一致しない場合falseを返す`() {
            every { oAuth2AuthenticationToken.authorizedClientRegistrationId } returns "dummytestClient"

            val result = handler.supports(oAuth2AuthenticationToken)

            assertFalse(result)
        }
    }

    @Nested
    inner class `onAuthenticationSuccessメソッドは` () {
        @Test
        fun `userServiceのgetOrCreatUserServiceを正しい引数で呼んでいること`() {
            every { authentication.principal } returns oAuth2User
            every { (authentication as OAuth2AuthenticationToken).authorizedClientRegistrationId } returns "testClient"
            every { userService.getOrCreateUserService(any(), any()) } returns ResponceUserInfo()
            every { response.sendRedirect(any()) } returns Unit


            handler.onAuthenticationSuccess(request, response, authentication)

            verify { userService.getOrCreateUserService("testOid","testDisplayName") }
        }
    }

}