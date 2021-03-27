package com.project.winesmeller

import org.junit.Assert
import org.junit.Test
import java.net.PasswordAuthentication
import kotlin.math.pow
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

    @Test
    fun getDate() {
//        val stLastAttemptInMillis = java.util.Calendar.getInstance().timeInMillis.toString()
//        val lastAttemptInMillis = stLastAttemptInMillis.toLong()
        val lastAttemptInMillis = java.util.Calendar.getInstance().timeInMillis
        val lastAttemptInMins = lastAttemptInMillis / 60000
        val lastAttemptInHours = lastAttemptInMins / 60
        val lastAttemptInDays = lastAttemptInHours / 24
        val lastAttemptInYears = lastAttemptInDays / 365

//        println("String: Cantidad de milisegundos $stLastAttemptInMillis")
        println("Cantidad de milisegundos $lastAttemptInMillis")
        println("Cantidad de minutos $lastAttemptInMins")
        println("Cantidad de horas $lastAttemptInHours")
        println("Cantidad de dias $lastAttemptInDays")
        println("Cantidad de años $lastAttemptInYears")
    }

    @Test
    fun diffBtwnLongAndInt(){
        var diffTimeInMins = 0
        val lastAttemptInMins = 26943617L
        val currentTimeInMins = java.util.Calendar.getInstance().timeInMillis / 60000
        diffTimeInMins = (currentTimeInMins - lastAttemptInMins).toInt()
        println("Diff MINUTOS: $diffTimeInMins")
        println("lastAttemptInMins: $lastAttemptInMins")
        println("currentTimeInMins: $currentTimeInMins")
    }

    @Test
    fun exponentOperation(){
        val num = 2.toFloat().pow(3).toInt()
        println("El número es $num")
    }
}
