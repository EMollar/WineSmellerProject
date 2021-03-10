package com.project.winesmeller.others

import android.content.Context
import android.content.Intent
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import com.google.android.material.navigation.NavigationView
import com.project.winesmeller.R

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
