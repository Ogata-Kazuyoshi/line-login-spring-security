package com.example.backend.config.handler


import com.example.backend.config.model.CustomOAuth2User
import com.example.backend.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User

open class LineAuthenticationSuccessHandler(
    val userService: UserService
) : AppAuthenticationSuccessHandler {

    override fun supports(oauth2Authentication: OAuth2AuthenticationToken): Boolean {
        return "line" == oauth2Authentication.authorizedClientRegistrationId
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal as OAuth2User
        val oAuth2AuthenticationToken = authentication as OAuth2AuthenticationToken
        val oid = principal.getAttribute<String>("userId") ?: throw Exception("There is no userId")
        val displayName = principal.getAttribute<String>("displayName") ?: throw Exception("There is no name")
        val res = userService.getOrCreateUserService(oid=oid, name = displayName)
        val newAuthentication = OAuth2AuthenticationToken(
            CustomOAuth2User(
                userId = res.id.toString(),
                oid = oid,
                name = displayName,
                authorities = principal.authorities,
            ),
            authentication.authorities,
            oAuth2AuthenticationToken.authorizedClientRegistrationId
        )
        SecurityContextHolder.getContext().authentication = newAuthentication
        val redirectUrl = System.getenv("AFTER_AUTH_REDIRECT_URL") ?: "hogehoge"
        response.sendRedirect(redirectUrl)
    }
}