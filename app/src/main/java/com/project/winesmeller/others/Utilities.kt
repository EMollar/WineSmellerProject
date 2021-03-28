package com.project.winesmeller.others

import android.content.Context
import android.content.Intent
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import com.google.android.material.navigation.NavigationView
import com.project.winesmeller.R
import kotlin.random.Random

class Utilities {

    companion object {

        //LOGS
        val LOG_INFO    = "LOG_INFO"
        val LOG_ERROR   = "LOG_ERROR"



        /*************************************************************************************
         * Instrucción para mostrar la pantalla completa. No se mostrará la barra de notificaciones
         *************************************************************************************/
        fun noShowNotificationBar(window: Window?) {
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }



        /*************************************************************************************
         * Instrucción para mostrar el botón de navegación al menú lateral de la app
         *************************************************************************************/
        fun showHomeButton(b: Boolean, supportActionBar: ActionBar?, icMenu: Int) {
            val actionBar : androidx.appcompat.app.ActionBar? = supportActionBar
            actionBar?.setDisplayHomeAsUpEnabled(b)
            actionBar?.setHomeAsUpIndicator(icMenu)
        }



        /*************************************************************************************
         * Instrucción para generar un código de 4 dígitos.
         * Lo devuelve en formato String
         *************************************************************************************/
        fun getCode4Digits() : String {
            val code01 : String = Random.nextInt(0,9).toString()
            val code02 : String = Random.nextInt(0,9).toString()
            val code03 : String = Random.nextInt(0,9).toString()
            val code04 : String = Random.nextInt(0,9).toString()

            return "$code01$code02$code03$code04"
        }



        /*************************************************************************************
         * Listener del menú lateral
         *************************************************************************************/
        fun setNavigationItemSelectedListener(nav_view: NavigationView, context : Context) {

            nav_view.setNavigationItemSelectedListener {

//                when(it.itemId) {
//
//                    R.id.nav_addWine -> {
//                        val loginIntent = Intent(context, AddWineActivity::class.java)
//                        context.startActivity(loginIntent)
//                    }
//
//                    R.id.nav_board -> {
//                        val loginIntent = Intent(context, BoardActivity::class.java)
//                        context.startActivity(loginIntent)
//                    }
//
//                    R.id.nav_myWinery -> {
//                        val loginIntent = Intent(context, MyWineryActivity::class.java)
//                        context.startActivity(loginIntent)
//                    }
//                }

                return@setNavigationItemSelectedListener true
            }
        }

    }

}
