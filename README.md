# ソーシャルログイン@SpringSecurity

<details open="open">
<summary>目次</summary>



- [環境セットアップ](#環境セットアップ)
- [ログインの流れ](#ログインの流れ)
- [コードの説明](#コードの説明)
  - [applicaton.yml](#applicaton.yml)
  - [SecurityConfiguration.kt](#SecurityConfiguration.kt)

- [ChatGPT](#ChatGPT)

- [参考](#参考)

</details>

# 環境セットアップ

<details>
<summary> 1. 必要ライブラリーのインポート</summary>

```gradlew.kts
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
	implementation("org.springframework.boot:spring-boot-starter-security")
```
</details>

<details>
<summary> 2. 各プロバイダでセットアップしたAppにリダイレクトURIを設定</summary>

- 最小限必要なUriはSpringSecurityでセットする「redirectUri: "{baseUrl}/login/oauth2/code/{registrationId}"」が必要。
- LINEの場合は、http://localhost:8080/login/oauth2/code/line"
</details>

# ログインの流れ

<details>
<summary>  OAuth2/ OIDCの認証では大きな流れとして下記が実施される（LINEの場合）</summary>

- ![ログインの流れ](./assets/images/line-login-flow.png)

1. フロントエンド→バックエンドのログインエンドポイントへHttpリクエスト。/oauth2/authorization/{registrationId}がSpringSecurityがログイン認証を始める最初のエンドポイント
2. SpringSecurity→各プロバイダの認証エンドポイントを叩く。上記の{registrationId}にともなって、各resistrationに登録されている名前と一致するproviderのauthorizationUriを叩く。LINEの場合は、https://access.line.me/oauth2/v2.1/authorizeに必要なQueryをSpringSecurityが勝手につけてリクエスト送付
3. ユーザーが認証処理をすると、各プロバイダ→Spring Securityに必要なクエリをつけた状態（codeなど）で、登録されているredirectUriにリダイレクトされる
4. Spring Security→各プロバイダに必要な情報（codeなど）を付与してアクセストークン発行の依頼（postメソッド）。tokenUri: https://api.line.me/oauth2/v2.1/token
5. 各プロバイダ→SpringSecurityにアクセストークンなどを含んだ情報をredirect_uriへresponseする。
6. SpringSecurity→各プロバイダへアクセストークンを使用して、ユーザーの認証情報を取得依頼。userInfoUri: https://api.line.me/v2/profile
7. 各プロバイダ→SpringSecurityへ上記の情報をredirect_uriへresponseする。
8. 上記まで問題なければ、各SuccessHandlerが実行される（SuccessHandlerに入るタイミングでは２〜７が全て終わっていて、ユーザー情報はすでに持っている!!）
9. SuccessHandlerの最後にフロントエンドへredirectする。この時、SpringSecurityが「JSESSIONID」を作成してブラウザが保持する。これによってセッション管理されてUserの認証プロセスを完了する


```application.yml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            clientId: ${GITHUB_CLIENT_ID}
            clientSecret: ${GITHUB_SECRET_ID}
            scope: read:user
          line:
            clientId: ${LINE_CLIENT_ID}
            clientSecret: ${LINE_SECRET_ID}
            authorizationGrantType: authorization_code
            redirectUri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: profile
        provider:
          line:
            authorizationUri: https://access.line.me/oauth2/v2.1/authorize
            tokenUri: https://api.line.me/oauth2/v2.1/token
            userInfoUri: https://api.line.me/v2/profile
            userNameAttribute: userId
#githubは共通のOAuthプロバイダとして、SpringBootが認識するので、登録不要
#          github:
#            authorization-uri: https://github.com/login/oauth/authorize
#            token-uri: https://github.com/login/oauth/access_token
#            user-info-uri: https://api.github.com/user


#Frontendから叩く場合は、/oauth2/authorization/{registrationId}がバックエンドの認証フローをスタートさせるトリガー
```
</details>


# コードの説明


<details>
<summary> 1. application.yml </summary>

- resistrationIdで各プロバイダを区別する
- 複数のプロバイダを使用する場合は、１つにredirectUriを定義すれば良い。最後に{resistrationId}とすることで、それぞれのプロバイダ用のリダイレクトUriが内部的に定義される
- scopeは各プロバイダに寄るので調べること
- フロントエンドから叩く際は、/oauth2/authorization/{registrationId}を叩く。各プロバイダでパスによって切り替えられる
- 共通OAuthプロバイダの場合（Github、Google、Facebookなど）は、SpringSecurityに既にプロバイダ情報はあるため、authorizationUriとかは特に定義しなくても問題ない。


```application.yml
spring:
  security:
    oauth2:
      client:
        registration:
          github:
            clientId: ${GITHUB_CLIENT_ID}
            clientSecret: ${GITHUB_SECRET_ID}
            scope: read:user
          line:
            clientId: ${LINE_CLIENT_ID}
            clientSecret: ${LINE_SECRET_ID}
            authorizationGrantType: authorization_code
            redirectUri: "{baseUrl}/login/oauth2/code/{registrationId}"
            scope: profile
        provider:
          line:
            authorizationUri: https://access.line.me/oauth2/v2.1/authorize
            tokenUri: https://api.line.me/oauth2/v2.1/token
            userInfoUri: https://api.line.me/v2/profile
            userNameAttribute: userId
#githubは共通のOAuthプロバイダとして、SpringBootが認識するので、登録不要
#          github:
#            authorization-uri: https://github.com/login/oauth/authorize
#            token-uri: https://github.com/login/oauth/access_token
#            user-info-uri: https://api.github.com/user


#Frontendから叩く場合は、/oauth2/authorization/{registrationId}がバックエンドの認証フローをスタートさせるトリガー
```

</details>

<details>
<summary> 2. SecurityConfiguration.kt </summary>

- 複数のプロバイダを切り替えれるように、各プロバイダ用のSuccessHandlerをBean登録しておく
- .csrfでポスト処理などの403エラーを回避できる（ただ、ざるになるので本当はだめ）
- requestMatchers().authentificated()で登録されているエンドポイントは認証されているかのチェックが入る。@AuthenticationPrincipalを使ってUser情報を取りたい場合は、このエンドポイント内にないとだめ

```kotlin
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

```

</details>

# ChatGPT

<details>
<summary> 1. 起動時に読み込まれるアノテーションに関して</summary>

- ![](./assets/images/anotation.png)
</details>

<details>
<summary> 2. application.ymlの設定に関して</summary>

- ![](./assets/images/applicationyml1.png)
- ![](./assets/images/applicationyml2.png)
</details>

<details>
<summary> 3. 認証プロセスに関して</summary>

- ![](./assets/images/auth_process1.png)
- ![](./assets/images/auth_process2.png)
</details>


# 参考
- [github](https://tech.asoview.co.jp/entry/2021/12/09/000000)
