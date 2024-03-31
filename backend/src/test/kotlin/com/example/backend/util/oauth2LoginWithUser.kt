package com.example.backend.util

import com.example.backend.auth.model.CustomOAuth2User
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login
import java.util.*

fun oauth2LoginWithUser(
    user: CustomOAuth2User =
        CustomOAuth2User(
            authorities = listOf(),
            userId = "12345678-1234-1234-1234-123456789012",
            oid = "test-oid",
            name = "test-user-name",
        ),
): SecurityMockMvcRequestPostProcessors.OAuth2LoginRequestPostProcessor {
    return oauth2Login()
        .oauth2User(
            user,
        )
}
