package com.example.chatapplication

import android.content.Context
import android.content.Intent
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

   class UsersAdapter(val context: Context ,val username:String,val userlist: ArrayList<String  >) : RecyclerView.Adapter<UsersAdapter.ViewHolder>(){

       var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

        var reference : DatabaseReference = firebaseDatabase.getReference()

       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutinflate = LayoutInflater.from(context)
        val listitem = layoutinflate.inflate(R.layout.card_view_users,parent,false)
        return ViewHolder(listitem)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentuser=userlist[position]
         reference.child("Users").child(userlist[position]).addValueEventListener(object: ValueEventListener{
             override fun onDataChange(snapshot: DataSnapshot) {
               val othername = snapshot.child("username").value.toString()
                 val imageurl = snapshot.child("image").value.toString()
                 holder.textview.setText(othername)
                 if(imageurl.equals("null")){
                     holder.imageview.setImageResource(R.drawable.login_image)

                 }else{
                     Picasso.get().load(imageurl).into(holder.imageview)
                 }
                 holder.card_view.setOnClickListener {
                     val intent = Intent(context,mychatactivity:: class.java)
                     intent.putExtra("username",username)
                     intent.putExtra("othername",othername)
                     context.startActivity(intent)

                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 TODO("Not yet implemented")
             }

         })

    }

    override fun getItemCount(): Int {
          return userlist.size
    }


           class ViewHolder(val view: View) : RecyclerView.ViewHolder(view){
           lateinit var card_view :CardView
           lateinit var imageview : CircleImageView
           lateinit var textview : TextView

           init {
               card_view  = view.findViewById(R.id.card_view)
               imageview = view.findViewById(R.id.imageview_users)
               textview = view.findViewById(R.id.textView_Users)

           }

       }

}
