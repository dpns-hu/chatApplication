package com.example.chatapplication

import android.content.Context
import android.support.annotation.NonNull
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.cardview.widget.CardView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text

class MessageAdapter(val context : Context ,val messagelist: ArrayList<ModelClass>,val userName :String ) :
RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val send = 1
    val recieve = 2

      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       lateinit var view:View
        if(viewType==1){
        view = LayoutInflater.from(context).inflate(R.layout.card_send_msg,parent,false)
             return sentViewHolder(view)
       }else{
            view = LayoutInflater.from(context).inflate(R.layout.card_recieve_msg,parent,false)
           return recieveViewHolder(view)
        }

    }



    override fun getItemCount(): Int {
           return messagelist.size
    }




    override fun getItemViewType(position: Int): Int {
        val m = messagelist[position]
        if(messagelist.get(position).fromm.equals(userName)){

            return send
        }else{

            return recieve
        }

    }
    class sentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            val sentmessage  = itemView.findViewById<TextView>(R.id.msg_text)

    }
    class recieveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         val recievemeesage = itemView.findViewById<TextView>(R.id.msg_recive_txt)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messagelist[position]
     if(holder.javaClass == sentViewHolder::class.java){
              val viewHolder = holder as sentViewHolder

         holder.sentmessage.text = currentMessage.message

     }else{
          val viewHolder = holder as recieveViewHolder
         holder.recievemeesage.text = currentMessage.message
     }
    }
}