package com.project.winesmeller.login

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import java.net.PasswordAuthentication

class LoginTest {

    @Test
    fun loginWithValidUserTest() {
        val authentication = PasswordAuthentication("pepe", "1234".toCharArray())
        assertEquals("pepe", authentication.userName)
    }

    @Test
    fun loginWithValidPasswordTest() {
        val password = "1234".toCharArray()
        val authentication = PasswordAuthentication("pepe", password)

        for ((index, value ) in password.withIndex()){
            assertEquals(value, authentication.password[index])
        }
    }

}