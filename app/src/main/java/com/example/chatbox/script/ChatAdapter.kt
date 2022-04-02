package com.example.chatbox.script

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatbox.R
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter(private val context: Context,
                  private val items: ArrayList<ChatData>,
                  private val listener: OnItemClickListener) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.textboxausgabe,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)
        holder.tvtext.text = item.text
        holder.chattextID = item.id
        val timeZone = TimeZone.getTimeZone("Europe/Berlin")
        val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault())
        sdf.timeZone = timeZone
        holder.tvtime.text = sdf.format((item.datum))
    }


    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view),View.OnLongClickListener, View.OnClickListener {
        val tvtext: TextView = view.findViewById(R.id.textboxoutput)
        val tvtime: TextView = view.findViewById(R.id.timetext)
        var chattextID : Int = 0
        init {
            view.setOnLongClickListener(this)
            view.setOnClickListener(this)
        }
        override fun onLongClick(v: View): Boolean {
            val position:Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemLongClick(position,v,chattextID)
            }
            return true
        }
        override fun onClick(v: View){
            val position:Int = adapterPosition
            if(position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position,v,chattextID)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, v: View, chattextID : Int)
        fun onItemLongClick(position: Int, v: View, chattextID : Int)
    }
}





