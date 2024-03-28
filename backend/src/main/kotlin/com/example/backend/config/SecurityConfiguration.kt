package com.example.backend.config

import com.example.backend.config.handler.AppCustomeAuthenticationSuccessHandler
import com.example.backend.config.handler.GithubAuthenticationSuccessHandler
import com.example.backend.config.handler.LineAuthenticationSuccessHandler
import com.example.backend.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

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
