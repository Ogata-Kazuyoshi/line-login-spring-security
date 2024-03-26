package com.example.backend.config

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User


interface CustomOAuth2User  {
    fun getUserId(): String
    fun getSub(): String
}

class CustomOAuth2UserImpl(
//    private val authorities: Collection<GrantedAuthority>,
    private val userId: String,
    private val sub: String,
    private val name: String?,
) : OAuth2User, CustomOAuth2User {
    private val attributes: Map<String, Any>

    init {
        attributes = mapOf("userId" to userId, "sub" to sub)
    }

    override fun getUserId(): String {
        return userId
    }

    override fun getSub(): String {
        return sub
    }

    override fun getName(): String? {
        return name
    }

    override fun getAttributes(): Map<String, Any> {
        return attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
//        return authorities
        return emptyList()
    }
}
