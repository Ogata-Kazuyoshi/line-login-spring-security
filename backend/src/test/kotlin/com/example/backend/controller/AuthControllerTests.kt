package com.example.backend.controller

import com.example.backend.service.UserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity

@WebMvcTest(AuthController::class)
class AuthControllerTests {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @MockBean
    private lateinit var userService: UserService

//    @BeforeEach
//    fun setUp() {
//        mockMvc = MockMvcBuilders
//            .webAppContextSetup(webApplicationContext)
//            .apply(springSecurity())
//            .build()
//    }
//
//    @Test
//    @WithMockUser
//    fun `checkAuth endpoint should return OK status`() {
//        mockMvc.perform(get("/api/auth/check-auth"))
//            .andExpect(status().isOk)
//    }
}