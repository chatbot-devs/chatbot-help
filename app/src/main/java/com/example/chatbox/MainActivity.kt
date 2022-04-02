package com.example.chatbox

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.security.Timestamp
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    val CHANNEL_ID  = "channelID"
    val CHANNEL_NAME =  "channelName"
    val NOTIFICATION_ID = 0

    // optionsmenu code start
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.optionsmenu, menu)
        return true
    }

    //options menu open settings fragment (root preferences) code start




    //override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
      //  return when (item.itemId) {
        //    R.id.action_settings -> true
          //  else -> super.onOptionsItemSelected(item)
     //   }
    //}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // splash screen code:
        setTheme(R.style.Theme_Chatbox)
        // spash screen code ended
        setContentView(R.layout.activity_main)
        createNotificationChannel()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run{
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        sende_button.setOnClickListener { view -> addRecord() }

        setupChatListRecyclerView()

// message clicked Code starts here
      /*  val messageclicked = findViewById<TextView>(R.id.msg_container_user)
        // set on-click listener
        messageclicked.setOnLongClickListener{
            Toast.makeText(this, "Nachricht ist nun anklickbar!", Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener true
        }
*/


        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Backup Created")
            .setContentText("A Backup has been successfully created (not)")
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        notify_btn.setOnClickListener {
            notificationManager.notify(NOTIFICATION_ID, notification)
        }
    }
    fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT).apply {
                    lightColor = Color.YELLOW
                    enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


    /**
     * Zeigt den Chatverlauf an
     */
    private fun setupChatListRecyclerView() {

        if (getChatList().size > 0) {

            // Legen Sie den LayoutManager fest, den dieser RecyclerView verwendet.
            recyclerView.layoutManager = LinearLayoutManager(this)
            // Adapterklasse wird initialisiert und Liste wird im Parameter übergeben.
            val itemAdapter = ChatAdapter(this, getChatList())
            recyclerView.adapter = itemAdapter
        }
    }

    // Die Funktion wird verwendet, um die daten aus der Datenbanktabelle zu erhalten.
    private fun getChatList(): ArrayList<ChatData> {
        val Datenbankhandler: DatenbankChat = DatenbankChat(this)
        val chatList: ArrayList<ChatData> = Datenbankhandler.viewChatVerlauf()
        return chatList
    }

    private fun addRecord() {
        val chattext = editTextTextMultiLine.text.toString()
        val DatenbankHandler: DatenbankChat = DatenbankChat(this)
        if (chattext.isNotEmpty()) {
           // val date = Date().getTime()
            val status = DatenbankHandler.addChatText(ChatData(0, "", chattext, 0))
            if (status > -1) {
                editTextTextMultiLine.text.clear()
                setupChatListRecyclerView()
            }
          }
        }

    fun updateRecordDialog(emp: ChatData) {
        Toast.makeText(this,
            "Nachricht ist nun anklickbar!\n\n "+emp.text,
            Toast.LENGTH_SHORT).show()
    }
}


override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.main, menu)
    return true
}

override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when(item.itemId){
        R.id.action_settings -> {
            true
        }
        R.id.action_logout -> {
            signOut()
            true
        }
        else -> return super.onOptionsItemSelected(item)
    }
}

private fun signOut(){
    MainActivity.clearToken()
    startSplashScreenActivity()
}

private fun startSplashScreenActivity(){
    val intent = Intent(GrepToDo.applicationContext(), ::class.java)
    startActivity(intent)
    finish()
}