package com.project.winesmeller.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.project.winesmeller.R


class LoginActivity : AppCompatActivity() {

    lateinit var title : ImageView
    lateinit var letters : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val animationTitle = AnimationUtils.loadAnimation(this, R.anim.alpha)
        title = findViewById(R.id.title_text)
        title.startAnimation(animationTitle)
        letters = findViewById(R.id.letters_text)
        letters.startAnimation(animationTitle)
    }
}