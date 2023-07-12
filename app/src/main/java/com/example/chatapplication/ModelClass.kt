package com.example.chatapplication

import android.widget.ImageView
import de.hdodenhof.circleimageview.CircleImageView

public  class ModelClass {
    var message: String = ""
    var fromm : String = ""
    constructor(){}
    constructor(name: String,from:String  ){
        this.message = name
         this.fromm = from

    }
}