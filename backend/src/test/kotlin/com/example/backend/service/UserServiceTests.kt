package com.example.backend.service

import com.example.backend.model.response.ResponceUserInfo
import com.example.backend.model.table.UserTable
import com.example.backend.repository.UserRepository
import io.mockk.called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.apache.catalina.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

//@SpringBootTest
class UserServiceTests {
    private lateinit var mockRepository: UserRepository

    private lateinit var service:UserService

    @BeforeEach
    fun setup () {
        mockRepository = mockk()
        service = UserServiceImpl(mockRepository)
    }

    @Nested
    inner class `getOrCreateUserServiceのテスト` () {
        @Test
        fun `getOrCreateUserServiceを呼ぶとuserRepositoryのfindByOidを正しい引数で呼ぶ` () {
            every { mockRepository.findByOid(any()) } returns null
            every { mockRepository.save(any()) } returns UserTable()

            service.getOrCreateUserService(oid = "hogehoge", name = "fugafuga")

            verify { mockRepository.findByOid("hogehoge") }

        }

        @Test
        fun `findByOidの帰り値がnullでないとき、受け取った値を返す` () {
            every { mockRepository.findByOid(any()) } returns UserTable(
                id = UUID.fromString("0000000-0000-0000-0000-000000000001"),
                oid = "testtest",
                name = "hogehoge",
                remark = "fugafuga"
            )
            every { mockRepository.save(any()) } returns UserTable()

            val res = service.getOrCreateUserService(oid = "1234", name = "fugafuga")

            assertEquals(ResponceUserInfo(
                id = UUID.fromString("0000000-0000-0000-0000-000000000001"),
                oid = "testtest",
                name = "hogehoge",
                remark = "fugafuga"
            ),res)
        }

        @Test
        fun `findByOidの帰り値がnullのとき正しい引数でsaveを呼ぶ` () {
            every { mockRepository.findByOid(any()) } returns null
            every { mockRepository.save(any()) } returns UserTable()

            service.getOrCreateUserService(oid = "1234", name = "fugafuga")

            verify { mockRepository.save(match {
                        it.oid == "1234" &&
                        it.name == "fugafuga"
            }) }
        }

        @Test
        fun `findByOidの帰り値がnullのとき新たにsaveされた値を返す` () {
            every { mockRepository.findByOid(any()) } returns null
            every { mockRepository.save(any()) } returns UserTable(
                id = UUID.fromString("0000000-0000-0000-0000-000000000001"),
                oid = "testtest",
                name = "hogehoge",
                remark = "fugafuga"
            )

            val res = service.getOrCreateUserService(oid = "1234", name = "fugafuga")

            assertEquals(ResponceUserInfo(
                id = UUID.fromString("0000000-0000-0000-0000-000000000001"),
                oid = "testtest",
                name = "hogehoge",
                remark = "fugafuga"
            ),res)
        }
    }

    @Nested
    inner class `getUserInfoのテスト` () {
        @Test
        fun `引数がnullの時、findByOidを呼ばずにnullを返す` () {
            every { mockRepository.findByOid(any()) } returns null

            val res = service.getUserInfo(null)

            verify { mockRepository.findByOid(any())?.wasNot(called) }
            assertEquals(null,res)
        }

        @Test
        fun `引数がnullでない時、userRepositoryのfindByOidを正しい引数で呼ぶ` () {
            every { mockRepository.findByOid(any()) } returns null

            service.getUserInfo("hogehoge")

            verify { mockRepository.findByOid("hogehoge") }
        }

        @Test
        fun `引数がnullでない時、正しい値を返す` () {
            every { mockRepository.findByOid(any()) } returns UserTable(
                id = UUID.fromString("0000000-0000-0000-0000-000000000001"),
                oid = "testtest",
                name = "hogehoge",
                remark = "fugafuga"
            )

            val res = service.getUserInfo("hogehoge")

            assertEquals(ResponceUserInfo(
                id = UUID.fromString("0000000-0000-0000-0000-000000000001"),
                oid = "testtest",
                name = "hogehoge",
                remark = "fugafuga"
            ),res)
        }
    }

    @Nested
    inner class `updateUserInfoのテスト` () {
        @Test
        fun `引数のoidがullの時、findByOidを呼ばずにnullを返す` () {
            every { mockRepository.findByOid(any()) } returns null

            val res = service.updateUserInfo(null, "")

            verify { mockRepository.findByOid(any())?.wasNot(called) }
            assertEquals(Unit,res)
        }

        @Test
        fun `引数がoidがnullでない時、userRepositoryのfindByOidを正しい引数で呼ぶ` () {
            every { mockRepository.findByOid(any()) } returns null

            service.updateUserInfo("hogehoge","testtest")

            verify { mockRepository.findByOid("hogehoge") }
        }

        @Test
        fun `引数のoidがnullでない時、正しい値を返す` () {
            every { mockRepository.findByOid(any()) } returns UserTable(
                id = UUID.fromString("0000000-0000-0000-0000-000000000001"),
                oid = "testtest",
                name = "hogehoge",
                remark = "fugafuga"
            )

            every { mockRepository.save(any()) } returns UserTable()

            service.updateUserInfo("hogehoge","testtest2")

            verify { mockRepository.save(UserTable(
                id = UUID.fromString("0000000-0000-0000-0000-000000000001"),
                oid = "testtest",
                name = "hogehoge",
                remark = "testtest2"
            )) }
        }
    }

}