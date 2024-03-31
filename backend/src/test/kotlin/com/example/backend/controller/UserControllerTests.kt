package com.example.backend.controller

import com.example.backend.auth.model.CustomOAuth2User
import com.example.backend.model.response.ResponceUserInfo
import com.example.backend.service.UserService
import com.example.backend.util.oauth2LoginWithUser
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.mockito.Mockito.doNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTests() {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var service: UserService

    @Test
    fun `Getメソッドでアクセスするとサービス層のgetUserInfoメソッドを正しい引数で呼ぶ`() {
        Mockito.`when`(service.getUserInfo(any())).thenReturn(ResponceUserInfo())

        val testUser = CustomOAuth2User(
            authorities = listOf(),
            userId = "12345678-1234-1234-1234-123456789012",
            oid = "test-oid",
            name = "test-user-name",
        )

        // when
        mockMvc.perform(
            get("/api/users")
                .with(oauth2LoginWithUser(testUser))
        ).andExpect(status().isOk)

        // then
        Mockito.verify(service).getUserInfo("test-oid")

    }

    @Test
    fun `Postメソッドでアクセスするとサービス層のupdateUserInfoメソッドを正しい引数で呼ぶ`() {

        doNothing().`when`(service).updateUserInfo(anyString(), anyString())

        val testUser = CustomOAuth2User(
            authorities = listOf(),
            userId = "12345678-1234-1234-1234-123456789012",
            oid = "test-oid",
            name = "test-user-name",
        )

        // when
        mockMvc.perform(
            post("/api/users")
                .with(oauth2LoginWithUser(testUser))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                        {
                            "remark" : "test-update"
                        }
                    """
                )
        ).andExpect(status().isOk)

        // then
        Mockito.verify(service).updateUserInfo("test-oid","test-update")

    }
}