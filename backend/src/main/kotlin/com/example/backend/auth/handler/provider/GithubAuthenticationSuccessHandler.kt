package com.example.backend.auth.handler.provider

import com.example.backend.auth.handler.common.CommonAuthenticationSuccessHandler
import com.example.backend.service.UserService
import org.springframework.security.oauth2.core.user.OAuth2User

class GithubAuthenticationSuccessHandler(userService: UserService) : CommonAuthenticationSuccessHandler(userService, "github") {
    override fun getOid(principal: OAuth2User): String = principal.getAttribute<String>("node_id") ?: throw Exception("There is no userId")
    override fun getDisplayName(principal: OAuth2User): String = principal.getAttribute<String>("login") ?: throw Exception("There is no name")
}