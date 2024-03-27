package com.example.backend.config.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

interface AppAuthenticationSuccessHandler :
    AuthenticationSuccessHandler {
    fun supports(oauth2Authentication: OAuth2AuthenticationToken): Boolean
}

@Component
class AppCustomeAuthenticationSuccessHandler(
    private val delegatees: List<AppAuthenticationSuccessHandler>
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        for (delegatee in delegatees) {
            if (delegatee.supports(authentication as OAuth2AuthenticationToken)) {
                delegatee.onAuthenticationSuccess(request, response, authentication)
            }
        }
    }
}