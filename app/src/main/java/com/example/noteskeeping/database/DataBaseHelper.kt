package com.example.noteskeeping.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.ContactsContract
import android.util.Log
import com.example.noteskeeping.model.Notes
import com.example.noteskeeping.model.User

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private val DB_NAME = "NotesDB"
        private val DB_VERSION = 1
        private val TABLE_NAME = "notes"
        private val NOTE_ID = "id"
        private val NOTE_TITLE = "title"
        private val NOTE = "note"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val CREATE_TABLE =
            "CREATE TABLE $TABLE_NAME ($NOTE_ID TEXT PRIMARY KEY AUTOINCREMENT, $NOTE_TITLE TEXT,$NOTE TEXT)"
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        p0?.execSQL(DROP_TABLE)
        onCreate(p0)
    }

    fun getALLNotes(): ArrayList<Notes> {
        val listOfNotes = ArrayList<Notes>()
        val db: SQLiteDatabase = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null,null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val note = Notes(
                        noteId = cursor.getColumnIndex(NOTE_ID).toString(),
                        title = cursor.getColumnIndex(
                            NOTE_TITLE
                        ).toString(),
                        notes = cursor.getColumnIndex(NOTE).toString()
                    )
                    listOfNotes.add(note)
                } while (cursor.moveToNext())
            }
            return listOfNotes
        }
       // cursor.close()
        return listOfNotes
    }

    fun addNotes(notes: Notes, noteId: String): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(NOTE_ID,noteId)
        values.put(NOTE_TITLE, notes.title)
        values.put(NOTE, notes.notes)
        val _success: Long = db.insert(TABLE_NAME, null, values)
        //db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    fun getSingleNote(noteId: String): Notes {
        // var user = User(userId, userName = "", email = "", password = "", profile = "")
        val db: SQLiteDatabase = this.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $NOTE_ID = '$noteId'"
        val cursor = db.rawQuery(selectQuery,null,null)
        cursor.moveToFirst()
        val note = Notes(
            noteId = cursor.getColumnIndex(NOTE_ID).toString(), title = cursor.getColumnIndex(
                NOTE_TITLE
            ).toString(), notes = cursor.getColumnIndex(NOTE).toString()
        )
    //    cursor.close() connectivity manager
        return note
    }

    fun deleteUser(noteId: String): Boolean {
        val db : SQLiteDatabase = this.writableDatabase
        val _success = db.delete(TABLE_NAME, NOTE_ID +"=?", arrayOf(noteId.toString())).toLong()
        //db.close()
        return _success.toInt() != -1
    }

    fun updateTask(note: Notes,noteId: String): Boolean{
        val db: SQLiteDatabase = this.writableDatabase
        val values = ContentValues()
        values.put(NOTE_ID, noteId)
        values.put(NOTE_TITLE,note.title)
        values.put(NOTE,note.notes)
        val _success : Long = db.update(TABLE_NAME,values, NOTE_ID + "=?", arrayOf(noteId.toString())).toLong()
       // db.close()
        return _success.toInt() != -1
    }
}