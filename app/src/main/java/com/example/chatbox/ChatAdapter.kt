package com.example.chatbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.textboxausgabe.view.*
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter(val context: Context, val items: ArrayList<ChatData>) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {


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
        holder.tvtext.setOnLongClickListener{
            if (context is MainActivity) {
                context.updateRecordDialog(item)
            }
            return@setOnLongClickListener true
        }
        val timeZone = TimeZone.getTimeZone("Europe/Berlin")
        val sdf = SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.getDefault())
        sdf.timeZone = timeZone
        holder.tvtime.text = sdf.format((item.datum))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each item to
        val tvtext: TextView = view.textboxoutput
        val tvtime: TextView = view.timetext

    }
}