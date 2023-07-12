package com.example.chatapplication

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {
    lateinit var login_image : ImageView
    lateinit var txtinput_email:TextInputEditText
    lateinit var txtinput_password:TextInputEditText
    lateinit var sign_up_button : Button
    lateinit var sign_in_button:Button
    lateinit var forget_text:TextView

     lateinit var auth : FirebaseAuth
     lateinit var firebaseUser : FirebaseUser
    override fun onStart() {
        super.onStart()
        if(FirebaseAuth.getInstance().currentUser!=null){
            intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        login_image = findViewById(R.id.login_image)
        txtinput_email = findViewById(R.id.email_id_input)
        txtinput_password = findViewById(R.id.password_id_input)
        sign_up_button = findViewById(R.id.button_Sign_Up)
        sign_in_button = findViewById(R.id.button_Sign_In)
        auth = FirebaseAuth.getInstance()
        forget_text = findViewById(R.id.foget_textview)

          // sign_in_button

        sign_in_button.setOnClickListener {
            val email = txtinput_email.text.toString();
            val password = txtinput_password.text.toString()
            if (email != "" && password != "") {
                signin(email, password)
            } else {
                Toast.makeText(this, "Please Enter Email & password...", Toast.LENGTH_SHORT).show()

            }
        }
            // sign up button

            sign_up_button.setOnClickListener {
                intent = Intent(this, RegistrationActivity::class.java)
                startActivity(intent)
                finish()

            }

            // forget password

            forget_text.setOnClickListener {
                intent = Intent(applicationContext, ResetActivity::class.java)
                startActivity(intent)
            }


    }

    private fun signin(email:String, password : String){

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { signIn ->
                    if (signIn.isSuccessful) {
                        startActivity(Intent(this, MainActivity::class.java))
                        Toast.makeText(this,"signed in successfully",Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this,"signed in Failed3",Toast.LENGTH_SHORT).show()
                    }
        }
    }



}