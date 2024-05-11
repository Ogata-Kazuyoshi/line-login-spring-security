package com.example.backend.auth.config

import com.example.backend.auth.handler.common.AppCustomeAuthenticationSuccessHandler
import com.example.backend.auth.handler.provider.GithubAuthenticationSuccessHandler
import com.example.backend.auth.handler.provider.GoogleAuthenticationSuccessHandler
import com.example.backend.auth.handler.provider.LineAuthenticationSuccessHandler
import com.example.backend.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

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
                GithubAuthenticationSuccessHandler(userService),
                GoogleAuthenticationSuccessHandler(userService)
            )
        )
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
//            .csrf().disable()
            .authorizeHttpRequests {
                it.requestMatchers("/api/**")
                    .authenticated()
                it.anyRequest()
                    .permitAll()
            }
            .oauth2Login {
                it.successHandler(authenticationSuccessHandler())
            }
            .logout {
                it.logoutUrl("/logout") // ログアウトのエンドポイントを指定
                it.logoutSuccessUrl("http://localhost:5173/front-login") // ログアウト成功後にリダイレクトするURLをフロントエンドのログインページに設定
//                    .deleteCookies("JSESSIONID") // ログアウト時にJSESSIONIDクッキーを削除
//                    .invalidateHttpSession(true) // ログアウト時にセッションを無効化
//                    .clearAuthentication(true) // ログアウト時に認証情報をクリア
//                    .permitAll()
            }
        http.csrf {
            it.csrfTokenRepository(HttpSessionCsrfTokenRepository())
        }
        return http.build()
    }
}
