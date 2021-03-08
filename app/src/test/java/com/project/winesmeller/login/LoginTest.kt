package com.project.winesmeller.login

import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test
import java.net.PasswordAuthentication

class LoginTest : TestCase() {

    @Test
    fun loginWithValidUserAndPasswordTest() {
        val authentication : PasswordAuthentication = PasswordAuthentication("pepe", "password" as CharArray)

        assertEquals("Nombre no es el correcto","pepe", authentication.userName)
    }

}