package com.example.backend.auth.handler.common

import com.example.backend.auth.model.CustomOAuth2User
import com.example.backend.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User

abstract class CommonAuthenticationSuccessHandler(
    private val userService: UserService,
    private val clientRegistrationId: String
) : AppAuthenticationSuccessHandler {

    override fun supports(oauth2Authentication: OAuth2AuthenticationToken): Boolean {
        return clientRegistrationId == oauth2Authentication.authorizedClientRegistrationId
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val principal = authentication.principal as OAuth2User
        println("principal : $principal")
        val oAuth2AuthenticationToken = authentication as OAuth2AuthenticationToken
        val oid = getOid(principal)
        val displayName = getDisplayName(principal)
        val res = userService.getOrCreateUserService(oid = oid, name = displayName)
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

    abstract fun getOid(principal: OAuth2User): String
    abstract fun getDisplayName(principal: OAuth2User): String
}