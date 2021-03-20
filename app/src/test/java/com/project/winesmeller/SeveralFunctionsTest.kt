package com.project.winesmeller

import org.junit.Assert
import org.junit.Test
import java.net.PasswordAuthentication
import kotlin.random.Random

class SeveralFunctionsTest {

    @Test
    fun getCodeTest() {
        val code : String = getCode()
        Assert.assertEquals(4, code.length)
    }

    private fun getCode() : String {
        val code01 : String = Random.nextInt(0,9).toString()
        val code02 : String = Random.nextInt(0,9).toString()
        val code03 : String = Random.nextInt(0,9).toString()
        val code04 : String = Random.nextInt(0,9).toString()

        return "$code01$code02$code03$code04"
    }
}