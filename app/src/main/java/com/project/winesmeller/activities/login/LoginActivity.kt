package com.project.winesmeller.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.project.winesmeller.R
import com.project.winesmeller.activities.MainMenuActivity


class LoginActivity : AppCompatActivity() {

    lateinit var title : ImageView
    lateinit var letters : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(findViewById(R.id.my_toolbar_auth))
        setTitle(R.string.activityTitle_auth)
        window.setBackgroundDrawableResource(R.drawable.background_auth)



        /**
         * Animación de logo y título en pantalla de login
         */
        val animationTitle = AnimationUtils.loadAnimation(this, R.anim.alpha)
        title = findViewById(R.id.title_text)
        title.startAnimation(animationTitle)
        letters = findViewById(R.id.letters_text)
        letters.startAnimation(animationTitle)

        // Elimina la barra de notificaciones
//        Utilities.noShowNotificationBar(this.window)

        listenerButtons()
    }



    /**
     * Escuchamos todos los botones de la activity desde esta función
     */
    private fun listenerButtons() {
        val idButtonLogin = findViewById<Button>(R.id.idButton_Login)
        val idButtonPasswordForgottenActivity = findViewById<TextView>(R.id.tvForgottenPasswordCuestion)
        val idButtonSignUp = findViewById<TextView>(R.id.tvSignUp)

        idButtonLogin.setOnClickListener {
            val homeIntent = Intent(this, MainMenuActivity::class.java).apply {
            }
            startActivity(homeIntent)
        }

        idButtonPasswordForgottenActivity.setOnClickListener {
            val homeIntent = Intent(this, PasswordForgottenActivity::class.java).apply {
            }
            startActivity(homeIntent)
        }

        idButtonSignUp.setOnClickListener {
            val homeIntent = Intent(this, SignUpActivity::class.java).apply {
            }
            startActivity(homeIntent)
        }
    }
}