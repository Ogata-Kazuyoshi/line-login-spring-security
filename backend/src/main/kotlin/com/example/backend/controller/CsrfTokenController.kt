package com.example.backend.controller

import org.springframework.security.web.csrf.CsrfToken
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/csrf")
class CsrfTokenController {
    @GetMapping
    fun csrf(token: CsrfToken): CsrfToken {
        return token
    }
//    @GetMapping
//    fun csrf(token: CsrfToken, response: HttpServletResponse): CsrfToken {
//        val csrfCookie = Cookie("XSRF-TOKEN", token.token)
//        csrfCookie.path = "/"
//        csrfCookie.isHttpOnly = true // クライアントサイドのJavaScriptからクッキーにアクセスすることができなくなる設定
//        csrfCookie.secure = true // クッキーはHTTPS経由の通信でのみ送信されるようになります
//        csrfCookie.maxAge = -1 // ブラウザセッションが終了するまで有効
//        response.addCookie(csrfCookie)
//        return token
//    }
}
