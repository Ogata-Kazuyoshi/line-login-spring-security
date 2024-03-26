package com.example.backend.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
class SecurityConfiguration(
    val successHandler: CustomOAuth2SuccessHandler
) {

    @Value("\${environments.auth-provider-redirect-url}")
    lateinit var authenticationProviderRedirectUrl: String


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
                it.loginPage(this.authenticationProviderRedirectUrl) // 認証プロバイダへのリダイレクト先を設定
                it.successHandler(successHandler)
            }
        return http.build()
    }
}
