package com.project.winesmeller.activities.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.winesmeller.R

class PasswordForgottenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_forgotten)
        setSupportActionBar(findViewById(R.id.my_toolbar_password_forgotten))
        setTitle(R.string.activityTitle_passwordForgotten)
    }
}