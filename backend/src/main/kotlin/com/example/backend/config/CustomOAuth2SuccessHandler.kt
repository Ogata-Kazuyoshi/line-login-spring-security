package com.example.backend.config

//import com.example.backend.model.*
//import com.example.backend.service.UsersService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

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
        //認証成功時に自動でonAuthenticationSuccessメソッドの中身が実行される
        //認証成功＝application.ymlに記載しているuser-info-uriまで問い合わせ終了でUser情報はすでに持っている
        // authorize -> アクセスtoken発行 -> profileでUser情報を取得
        println("userInfo : " + authentication)
        if (authentication is OAuth2AuthenticationToken) {
            val principal = authentication.principal as OAuth2User
            val oAuth2AuthenticationToken = authentication as OAuth2AuthenticationToken
            val userId = principal.attributes["userId"] as String?
            val displayName = principal.attributes["displayName"] as String?
            println("userId: $userId")
            println("displayname: $displayName")
            if (userId != null) {
                val usertableId = "hogehoge" //テーブルに現在のuserIdが存在するかを問い合わせて、なければ登録する。その時のUUIDを返却してもらう
                val newAuthentication =
                    OAuth2AuthenticationToken(
                        CustomOAuth2UserImpl(
//                            principal.authorities,
                            userId = usertableId,
                            name =  displayName,
                            sub = userId,
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
}


