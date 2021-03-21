package com.project.winesmeller.activities.login

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.winesmeller.R

class PasswordForgottenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_forgotten)
        setSupportActionBar(findViewById(R.id.my_toolbar_password_forgotten))
        setTitle(R.string.activityTitle_passwordForgotten)
        window.setBackgroundDrawableResource(R.drawable.background_auth)
        setBackBoton()
    }



    /**
     *  Muestra el botón atrás en la actionBar y selección de su color
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setBackBoton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val upArrow : Drawable = resources.getDrawable(R.drawable.abc_ic_ab_back_material)
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
        getSupportActionBar()?.setHomeAsUpIndicator(upArrow)
    }
}