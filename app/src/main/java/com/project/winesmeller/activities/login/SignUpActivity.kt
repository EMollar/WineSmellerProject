package com.project.winesmeller.activities.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.winesmeller.R

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(findViewById(R.id.my_toolbar_signup))
        setTitle(R.string.activityTitle_signUp)
    }
}