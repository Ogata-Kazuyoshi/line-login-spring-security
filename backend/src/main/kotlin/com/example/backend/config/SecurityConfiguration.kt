//package com.example.backend.config
//
//import com.example.backend.config.handler.AppCustomeAuthenticationSuccessHandler
//import com.example.backend.config.handler.LineAuthenticationSuccessHandler
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
//import org.springframework.security.web.SecurityFilterChain
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler
//
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//@Configuration
//class SecurityConfiguration(
////    val successHandler: CustomOAuth2SuccessHandler
//) {
////
////    @Value("\${environments.auth-provider-redirect-url}")
////    lateinit var authenticationProviderRedirectUrl: String
//
//    @Bean
//    fun authenticationSuccessHandler(): AuthenticationSuccessHandler {
//        return AppCustomeAuthenticationSuccessHandler(
//            listOf(
//                LineAuthenticationSuccessHandler()
//            )
//        )
//    }
//
//
//    @Bean
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        http
//            .authorizeHttpRequests {
////                it.requestMatchers("/api/**")
////                    .authenticated()
//                it.anyRequest()
//                    .permitAll()
//            }
//            .oauth2Login {
//                it.loginPage(this.authenticationProviderRedirectUrl) // 認証プロバイダへのリダイレクト先を設定
//                it.successHandler(authenticationSuccessHandler())
////                it.failureHandler { _, response, exception ->
////                    response?.sendRedirect("/?error=${exception.message}")
////                }
////                it.redirectionEndpoint { configurer ->
////                    configurer.baseUri("/api/login/oauth2/code/line")
////                }
////                it.authorizationEndpoint {
////                    it.authorizationRequestResolver(OAuth2AuthorizationRequestResolver { request ->
////                        // ここにOAuth2AuthorizationRequestResolverの実装を記述
////                        // 例: OAuth2AuthorizationRequestをカスタマイズする
////                    })
////                }
//            }
//
//        http.csrf {
//            it.disable()// H2DBデバッグ用
//        }
//        return http.build()
//    }
//}
//



package com.example.backend.config

import com.example.backend.config.handler.AppCustomeAuthenticationSuccessHandler
import com.example.backend.config.handler.GithubAuthenticationSuccessHandler
import com.example.backend.config.handler.LineAuthenticationSuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

@EnableWebSecurity
@Configuration
class SecurityConfiguration {

    @Bean
    fun authenticationSuccessHandler(): AuthenticationSuccessHandler {
        return AppCustomeAuthenticationSuccessHandler(
            listOf(
                LineAuthenticationSuccessHandler(),
                GithubAuthenticationSuccessHandler()
            )
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .oauth2Login { oauth2Login ->
                oauth2Login.successHandler(authenticationSuccessHandler())
            }
        return http.build()
    }
}
