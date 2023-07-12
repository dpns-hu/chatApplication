package com.example.chatapplication

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Adapter
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var btoon:Button
     lateinit var auth: FirebaseAuth
     lateinit var recyclerView: RecyclerView
     lateinit var user: FirebaseUser
     lateinit var  firebasedatabase : FirebaseDatabase
     lateinit var databasereference: DatabaseReference
     lateinit var getusername:String
     private lateinit var userList : ArrayList<String>
    private lateinit var adapter:UsersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView= findViewById(R.id.rv)
         recyclerView.layoutManager = LinearLayoutManager(this)

        userList = ArrayList()


        auth= FirebaseAuth.getInstance()
        user= auth.currentUser!!

        if(FirebaseAuth.getInstance().currentUser==null){
            intent= Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        }


        firebasedatabase = FirebaseDatabase.getInstance()
        databasereference = firebasedatabase.getReference()

        databasereference.child("Users").child(auth.currentUser!!.uid).child("username").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                getusername= snapshot.getValue().toString()
                getuser()
                adapter = UsersAdapter(this@MainActivity,getusername, userList)
                recyclerView.adapter = adapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


         
    }
    private fun getuser(){
        databasereference.child("Users").addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val key = snapshot.key
                if(!key.equals(user.uid!!)){
                    userList.add(key.toString())
                    adapter.notifyDataSetChanged()
                }
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
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
         val menuInflater:MenuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.chat_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
           if(item.itemId== R.id.Profile_chatmenu){
               intent = Intent(this,ProfileActivity::class.java)
               startActivity(intent)

           }else if(item.itemId==R.id.Sign_out_chatmenu){
               auth.signOut()
               startActivity(Intent(this,LoginActivity::class.java))

           }
        return super.onOptionsItemSelected(item)
    }

}

