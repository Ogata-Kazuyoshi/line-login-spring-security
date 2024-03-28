package com.example.backend.model.response

import java.util.UUID

data class ResponceUserInfo(
    val id:UUID? = null,
    val oid:String = "abcd",
    val name:String = "aaa",
    val remark:String = "",
)
