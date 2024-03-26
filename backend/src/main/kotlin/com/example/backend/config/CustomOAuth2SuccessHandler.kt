package com.example.backend.config

import ResponseUserInfoEndpoint
//import com.example.backend.model.*
//import com.example.backend.service.UsersService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.exchange

@Component
class CustomOAuth2SuccessHandler(
    private val authorizationClientService: OAuth2AuthorizedClientService,
//    private val usersService: UsersService,
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?,
    ) {
//        val principal = authentication?.principal as OAuth2User
        val oAuth2AuthenticationToken = authentication as OAuth2AuthenticationToken
        println("oAuth2Auth : " + oAuth2AuthenticationToken)
        val clientRegistrationId = oAuth2AuthenticationToken.authorizedClientRegistrationId
        val authorizedClient =
            authorizationClientService.loadAuthorizedClient<OAuth2AuthorizedClient?>(
                clientRegistrationId,
                authentication.name,
            )
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        val accessToken = authorizedClient.accessToken.tokenValue
        headers.add("Authorization", "Bearer $accessToken")
        val entity = HttpEntity(null, headers)
        val userInfoEndpoint = authorizedClient.clientRegistration.providerDetails.userInfoEndpoint.uri
        val userInfoEndPointResBody: ResponseUserInfoEndpoint? = restTemplate.exchange<ResponseUserInfoEndpoint>(userInfoEndpoint, HttpMethod.GET, entity).body
        println("userInfoEndPoint : " + userInfoEndPointResBody)

        if (userInfoEndPointResBody != null) {
//            val userId = usersService.getUserInfoRecordId(UserInfoRecord(name = userInfoEndPointResBody.name,sub = userInfoEndPointResBody.sub))
            val userId = "hogehoge"
            val newAuthentication =
                OAuth2AuthenticationToken(
                    CustomOAuth2UserImpl(
//                        principal.authorities,
                        userId = userId,
                        name =  userInfoEndPointResBody.displayName!!,
                        sub = userInfoEndPointResBody.userId,
                    ),
                    authentication.authorities,
                    oAuth2AuthenticationToken.authorizedClientRegistrationId,
                )
            SecurityContextHolder.getContext().authentication = newAuthentication

//            response?.sendRedirect("\${environments.after-auth-redirect-url}")
            response?.sendRedirect("http://localhost:5173/mypage")
        } else {
            throw Error("!!! user info endpoint is not work. !!!")
        }
    }
}
