package com.example.chatapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.properties.Delegates

class ProfileActivity : AppCompatActivity() {
    lateinit var button : Button
    lateinit var textInputEditText: TextInputEditText
    lateinit var circulareimage : CircleImageView
    lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var firebaseuser : FirebaseUser
    lateinit var reference: DatabaseReference
    lateinit var imageuri : Uri
    var imagecontrol by Delegates.notNull<Boolean>()
    lateinit var firebasestorage : FirebaseStorage
    lateinit var storagereference : StorageReference
    var image =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
       button = findViewById(R.id.button_profile)
       textInputEditText = findViewById(R.id.edittext_profile)
        circulareimage = findViewById(R.id.circleImageView_profileactivity)

        auth = FirebaseAuth.getInstance()
        database  = FirebaseDatabase.getInstance()
        reference = database.getReference()
        firebaseuser = FirebaseAuth.getInstance().currentUser!!
        firebasestorage=FirebaseStorage.getInstance()
        storagereference = firebasestorage.getReference()
        getUserInfo()
        circulareimage.setOnClickListener {
            imagechooser()

        }
        button.setOnClickListener {
            updateInfo()
        }


    }
    private fun updateInfo(){
            val username = textInputEditText.text.toString()
            reference.child("Users").child(firebaseuser.uid).child("username").setValue(username)
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
        }else{
            reference.child("Users").child(firebaseuser.uid).child("username").setValue(image)
        }
//        intent = Intent(this,MainActivity::class.java)
//        intent.putExtra("Username",username)
//        startActivity(intent)
//        finish()

    }

    private fun getUserInfo(){
      reference.child("Users").child(firebaseuser.uid).addValueEventListener(postListener)

    }

    val postListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            val name = dataSnapshot.child("username").value.toString()
             image  = dataSnapshot.child("image").value.toString()
            textInputEditText.setText(name)
            if(image =="null"){
                circulareimage.setImageResource(R.drawable.no_image_)
            }else{
                Picasso.get().load(image).into(circulareimage)
            }
        }

        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Post failed, log a message
            Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
        }
    }
    private fun imagechooser() {

        getresult.launch("image/*")
    }


    private val getresult = registerForActivityResult(ActivityResultContracts.GetContent()) { result->
        if( result!=null){
            imageuri  = result
            Picasso.get().load(imageuri)
                .resize(6000, 6000)
                .onlyScaleDown()
                .into(circulareimage)
            imagecontrol = true

        }else imagecontrol =false
    }

}

