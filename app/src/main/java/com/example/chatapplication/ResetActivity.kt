package com.example.chatapplication

import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class ResetActivity : AppCompatActivity() {
    lateinit var reset_button : Button
    lateinit var editext_reset : TextInputEditText
    lateinit var reset_image_view : ImageView
    lateinit var firebaseauth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        reset_button = findViewById(R.id.reset_button)
        editext_reset = findViewById(R.id.reset_editext)
        reset_image_view = findViewById(R.id.reset_imageView)
        firebaseauth = FirebaseAuth.getInstance()
    reset_button.setOnClickListener {
        val email = editext_reset.text.toString()
        if(email!=""){
            passwordReset(email)
        }else{
            Toast.makeText(this, "Please Give The Email", Toast.LENGTH_SHORT).show()
        }
    }
    }
    private fun passwordReset(email : String){
          firebaseauth.sendPasswordResetEmail(email).addOnCompleteListener {
               if(it.isSuccessful){
                   Toast.makeText(this, "Please check Your Email", Toast.LENGTH_SHORT).show()
               }else{
                   Toast.makeText(this, "There is a problem", Toast.LENGTH_SHORT).show()
               }
          }
    }

}