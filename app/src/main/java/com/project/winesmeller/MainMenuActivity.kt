package com.project.winesmeller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import com.project.winesmeller.R
import com.project.winesmeller.others.Utilities


class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)
        setSupportActionBar(findViewById(R.id.fragment_my_toolbar))
        setTitle(R.string.activityTitle_mainMenu)

        // Agrega el botón de navegación al menu lateral
        Utilities.showHomeButton(true, supportActionBar, R.drawable.ic_menu)

        // Listener del menú lateral
        val nav_view = findViewById<NavigationView>(R.id.nav_view)
        Utilities.setNavigationItemSelectedListener(nav_view, this)
    }
}