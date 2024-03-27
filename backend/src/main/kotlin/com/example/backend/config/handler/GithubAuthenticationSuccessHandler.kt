package com.example.backend.config.handler


import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User

open class GithubAuthenticationSuccessHandler(
) : AppAuthenticationSuccessHandler {

    override fun supports(oauth2Authentication: OAuth2AuthenticationToken): Boolean {
        return "github" == oauth2Authentication.authorizedClientRegistrationId
    }

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        println("ここにはきてる!!Github")
//        val principal = authentication.principal as OAuth2User
//        val oAuth2AuthenticationToken = authentication as OAuth2AuthenticationToken
//        val oauth2UserId = principal.getAttribute<String>("oid") ?: throw Exception("There is no oid")
//        val name = principal.getAttribute<String>("name") ?: throw Exception("There is no name")
//        val user = userService.getOrCreateUserByOauth2UserId(oauth2UserId, loginType = LoginType.AZURE_ACTIVE_DIRECTORY)
//        val newAuthentication = OAuth2AuthenticationToken(
//            EXPOAuth2User(
//                userId = user.userId,
//                name = name,
//                oid = oauth2UserId,
//                authorities = principal.authorities,
//            ),
//            authentication.authorities,
//            oAuth2AuthenticationToken.authorizedClientRegistrationId
//        )
//        SecurityContextHolder.getContext().authentication = newAuthentication
        response?.sendRedirect("http://localhost:5173/mypage")
    }
}