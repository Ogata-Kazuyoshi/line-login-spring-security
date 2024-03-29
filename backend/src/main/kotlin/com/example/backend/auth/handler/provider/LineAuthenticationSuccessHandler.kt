package com.example.backend.auth.handler.provider

import com.example.backend.auth.handler.common.CommonAuthenticationSuccessHandler
import com.example.backend.service.UserService
import org.springframework.security.oauth2.core.user.OAuth2User

class LineAuthenticationSuccessHandler(userService: UserService) : CommonAuthenticationSuccessHandler(userService, "line") {
    override fun getOid(principal: OAuth2User): String = principal.getAttribute<String>("userId") ?: throw Exception("There is no userId")
    override fun getDisplayName(principal: OAuth2User): String = principal.getAttribute<String>("displayName") ?: throw Exception("There is no name")
}