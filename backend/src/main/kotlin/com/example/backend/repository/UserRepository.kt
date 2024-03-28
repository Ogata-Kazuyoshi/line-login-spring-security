package com.example.backend.repository

import com.example.backend.model.table.UserTable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<UserTable, UUID> {
    fun findByOid(oid: String): UserTable?
}
