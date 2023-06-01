package com.example.perpus

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DbBook internal constructor(context: Context?) :
    SQLiteOpenHelper(context, NamaDatabse, null, VersiDatabase) {
    internal object MyColumns : BaseColumns {
        const val NamaTabel = "DataBuku"
        const val KodeBuku = "KodeBuku"
        const val Judul = "JudulBuku"
        const val Pembaca = "Pembaca"
        const val Rating = "Rating"
        const val Penerbit = "Penerbit"
        const val Deskripsi = "Deskripsi"
        const val Harga = "HargaSatuan"
        const val Foto = "GambarPic"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, i: Int, i1: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    } //    //Get 1 Data By ID

    //    public static Cursor oneData(Long id) {
    //        Cursor cur = db.rawQuery("SELECT * FROM " + MyColumns.NamaTabel + " WHERE " + MyColumns.KodeBuku + "=" + id, null);
    //        return cur;
    //    }
    companion object {
        private val db: SQLiteDatabase? = null
        private const val NamaDatabse = "dbmybook.db"
        private const val VersiDatabase = 1
        private const val SQL_CREATE_ENTRIES = ("CREATE TABLE " + MyColumns.NamaTabel +
                "(" + MyColumns.KodeBuku + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + MyColumns.Judul + " TEXT NOT NULL, "
                + MyColumns.Pembaca + " TEXT NOT NULL, "
                + MyColumns.Rating + " TEXT NOT NULL, "
                + MyColumns.Penerbit + " TEXT NOT NULL, "
                + MyColumns.Deskripsi + " TEXT NOT NULL, "
                + MyColumns.Harga + " TEXT NOT NULL, "
                + MyColumns.Foto + " TEXT NOT NULL)")
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MyColumns.NamaTabel
    }
}