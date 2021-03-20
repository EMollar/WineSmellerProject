package com.project.winesmeller.activities.login

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import kotlin.random.Random

//TODO: en la parte de introducir los códigos, hacer que según se vaya introduciendo el código salte automáticamente al siguiente sin tener que estar pulsando sobre cada uno de los recuadros
//TODO: evitar que se pueda spamear con envío de correos
//TODO: límite de intentos por código
class SignUpActivity : AppCompatActivity() {

    var confirmCode : String = ""
    var email = ""
    var password = ""
    var confirmPassword = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(findViewById(R.id.my_toolbar_signup))
        setTitle(R.string.activityTitle_signUp)
        window.setBackgroundDrawableResource(R.drawable.background_auth)

        elementsEnable(false)
        listenerButtons()
    }



    fun elementsEnable(value : Boolean) {
        val infoText = findViewById<TextView>(R.id.idInfoText)
        val codesLayout = findViewById<LinearLayout>(R.id.linearLayoutCodes)
        val bValidCode = findViewById<Button>(R.id.idButton_validCode)
        val bar01 = findViewById<View>(R.id.first_view)
        val bar02 = findViewById<View>(R.id.first_separator)
        val bar03 = findViewById<View>(R.id.second_view)
        val bar04 = findViewById<View>(R.id.second_separator)

        infoText?.isVisible = value
        codesLayout?.isVisible = value
        bValidCode?.isVisible = value
        bar01?.isVisible = value
        bar02?.isVisible = value
        bar03?.isVisible = value
        bar04?.isVisible = value
    }



    fun listenerButtons() {
        val bConfirmRegister = findViewById<Button>(R.id.idButton_sendRegisterData)
        val bValidCode = findViewById<Button>(R.id.idButton_validCode)

        bConfirmRegister.setOnClickListener {
            bConfirmRegister.isEnabled = false
            email = findViewById<EditText>(R.id.idEditText_Email_Register).text.toString()
            password = findViewById<EditText>(R.id.idEditText_Password).text.toString()
            confirmPassword = findViewById<EditText>(R.id.idEditText_ConfirmPassword).text.toString()

            println("${Constants.URL_SERVER}${Constants.SC_CHECK_EMAIL}?email=$email************************************************")
            checkEmailAndSendCode(
                "${Constants.URL_SERVER}${Constants.SC_CHECK_EMAIL}?email=$email",
                bConfirmRegister
            )
        }

        bValidCode.setOnClickListener {
            if ( email == "" || password == "" || confirmPassword == "") {      // Este caso NO debería de ocurrir nunca
                Toast.makeText(this, R.string.toast_voidSomeData, Toast.LENGTH_LONG).show()
                elementsEnable(false)
            } else {
                val code01 = findViewById<EditText>(R.id.squareCode01).text.toString()
                val code02 = findViewById<EditText>(R.id.squareCode02).text.toString()
                val code03 = findViewById<EditText>(R.id.squareCode03).text.toString()
                val code04 = findViewById<EditText>(R.id.squareCode04).text.toString()

                val codeEntered = "$code01$code02$code03$code04"

                if( confirmCode == codeEntered ) {
                    registerUserBBDD()
                } else {
                    Toast.makeText(this, R.string.toast_wrongCodeEntered, Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    private fun checkEmailAndSendCode(
        URL: String,
        bConfirmRegister: Button
    ) {
        var resultado       : Any   = ""
        var message         : Any   = ""

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
                        Toast.makeText(this, R.string.toast_codeSend, Toast.LENGTH_LONG).show()
                        confirmCode = getCode()
                        sendEmail(this)
                    }
                    bConfirmRegister.isEnabled = true
                    elementsEnable(true)
                },
                { error ->
                    Log.e("ERROR", error.toString())
                    bConfirmRegister.isEnabled = true
                })
            val queue = Volley.newRequestQueue(this)
            queue.add(request)
        }
    }



    fun sendEmail(context : Context) {
        val mEmail : String = email
        val mSubject : String = context.resources.getString(R.string.email_titleRegistrationMail)
        val mMessage : String =
                context.resources.getString(R.string.email_bodyRegistrationMail01) +
                "Código: ${confirmCode}\n\n" +
                context.resources.getString(R.string.email_bodyRegistrationMail02) +
                context.resources.getString(R.string.email_bodyRegistrationMail03)

        val javaMailAPI : JavaMailAPI =
            JavaMailAPI(
                this,
                mEmail,
                mSubject,
                mMessage
            )
        javaMailAPI.execute()
    }



    private fun getCode() : String {
        val code01 : String = Random.nextInt(0,9).toString()
        val code02 : String = Random.nextInt(0,9).toString()
        val code03 : String = Random.nextInt(0,9).toString()
        val code04 : String = Random.nextInt(0,9).toString()

        return "$code01$code02$code03$code04"
    }



    private fun registerUserBBDD() {
        Toast.makeText(this, "El usuario se está registrando", Toast.LENGTH_LONG).show()
        //TODO: registrar al usuario en BBDD
        //TODO: una vez que se complete el registro cerrar activity y navegar a activity de login
    }
}
