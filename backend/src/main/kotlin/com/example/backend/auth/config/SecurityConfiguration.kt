package com.example.backend.auth.config

import com.example.backend.auth.handler.common.AppCustomeAuthenticationSuccessHandler
import com.example.backend.auth.handler.provider.GithubAuthenticationSuccessHandler
import com.example.backend.auth.handler.provider.LineAuthenticationSuccessHandler
import com.example.backend.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
class SecurityConfiguration (
    val userService: UserService
) {
    @Bean
    fun authenticationSuccessHandler(): AuthenticationSuccessHandler {
        return AppCustomeAuthenticationSuccessHandler(
            listOf(
                LineAuthenticationSuccessHandler(userService),
                GithubAuthenticationSuccessHandler(userService)
            )
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeHttpRequests {
                it.requestMatchers("/api/**")
                    .authenticated()
                it.anyRequest()
                    .permitAll()
            }
            .oauth2Login {
                it.successHandler(authenticationSuccessHandler())
            }
        return http.build()
    }
}
