package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.lifecycle.Transformations.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import java.util.*
import kotlin.collections.ArrayList

class mychatactivity : AppCompatActivity() {
    lateinit var floatin_button : FloatingActionButton
    lateinit var rv_chatactivity : RecyclerView
    lateinit var edittxt_msg : EditText
    lateinit var txtview_name : TextView
    lateinit var arrow_back : ImageView
    lateinit var userName:String
    lateinit var otherName: String
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference :  DatabaseReference
    lateinit var  messageadpter : MessageAdapter
    lateinit var messageList : ArrayList<ModelClass>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mychatactivity)
        floatin_button = findViewById(R.id.floatingActionButton4)
        rv_chatactivity = findViewById(R.id.rv_chatActivity)
        edittxt_msg = findViewById(R.id.editTextTextMultiLine)
        txtview_name = findViewById(R.id.chat_name_textiew)
        arrow_back = findViewById(R.id.left_arrow_imageview)
        userName = intent.getStringExtra("username").toString()
        otherName = intent.getStringExtra("othername").toString()
        firebaseDatabase = FirebaseDatabase.getInstance()
        reference = firebaseDatabase.getReference()
        messageList = ArrayList()
        rv_chatactivity.layoutManager = LinearLayoutManager(this)


        txtview_name.setText(otherName)
        arrow_back.setOnClickListener{
            intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }
        getmessage()
        floatin_button.setOnClickListener{

            val message = edittxt_msg.text.toString()
            Toast.makeText(this, "Your meeesage is $message", Toast.LENGTH_SHORT).show()
            if(!message.equals("")){

                sendMessage(message)
                edittxt_msg.setText("")
            }else{
                Toast.makeText(this, "Bro", Toast.LENGTH_SHORT).show()
            }
        }



    }

    private fun sendMessage(message: String) {
        val key = reference.child("messages").child(userName).child(otherName).push().key.toString()

        val messagemap = hashMapOf<String,String>()
       messagemap.put("message",message)
        messagemap.put("fromm",userName)

            reference.child("messages").child(userName).child(otherName).child(key).setValue(messagemap).addOnCompleteListener{
                if(it.isSuccessful){
                    reference.child("messages").child(otherName).child(userName).child(key).setValue(messagemap)
                }else{
                    Toast.makeText(this, "Failed brop", Toast.LENGTH_SHORT).show()
                }
            }



    }
    private fun getmessage(){
        reference.child("messages").child(userName).child(otherName).addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                 val a  = snapshot.getValue(ModelClass::class.java)
                messageList.add(a!!)
                     messageadpter.notifyDataSetChanged()
                 rv_chatactivity.scrollToPosition(messageList.size-1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        messageadpter = MessageAdapter(this,messageList,userName)
        rv_chatactivity.adapter = messageadpter
    }
}


