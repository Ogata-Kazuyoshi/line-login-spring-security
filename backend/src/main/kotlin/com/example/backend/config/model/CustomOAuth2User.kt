package com.example.backend.config.model

import org.springframework.security.core.AuthenticatedPrincipal
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.io.Serializable
import java.util.UUID


class CustomOAuth2User(
    private val authorities: Collection<GrantedAuthority>,
    private val userId: String,
    private val oid: String,
    private val name: String,
) : OAuth2User {
    private val attributes: Map<String, Any> = mapOf("userId" to userId, "oid" to oid, "name" to name)

    override fun getName(): String {
        return name
    }

    override fun getAttributes(): Map<String, Any> {
        return attributes
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }
}
