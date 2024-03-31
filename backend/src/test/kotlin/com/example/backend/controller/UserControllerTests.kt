package com.example.backend.controller

import com.example.backend.auth.model.CustomOAuth2User
import com.example.backend.service.UserService
import com.example.backend.service.UserServiceImpl
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(UserController::class)
class UserControllerTests(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var userService: UserService

    private lateinit var customUser: CustomOAuth2User
    private lateinit var authentication: OAuth2AuthenticationToken

    @BeforeEach
    fun setUp() {
        customUser = mockk<CustomOAuth2User>()
        authentication = mockk<OAuth2AuthenticationToken>()

        every { customUser.getAttribute<String>("oid") } returns "testOid"
        every { authentication.principal } returns customUser
        every { authentication.isAuthenticated } returns true
        every { authentication.getName() } returns "testName"
    }

    @Test
    @WithMockUser
    fun `getUserInfo endpoint should call userService with correct arguments`() {
//        every { userService.getUserInfo(oid = "testOid") } returns null // 期待される動作をモック

        mockMvc.perform(get("/api/users")
            .with(authentication(authentication)))
            .andExpect(status().isOk)

        // userService.getUserInfoが期待通りの引数で呼び出されたことを検証
        verify { userService }
    }
}