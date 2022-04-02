package com.example.chatbox

import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbox.script.ChatAdapter
import com.example.chatbox.script.ChatData

class Chat : Fragment(), ChatAdapter.OnItemClickListener {

    private lateinit var backBtn : ImageButton
    private lateinit var toolbarMainText : TextView
    private var modus : Int = 0;
    private lateinit var rootView: View
    private lateinit var chatButton : Button
    private lateinit var chatstring : TextView
    private lateinit var recylerChat : RecyclerView
    private lateinit var chatedittool : androidx.appcompat.widget.Toolbar
    private lateinit var chatedittoolselect : TextView
    private lateinit var chatList: ArrayList<ChatData>


    private fun editmsgtool(position: Int, v: View){
        var conterselectet: Int = 0;
        if(chatList[position-1].select == false) {
            chatList[position-1].select = true
            v.setBackgroundColor(Color.parseColor("#5185BFB2"))
        }else{
            chatList[position-1].select = false
            v.setBackgroundColor(Color.parseColor("#FFFFFFFF"))
        }
        for ((index, value) in chatList.withIndex()){
            if(value.select==true) {
                conterselectet += 1
            }
        }
        if(conterselectet>0) {
            chatedittool.isVisible = true
            chatedittoolselect.text = conterselectet.toString()
        }else{
            chatedittool.isVisible = false
            modus=0
        }
    }

    override fun onItemClick(position: Int, v: View, chattextID : Int) {
        if(modus==1) {
            updateRecordDialog(position.toString())
            editmsgtool(chattextID,v)
            //v.setBackgroundColor(Color.parseColor("#5185BFB2"))
        }
    }

    override fun onItemLongClick(position: Int, v: View, chattextID : Int) {
        updateRecordDialog(position.toString())
        modus = 1
        editmsgtool(chattextID,v)
        //v.setBackgroundColor(Color.parseColor("#5185BFB2"))
    }

    private fun addRecord() {
        if(modus==1) {
            for ((index, value) in chatList.withIndex()){
                if(value.select==true) {
                    chatList[index].select = false
                }
            }
            chatedittool.isVisible = false
            modus=0
        }

        val chattext = chatstring.text.toString()
        val datenbankHandler = DB_Chat_Table(rootView.context)
        if (chattext.isNotEmpty()) {
            // val date = Date().getTime()
            val status = datenbankHandler.addChatText(ChatData(0, "", chattext, 0,false))
            if (status > -1) {
                chatstring.text = ""
                setupChatListRecyclerView()
            }
        }
    }

    fun updateRecordDialog(emp: String) {
        Toast.makeText(rootView.context,
            "Info :"+emp,
            Toast.LENGTH_LONG).show()
    }

}