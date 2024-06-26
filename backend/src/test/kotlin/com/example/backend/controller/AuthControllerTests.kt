package com.example.backend.controller

import com.example.backend.auth.model.CustomOAuth2User
import com.example.backend.service.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@WebMvcTest(AuthController::class)
class AuthControllerTests(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var userService: UserService

    private lateinit var authorities: List<SimpleGrantedAuthority>
    private lateinit var customUser: CustomOAuth2User
    private lateinit var authentication:OAuth2AuthenticationToken

    @BeforeEach
    fun setUp () {
        authorities = listOf(SimpleGrantedAuthority("dummy"))
        customUser = CustomOAuth2User(authorities, "testUserId", "testOid", "testName")

        authentication = mockk<OAuth2AuthenticationToken>()

        //認証プロセスで、下記が自動で呼ばれちゃうので、mockしておいて、適切なstubを返しておく
        every { authentication.principal } returns customUser
        every { authentication.isAuthenticated } returns true
        every { authentication.getName() } returns "testUserName"
    }

    @Test
    @WithMockUser
    fun `api-auth-check-authにアクセスするとstatus okであること`() {

        mockMvc.perform(get("/api/auth/check-auth")
            .with(authentication(authentication)))
            .andExpect(status().isOk)
    }
}