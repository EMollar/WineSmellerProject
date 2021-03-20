package com.project.winesmeller.activities.login

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.project.winesmeller.R
import com.project.winesmeller.login.email.JavaMailAPI
import com.projects.winesmeller_v10.others.Constants
import org.json.JSONObject


class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(findViewById(R.id.my_toolbar_signup))
        setTitle(R.string.activityTitle_signUp)
        window.setBackgroundDrawableResource(R.drawable.background_auth)

        elementsDisable()
        listenerButtons()
    }


    fun elementsDisable() {
        val infoText = findViewById<TextView>(R.id.idInfoText)
        val codesLayout = findViewById<LinearLayout>(R.id.linearLayoutCodes)
        val bValidCode = findViewById<Button>(R.id.idButton_validCode)
        val bar01 = findViewById<View>(R.id.first_view)
        val bar02 = findViewById<View>(R.id.first_separator)
        val bar03 = findViewById<View>(R.id.second_view)
        val bar04 = findViewById<View>(R.id.second_separator)

        infoText?.isVisible = false
        codesLayout?.isVisible = false
        bValidCode?.isVisible = false
        bar01?.isVisible = false
        bar02?.isVisible = false
        bar03?.isVisible = false
        bar04?.isVisible = false
    }


    fun listenerButtons() {
        val bConfirmRegister = findViewById<Button>(R.id.idButton_sendRegisterData)
        val bValidCode = findViewById<Button>(R.id.idButton_validCode)

        var email = ""
        var password = ""
        var confirmPassword = ""

        bConfirmRegister.setOnClickListener {
            bConfirmRegister.isEnabled = false
            email = findViewById<EditText>(R.id.idEditText_Email_Register).text.toString()
            password = findViewById<EditText>(R.id.idEditText_Password).text.toString()
            confirmPassword = findViewById<EditText>(R.id.idEditText_ConfirmPassword).text.toString()
            //TODO: comprobar si el correo existe ya en BBDD

            println("${Constants.URL_SERVER}${Constants.SC_CHECK_EMAIL}?email=$email************************************************")
            checkRegisterBBDD(
                "${Constants.URL_SERVER}${Constants.SC_CHECK_EMAIL}?email=$email",
                email,
                password,
                confirmPassword,
                bConfirmRegister
            )
        }
    }



    private fun checkRegisterBBDD(
        URL: String,
        email: String,
        password: String,
        confirmPassword: String,
        bConfirmRegister: Button
    ) {
        var resultado       : Any   = ""
        var message         : Any   = ""
        var res : String = ""
        var varBBDD : Boolean = false

        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, R.string.toast_invalidEmail, Toast.LENGTH_LONG).show()
            bConfirmRegister.isEnabled = true
        } else if (password != confirmPassword || password == "" ) {
            Toast.makeText(this, R.string.toast_passwordAndConfirmDiff, Toast.LENGTH_LONG).show()
            bConfirmRegister.isEnabled = true
        } else {
            val request = JsonObjectRequest(Request.Method.GET, URL, null,
                { response ->
                    val jsonRQ: JSONObject = JSONObject(response.toString())
                    resultado = jsonRQ.get("success")
                    message = jsonRQ.get("message")
                    if (jsonRQ.length() > 2) {
                        Toast.makeText(this, R.string.toast_emailAlreadyExists, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "REGISTRO VALIDO", Toast.LENGTH_LONG).show()
                        sendEmail()
                    }
                    bConfirmRegister.isEnabled = true
                },
                { error ->
                    // TODO: Handle error
                    println("HUBO UN ERROR")
                    bConfirmRegister.isEnabled = true
                })
            val queue = Volley.newRequestQueue(this)
            queue.add(request)
        }
    }



    fun sendEmail() {
        val mEmail : String = "enrique.mollarc@gmail.com"
        val mSubject : String = "Este sería el titulo"
        val mMessage : String = "Este seria el contenido"

        val javaMailAPI : JavaMailAPI =
            JavaMailAPI(
                this,
                mEmail,
                mSubject,
                mMessage
            )
        javaMailAPI.execute()
    }
}
