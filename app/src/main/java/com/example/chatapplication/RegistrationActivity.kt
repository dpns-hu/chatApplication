package com.example.chatapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.properties.Delegates

class RegistrationActivity : AppCompatActivity() {
    lateinit var circularImageview: CircleImageView
    lateinit var resgistration_email: TextInputEditText
    lateinit var registration_password: TextInputEditText
    lateinit var registration_username: TextInputEditText
    lateinit var sign_up_button: Button
    var imagecontrol by Delegates.notNull<Boolean>()
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
      lateinit var firebasestorage : FirebaseStorage
      lateinit var storagereference : StorageReference
      lateinit var imageuri : Uri
      var FirebaseuserId : String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        circularImageview = findViewById(R.id.circleImageView)
        resgistration_email = findViewById(R.id.edittxt_email_registration)
        registration_password = findViewById(R.id.editxt_password_registration)
        registration_username = findViewById(R.id.edittxt_username)
        sign_up_button = findViewById(R.id.button_profile)
   auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.getReference()
        firebasestorage=FirebaseStorage.getInstance()
        storagereference = firebasestorage.getReference()



         circularImageview.setOnClickListener {
             Toast.makeText(this,"Choose a lovely picture",Toast.LENGTH_SHORT).show()
                  imagechooser()
         }
        sign_up_button.setOnClickListener {
            val email = resgistration_email.text.toString()
            val password = registration_password.text.toString()
            val username = registration_username.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()){
                signup(email,password,username)

            }else {
                Toast.makeText(this,"Please fill Carefully!!!",Toast.LENGTH_SHORT).show()
            }
        }

    }
// kuch issue hain signup karte hi crash ho jata hain
    private fun signup(email: String, password: String, username: String) {
             auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { it->
                 if(it.isSuccessful){
                     FirebaseuserId= auth.uid!!
                      reference.child("Users").child(auth.uid!!).child("username").setValue(username)


                  if(imagecontrol) {

                      val randUid: UUID = UUID.randomUUID()
                      val name = "image/*" + randUid + ".jpg"
                      storagereference.child(name).putFile(imageuri).addOnSuccessListener {
                          val myStoragereferece: StorageReference =
                              firebasestorage.getReference(name)
                          myStoragereferece.downloadUrl.addOnSuccessListener {
                              val filepath = it.toString()
                              reference.child("Users").child(auth.uid!!).child("image").setValue(filepath).addOnSuccessListener {
                                      Toast.makeText(
                                          this,
                                          "Write to database is successfull",
                                          Toast.LENGTH_SHORT
                                      ).show()
                                  }.addOnFailureListener {
                                      Toast.makeText(
                                          this,
                                          "Write to database is not successfull",
                                          Toast.LENGTH_SHORT
                                      ).show()
                                  }
                              }
                          }
                      }
                     else{
                        reference.child("Users").child(FirebaseuserId).child("image").setValue("null")
                  }

                     intent = Intent(this,ProfileActivity::class.java)

                        startActivity(intent)
                     finish()

                 }else{
                      Toast.makeText(this,"Something went wront",Toast.LENGTH_SHORT).show()

                 }
             }
    }



    private fun imagechooser() {

        getresult.launch("image/*")
    }


    private val getresult = registerForActivityResult(ActivityResultContracts.GetContent()) {result->
                if( result!=null){
                  imageuri  = result
                    Picasso.get().load(imageuri)
                        .resize(6000, 6000)
                        .onlyScaleDown()
                        .into(circularImageview)
                    imagecontrol = true

                }else imagecontrol =false
    }
    }

