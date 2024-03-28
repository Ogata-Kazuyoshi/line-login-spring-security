package com.example.backend.service

import com.example.backend.model.response.ResponceUserInfo
import com.example.backend.model.table.UserTable
import com.example.backend.repository.UserRepository
import org.springframework.stereotype.Service

interface UserService {
    fun getOrCreateUserService (oid:String,name:String):ResponceUserInfo
    fun getUserInfo (oid:String?):ResponceUserInfo?
    fun updateUserInfo (oid:String?,remark:String)
}

@Service
class UserServiceImpl (
    val userRepository: UserRepository
): UserService {
    override fun getOrCreateUserService(oid: String, name: String): ResponceUserInfo {
        var res = userRepository.findByOid(oid)
        if (res == null) {
            res = userRepository.save(
                UserTable(oid=oid,name=name)
            )
        }
        return ResponceUserInfo(
            id = res.id,
            oid = res.oid,
            name = res.name,
            remark = res.remark
        )
    }

    override fun getUserInfo(oid: String?): ResponceUserInfo? {
        if (oid != null) {
            var res = userRepository.findByOid(oid)
            if (res != null) {
                return ResponceUserInfo(
                    id = res.id,
                    oid = res.oid,
                    name = res.name,
                    remark = res.remark
                )
            }
        }
        return null
    }

    override fun updateUserInfo(oid:String?,remark:String) {
        if (oid == null) return
        val res = userRepository.findByOid(oid)
        if (res != null) {
            userRepository.save(
                UserTable(
                    id = res.id,
                    oid = res.oid,
                    name = res.name,
                    remark = remark
                )
            )
        }
    }

}