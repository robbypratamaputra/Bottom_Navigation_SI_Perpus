package com.example.perpus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat
import java.util.*

class DetailActivity : AppCompatActivity() {
    private var MyDatabase: DbBook? = null
    private var ShowKode: TextView? = null
    private var ShowJudul: TextView? = null
    private var ShowPembaca: TextView? = null
    private var ShowRating: TextView? = null
    private var ShowPenerbit: TextView? = null
    private var ShowDeskripsi: TextView? = null
    private var ShowHarga: TextView? = null
    private var ShowImage: ImageView? = null
    private var Back: ImageView? = null
    private val Id: String? = null
    private var Update: Button? = null
    private val KodeSend = "KODE"
    private val sendVal = "id"
    var localeID = Locale("in", "ID")
    var formatRupiah = NumberFormat.getCurrencyInstance(localeID)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        MyDatabase = DbBook(baseContext)
        Update = findViewById(R.id.update)
        Back = findViewById(R.id.back)
        ShowKode = findViewById(R.id.KodeText)
        ShowImage = findViewById(R.id.imageDetail)
        ShowJudul = findViewById(R.id.judulBukuDetail)
        ShowPembaca = findViewById(R.id.BoxPembaca)
        ShowRating = findViewById(R.id.BoxRating)
        ShowPenerbit = findViewById(R.id.PenerbitDetail)
        ShowDeskripsi = findViewById(R.id.DeskripsiText)
        ShowHarga = findViewById(R.id.HargaDetail)
        data
        Update.setOnClickListener(View.OnClickListener {
            val Kode = ShowKode.getText().toString()
            if (Kode != null && Kode !== "") {
                val i = Intent(this@DetailActivity, UpdateActivity::class.java)
                i.putExtra(KodeSend, Kode)
                startActivity(i)
            }
        })
        Back.setOnClickListener(View.OnClickListener {
            val Kode = ShowKode.getText().toString()
            val intent = Intent(this@DetailActivity, MainActivity::class.java)
            intent.putExtra(sendVal, Kode)
            startActivity(intent)
        })
    }

    //Memulai Cursor pada Posisi Awal
    private val data: Unit
        private get() {
            val ReadData = MyDatabase!!.readableDatabase
            val cursor = ReadData.rawQuery(
                "SELECT * FROM " + DbBook.MyColumns.NamaTabel + " WHERE " + DbBook.MyColumns.KodeBuku + "=" + Id,
                null
            )
            cursor.moveToFirst() //Memulai Cursor pada Posisi Awal
            if (cursor.moveToFirst()) {
                val Kode = cursor.getString(cursor.getColumnIndex(DbBook.MyColumns.KodeBuku))
                val Judul = cursor.getString(cursor.getColumnIndex(DbBook.MyColumns.Judul))
                val Pembaca = cursor.getString(cursor.getColumnIndex(DbBook.MyColumns.Pembaca))
                val Rating = cursor.getString(cursor.getColumnIndex(DbBook.MyColumns.Rating))
                val Penerbit = cursor.getString(cursor.getColumnIndex(DbBook.MyColumns.Penerbit))
                val Deskripsi = cursor.getString(cursor.getColumnIndex(DbBook.MyColumns.Deskripsi))
                val Harga = cursor.getDouble(cursor.getColumnIndex(DbBook.MyColumns.Harga))
                val Foto = cursor.getString(cursor.getColumnIndex(DbBook.MyColumns.Foto))
                ShowKode!!.text = Kode
                ShowJudul!!.text = Judul
                ShowPembaca!!.text = Pembaca
                ShowRating!!.text = Rating
                ShowPenerbit!!.text = Penerbit
                ShowDeskripsi!!.text = Deskripsi
                ShowHarga!!.text = formatRupiah.format(Harga)
                ShowImage!!.setImageURI(Uri.parse(Foto))
            }
        }
}