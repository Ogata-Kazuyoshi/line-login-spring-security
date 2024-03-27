package com.example.backend.config.handler


import com.example.backend.config.model.CustomOAuth2User
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User

open class LineAuthenticationSuccessHandler(
) : AppAuthenticationSuccessHandler {

    override fun supports(oauth2Authentication: OAuth2AuthenticationToken): Boolean {
        return "line" == oauth2Authentication.authorizedClientRegistrationId
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        println("ここにはきてる!!LINE")
        val principal = authentication.principal as OAuth2User
        val oAuth2AuthenticationToken = authentication as OAuth2AuthenticationToken
        val userId = "hogehoge"
        val oid = principal.getAttribute<String>("userId") ?: throw Exception("There is no userId")
        val displayName = principal.getAttribute<String>("displayName") ?: throw Exception("There is no name")
        val newAuthentication = OAuth2AuthenticationToken(
            CustomOAuth2User(
                userId = userId,
                oid = oid,
                name = displayName,
                authorities = principal.authorities,
            ),
            authentication.authorities,
            oAuth2AuthenticationToken.authorizedClientRegistrationId
        )
        SecurityContextHolder.getContext().authentication = newAuthentication
        response.sendRedirect("http://localhost:5173/mypage")
    }
}