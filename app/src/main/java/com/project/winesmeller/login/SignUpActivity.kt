package com.project.winesmeller.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.project.winesmeller.R
import com.project.winesmeller.login.email.JavaMailAPI
import com.projects.winesmeller_v10.others.Constants
import org.json.JSONObject
import kotlin.math.pow
import kotlin.random.Random

//TODO: modificar los métodos @Deprecated
//TODO: evitar que se pueda spamear con envío de correos
class SignUpActivity : AppCompatActivity() {

    private var confirmCode : String = ""
    private var email = ""
    private var password = ""
    private var confirmPassword = ""
    private var numOfAttempts = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setSupportActionBar(findViewById(R.id.my_toolbar_signup))
        setTitle(R.string.activityTitle_signUp)
        window.setBackgroundDrawableResource(R.drawable.background_auth)
        setBackBoton()

        elementsEnable(false)
        listenerButtons()
    }



    /**
     *  Muestra el botón atrás en la actionBar y selección de su color
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    fun setBackBoton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val upArrow : Drawable = resources.getDrawable(R.drawable.abc_ic_ab_back_material)
        upArrow.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC_ATOP)
        supportActionBar?.setHomeAsUpIndicator(upArrow)
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



    private fun listenerButtons() {
        val bConfirmRegister = findViewById<Button>(R.id.idButton_sendRegisterData)
        val bValidCode = findViewById<Button>(R.id.idButton_validCode)
        val code01 = findViewById<EditText>(R.id.squareCode01)
        val code02 = findViewById<EditText>(R.id.squareCode02)
        val code03 = findViewById<EditText>(R.id.squareCode03)
        val code04 = findViewById<EditText>(R.id.squareCode04)

        val getPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
        val setPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE ).edit()

        changeFocus(code01, code02, code03, code04)



        bConfirmRegister.setOnClickListener {
            var lastAttemptInMins = getPrefs.getLong("lastAttemptInMins", 0L)
            val diffTimeInMins: Int
            var numAttempts = getPrefs.getInt("numAttempts", 0)
            numAttempts += 1

            if (numAttempts > 2) {
                val currentTimeInMins = java.util.Calendar.getInstance().timeInMillis / 60000
                diffTimeInMins = (currentTimeInMins - lastAttemptInMins).toInt()

                var timeToWait = (2.toFloat().pow(numAttempts).toInt() - 3)
                if (timeToWait > 1440) {
                    timeToWait = 1440 // Horas en un día
                }

                if (diffTimeInMins >= timeToWait){
                    buttonConfirmRegisterPressed(bConfirmRegister, code01)
                    lastAttemptInMins = java.util.Calendar.getInstance().timeInMillis / 60000
                    if (diffTimeInMins >= 1440) {
                        numAttempts = 0
                    }
                    setPrefs.putLong("lastAttemptInMins", lastAttemptInMins)
                    setPrefs.putInt("numAttempts", numAttempts)
                    setPrefs.apply()
                } else {
                    //TODO probarlo
                    Toast.makeText(this, "Inténtelo de nuevo en ${timeToWait-diffTimeInMins} minutos", Toast.LENGTH_LONG).show()
                    numAttempts -= 1
                    setPrefs.putInt("numAttempts", numAttempts)
                    setPrefs.apply()    // En un principio no haría falta volver a meter este parámetro ya que seguiría como está actualmente.
                }

            } else {
                buttonConfirmRegisterPressed(bConfirmRegister, code01)

                lastAttemptInMins = java.util.Calendar.getInstance().timeInMillis / 60000
                setPrefs.putLong("lastAttemptInMins", lastAttemptInMins)
                setPrefs.putInt("numAttempts", numAttempts)
                setPrefs.apply()
            }
        }



        bValidCode.setOnClickListener {
            if ( email == "" || password == "" || confirmPassword == "") {      // Este caso NO debería de ocurrir nunca
                Toast.makeText(this, R.string.toast_voidSomeData, Toast.LENGTH_LONG).show()
                elementsEnable(false)
            } else {
                val textCode01 = code01.text.toString()
                val textCode02 = code02.text.toString()
                val textCode03 = code03.text.toString()
                val textCode04 = code04.text.toString()

                val codeEntered = "$textCode01$textCode02$textCode03$textCode04"

                if( confirmCode == codeEntered ) {
                    userRegister()
                    setPrefs.putInt("numAttempts", 0)
                    setPrefs.apply()
                } else {
                    Toast.makeText(this, R.string.toast_wrongCodeEntered, Toast.LENGTH_LONG).show()
                    code01.setText("")
                    code02.setText("")
                    code03.setText("")
                    code04.setText("")
                    code01.requestFocus()
                    numOfAttempts++
                    if (numOfAttempts == 3) {
                        Toast.makeText(this, R.string.toast_maxNumOfAttempts, Toast.LENGTH_LONG).show()
                        elementsEnable(false)
                    }
                }
            }
        }
    }



    private fun buttonConfirmRegisterPressed(bConfirmRegister: Button, code01: EditText) {
        bConfirmRegister.isEnabled = false
        email = findViewById<EditText>(R.id.idEditText_Email_Register).text.toString()
        password = findViewById<EditText>(R.id.idEditText_Password).text.toString()
        confirmPassword = findViewById<EditText>(R.id.idEditText_ConfirmPassword).text.toString()

        checkEmailAndSendCode(
                "${Constants.URL_SERVER}${Constants.SC_CHECK_EMAIL}?email=$email",
                bConfirmRegister, code01
        )
    }



    /**
     * Cambiamos el focus de un recuadro a otro según se vayan introduciendo los caracteres
     * También cambia el focus en el caso de borrar el caracter
     */
    private fun changeFocus(code01: EditText, code02: EditText, code03: EditText, code04: EditText) {

        code01.addTextChangedListener {
            if (code01.text.toString() != "") {
                code02.requestFocus()
            }
        }
        code02.addTextChangedListener {
            if (code02.text.toString() == "") {
                code01.requestFocus()
            } else {
                code03.requestFocus()
            }
        }
        code03.addTextChangedListener {
            if (code03.text.toString() == "") {
                code02.requestFocus()
            } else {
                code04.requestFocus()
            }
        }
        code04.addTextChangedListener {
            if (code04.text.toString() == "") {
                code03.requestFocus()
            }
        }
    }



    private fun checkEmailAndSendCode(
            URL: String,
            bConfirmRegister: Button,
            code01: EditText
    ) {
        var resultado       : Any
        var message         : Any
        val btConfirm = findViewById<Button>(R.id.idButton_sendRegisterData)

        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, R.string.toast_invalidEmail, Toast.LENGTH_LONG).show()
            bConfirmRegister.isEnabled = true
        } else if (password != confirmPassword || password == "" ) {
            Toast.makeText(this, R.string.toast_passwordAndConfirmDiff, Toast.LENGTH_LONG).show()
            bConfirmRegister.isEnabled = true
        } else {
            val request = JsonObjectRequest(Request.Method.GET, URL, null,
                { response ->
                    val jsonRQ = JSONObject(response.toString())
                    resultado = jsonRQ.get("success")
                    message = jsonRQ.get("message")
                    Log.d("DEBUG", "Result: $resultado / Message: $message")
                    if (jsonRQ.length() > 2) {
                        Toast.makeText(this, R.string.toast_emailAlreadyExists, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, R.string.toast_codeSend, Toast.LENGTH_LONG).show()
                        confirmCode = getCode()
                        sendEmail(this)
                        elementsEnable(true)
                        btConfirm.text = this.resources.getString(R.string.tButton_sendAgain)
                        code01.requestFocus()
                    }
                    bConfirmRegister.isEnabled = true
                },
                { error ->
                    Log.e("ERROR", error.toString())
                    bConfirmRegister.isEnabled = true
                })
            val queue = Volley.newRequestQueue(this)
            queue.add(request)
        }
    }



    private fun sendEmail(context : Context) {
        val mEmail : String = email
        val mSubject : String = context.resources.getString(R.string.email_titleRegistrationMail)
        val mMessage : String =
                context.resources.getString(R.string.email_bodyRegistrationMail01) +
                "Código: ${confirmCode}\n\n" +
                context.resources.getString(R.string.email_bodyRegistrationMail02) +
                context.resources.getString(R.string.email_bodyRegistrationMail03)

        val javaMailAPI =
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



    private fun userRegister() {
        var resultado       : Any
        var message         : Any

        val url = "${Constants.URL_SERVER}${Constants.SC_USER_REGISTER}?email=$email&password=$password"
        println("$url************************************************")

        val request = JsonObjectRequest( Request.Method.POST, url, null,
                { response ->
                    Log.d("RESPONSE", response.toString())
                    Toast.makeText(this, R.string.toast_registerCompleted, Toast.LENGTH_LONG).show()
                    val jsonRQ = JSONObject(response.toString())
                    resultado = jsonRQ.get("success")
                    message = jsonRQ.get("message")
                    Log.d("DEBUG", "Result: $resultado / Message: $message")
                    val homeIntent = Intent(this, LoginActivity::class.java).apply {
                        putExtra("email", email)
                    }
                    startActivity(homeIntent)
                    finish()
                },
                { error ->
                    Log.e("ERROR", error.toString())
                    Toast.makeText(this, R.string.toast_errorFailedRegistration, Toast.LENGTH_LONG).show()
                })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }
}
