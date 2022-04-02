package com.example.chatbox

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

class DatenbankChat(context: Context) : SQLiteOpenHelper(context, DBNAME,null,DATENBANK_VERSION){


   companion object {
            private val DATENBANK_VERSION = 2
            private val DBNAME = "Chatdb"

            private val TABLE_NAME = "Chattable"

            private val FELD_ID = "_id"
            private val FELD_NAME = "name"
            private val FELD_TEXT = "text"
            private val FELD_DATUM = "time"
        }
    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_TABLE = ("CREATE TABLE IF NOT EXISTS" + TABLE_NAME +
                                "("+ FELD_ID + " INTEGER PRIMARY KEY,"
                                   + FELD_NAME + " TEXT,"
                                   + FELD_TEXT + " TEXT,"
                                   + FELD_DATUM + " TIMESTAMP NOT NULL)"
                               )
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    /**
     * Function um Nachricht zu speichern
     */
    fun addChatText(emp: ChatData): Long {
        val db = this.writableDatabase
        val date = Date().getTime()
        val contentValues = ContentValues()
        contentValues.put(FELD_NAME, emp.name) // ChatData Name
        contentValues.put(FELD_TEXT, emp.text) // ChatData text
        contentValues.put(FELD_DATUM, date) // ChatData datum

        // Einfügen von Chattext mithilfe einer Einfügeabfrage.
        val success = db.insert(TABLE_NAME, null, contentValues)

        db.close()
        return success
    }
    //Method to read the records from database in form of ArrayList
    fun viewChatVerlauf(): ArrayList<ChatData> {

        val ChatList: ArrayList<ChatData> = ArrayList<ChatData>()

        // Abfrage, um alle Datensätze aus der Tabelle auszuwählen.
        val selectQuery = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase
        // Der Cursor wird verwendet, um den Datensatz einzeln zu lesen. Fügen Sie sie der Datenmodellklasse hinzu.
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var text: String
        var datum : Long

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(FELD_ID))
                name = cursor.getString(cursor.getColumnIndex(FELD_NAME))
                text = cursor.getString(cursor.getColumnIndex(FELD_TEXT))
                datum = cursor.getLong(cursor.getColumnIndex(FELD_DATUM))

                val cD = ChatData(id = id, name = name, text = text, datum = datum)
                ChatList.add(cD)
                } while (cursor.moveToNext())
        }
        return ChatList
    }

    }