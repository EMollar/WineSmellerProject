package com.project.winesmeller.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.project.winesmeller.R
import com.project.winesmeller.login.email.JavaMailAPI
import com.project.winesmeller.others.Utilities
import com.projects.winesmeller_v10.others.Constants
import org.json.JSONObject
import kotlin.math.pow


//TODO: modificar los métodos @Deprecated
class PasswordForgottenActivity : AppCompatActivity() {

    private var confirmCode : String = ""
    private var numOfAttempts = 0
    private var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_forgotten)
        setSupportActionBar(findViewById(R.id.my_toolbar_password_forgotten))
        setTitle(R.string.activityTitle_passwordForgotten)
        window.setBackgroundDrawableResource(R.drawable.background_auth)
        setBackBoton()

        listenerButtons()

        elementsEnableSection01(false)
        elementsEnableSection02(false)
        elementsEnableSection03(true)
    }



    private fun listenerButtons() {
        val bSendCode = findViewById<Button>(R.id.bSendCode)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val bValidCode = findViewById<Button>(R.id.bValidCode)
        val bConfirmNewPassword = findViewById<Button>(R.id.bConfirmNewPassword)
        val code01 = findViewById<EditText>(R.id.squareCode01)
        val code02 = findViewById<EditText>(R.id.squareCode02)
        val code03 = findViewById<EditText>(R.id.squareCode03)
        val code04 = findViewById<EditText>(R.id.squareCode04)

        val getPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE )
        val setPrefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE ).edit()

        changeFocus(code01, code02, code03, code04)



        bConfirmNewPassword.setOnClickListener {
            val etNewPassword = findViewById<EditText>(R.id.etNewPassword).text.toString()
            val etConfirmNewPassword = findViewById<EditText>(R.id.etConfirmNewPassword).text.toString()

            if (etNewPassword != etConfirmNewPassword){
                Toast.makeText(this, R.string.toast_passwordAndConfirmDiff, Toast.LENGTH_LONG).show()
            } else {
                var resultado       : Any
                var message         : Any
                val url = "${Constants.URL_SERVER}${Constants.SC_UPDATE_PASSWORD}?email=$email&password=$etNewPassword"

                val request = JsonObjectRequest(Request.Method.GET, url, null,
                        { response ->
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
                        })
                val queue = Volley.newRequestQueue(this)
                queue.add(request)
            }


        }



        bSendCode.setOnClickListener {
            bSendCode.isEnabled = false
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
                    email = etEmail.text.toString()
                    confirmEmail(bSendCode, code01)
                    lastAttemptInMins = java.util.Calendar.getInstance().timeInMillis / 60000
                    if (diffTimeInMins >= 1440) {
                        numAttempts = 0
                    }
                    setPrefs.putLong("lastAttemptInMins", lastAttemptInMins)
                    setPrefs.putInt("numAttempts", numAttempts)
                    setPrefs.apply()
                } else {
                    Toast.makeText(this, "Inténtelo de nuevo en ${timeToWait-diffTimeInMins} minutos", Toast.LENGTH_LONG).show()
                    numAttempts -= 1
                    setPrefs.putInt("numAttempts", numAttempts)
                    setPrefs.apply()    // En un principio no haría falta volver a meter este parámetro ya que seguiría como está actualmente.
                }

            } else {
                email = etEmail.text.toString()
                confirmEmail(bSendCode, code01)

                lastAttemptInMins = java.util.Calendar.getInstance().timeInMillis / 60000
                setPrefs.putLong("lastAttemptInMins", lastAttemptInMins)
                setPrefs.putInt("numAttempts", numAttempts)
                setPrefs.apply()
            }
        }



        bValidCode.setOnClickListener {
            val textCode01 = code01.text.toString()
            val textCode02 = code02.text.toString()
            val textCode03 = code03.text.toString()
            val textCode04 = code04.text.toString()

            val codeEntered = "$textCode01$textCode02$textCode03$textCode04"

            if( confirmCode == codeEntered ) {
                elementsEnableSection01(true)
                elementsEnableSection02(false)
                elementsEnableSection03(false)
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
                    elementsEnableSection01(false)
                    elementsEnableSection02(false)
                    elementsEnableSection03(true)
                }
            }
        }
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



    private fun confirmEmail(bSendCode: Button, code01: EditText) {
        var resultado       : Any
        var message         : Any
        val url = "${Constants.URL_SERVER}${Constants.SC_CHECK_EMAIL}?email=$email"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val jsonRQ = JSONObject(response.toString())
                    resultado = jsonRQ.get("success")
                    message = jsonRQ.get("message")
                    Log.d("DEBUG", "Result: $resultado / Message: $message")
                    if (jsonRQ.length() > 2) {
                        Toast.makeText(this, R.string.toast_codeSend, Toast.LENGTH_LONG).show()
                        confirmCode = Utilities.getCode4Digits()
                        sendEmail(this)
                        elementsEnableSection02(true)
                        bSendCode.text = this.resources.getString(R.string.tButton_sendAgain)
                        code01.requestFocus()
                    } else {
                        Toast.makeText(this, R.string.toast_emailNoExists, Toast.LENGTH_LONG).show()
                    }
                },
                { error ->
                    Log.e("ERROR", error.toString())
                })
        val queue = Volley.newRequestQueue(this)
        queue.add(request)
        bSendCode.isEnabled = true
    }



    private fun sendEmail(context: Context) {
        val mEmail : String = email
        val mSubject : String = context.resources.getString(R.string.email_titlePasswordForgotten)
        val mMessage : String =
                context.resources.getString(R.string.email_bodyPasswordForgottenMail01) +
                        "Código: ${confirmCode}\n\n" +
                        context.resources.getString(R.string.email_bodyGeneralMail01) +
                        context.resources.getString(R.string.email_bodyGeneralMail02)

        val javaMailAPI =
                JavaMailAPI(
                        this,
                        mEmail,
                        mSubject,
                        mMessage
                )
        javaMailAPI.execute()
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



    private fun elementsEnableSection01(value : Boolean) {
        val tvNewPassword = findViewById<TextView>(R.id.tvNewPassword)
        val etNewPassword = findViewById<EditText>(R.id.etNewPassword)
        val etConfirmNewPassword = findViewById<EditText>(R.id.etConfirmNewPassword)
        val bConfirmNewPassword = findViewById<Button>(R.id.bConfirmNewPassword)

        tvNewPassword?.isVisible = value
        etNewPassword?.isVisible = value
        etConfirmNewPassword?.isVisible = value
        bConfirmNewPassword?.isVisible = value
    }



    private fun elementsEnableSection02(value : Boolean) {
        val bar01 = findViewById<View>(R.id.first_view)
        val bar02 = findViewById<View>(R.id.first_separator)
        val tvInfoText = findViewById<TextView>(R.id.tvInfoText)
        val codeLayout = findViewById<LinearLayout>(R.id.linearLayoutCodePasswordForgottenActivity)
        val bValidCode = findViewById<Button>(R.id.bValidCode)

        bar01?.isVisible = value
        bar02?.isVisible = value
        tvInfoText?.isVisible = value
        codeLayout?.isVisible = value
        bValidCode?.isVisible = value
    }



    private fun elementsEnableSection03(value : Boolean) {
        val tvEnterEmail = findViewById<TextView>(R.id.tvEnterEmail)
        val etEnterEmail = findViewById<EditText>(R.id.etEmail)
        val bSendCode = findViewById<Button>(R.id.bSendCode)

        tvEnterEmail?.isVisible = value
        etEnterEmail?.isVisible = value
        bSendCode?.isVisible = value
    }
}