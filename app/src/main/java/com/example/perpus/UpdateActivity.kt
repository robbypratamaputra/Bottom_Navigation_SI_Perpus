package com.example.perpus

import android.Manifest
import android.annotation.TargetApi
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.blogspot.atifsoftwares.circularimageview.CircularImageView
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class UpdateActivity : AppCompatActivity() {
    private var MyDatabase: DbBook? = null
    private var NewKode: EditText? = null
    private var NewJudul: EditText? = null
    private var NewPembaca: EditText? = null
    private var NewRating: EditText? = null
    private var NewPenerbit: EditText? = null
    private var NewDeskripsi: EditText? = null
    private var NewHarga: EditText? = null
    private var NewImage: CircularImageView? = null
    private var getNewKode: String? = null
    private var Update: Button? = null
    private var Open: Button? = null
    private val KodeSend = "KODE"
    private var Back: ImageView? = null
    var resultUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)
        MyDatabase = DbBook(baseContext)
        NewKode = findViewById(R.id.NewformKode)
        NewJudul = findViewById(R.id.NewformJudul)
        NewPembaca = findViewById(R.id.NewformPembaca)
        NewRating = findViewById(R.id.NewformRating)
        NewPenerbit = findViewById(R.id.NewformPenerbit)
        NewHarga = findViewById(R.id.NewformHarga)
        NewDeskripsi = findViewById(R.id.NewformDeskripsi)
        NewImage = findViewById(R.id.Newimage_profile)
        Back = findViewById(R.id.back)
        val extras = intent.extras
        getNewKode = extras!!.getString(KodeSend)
        Update = findViewById(R.id.btnUpdate)
        Open = findViewById(R.id.NewbtnOpen)
        val ReadDb = MyDatabase!!.readableDatabase
        val cursor = ReadDb.rawQuery(
            "SELECT * FROM " + DbBook.MyColumns.NamaTabel + " WHERE " + DbBook.MyColumns.KodeBuku + "=" + getNewKode,
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
            val Harga = cursor.getString(cursor.getColumnIndex(DbBook.MyColumns.Harga))
            val Foto = cursor.getString(cursor.getColumnIndex(DbBook.MyColumns.Foto))
            NewKode.setText(Kode)
            NewJudul.setText(Judul)
            NewPembaca.setText(Pembaca)
            NewRating.setText(Rating)
            NewPenerbit.setText(Penerbit)
            NewDeskripsi.setText(Deskripsi)
            NewHarga.setText(Harga)
            NewImage.setImageURI(Uri.parse(Foto))
            Update.setOnClickListener(View.OnClickListener {
                setUpdateData()
                startActivity(Intent(this@UpdateActivity, MainActivity::class.java))
            })
            Back.setOnClickListener(View.OnClickListener { finish() })
            Open.setOnClickListener(View.OnClickListener { CropImage.startPickImageActivity(this@UpdateActivity) })
        }
    }

    private fun setUpdateData() {
        val ReadData = MyDatabase!!.readableDatabase
        val getKode = NewKode!!.text.toString().trim { it <= ' ' }
        val getJudul = NewJudul!!.text.toString().trim { it <= ' ' }
        val getPembaca = NewPembaca!!.text.toString().trim { it <= ' ' }
        val getRating = NewRating!!.text.toString().trim { it <= ' ' }
        val getPenerbit = NewPenerbit!!.text.toString().trim { it <= ' ' }
        val getDeskripsi = NewDeskripsi!!.text.toString().trim { it <= ' ' }
        val getHarga = NewHarga!!.text.toString().trim { it <= ' ' }


        //Memasukan Data baru pada
        val values = ContentValues()
        values.put(DbBook.MyColumns.KodeBuku, getKode)
        values.put(DbBook.MyColumns.Judul, getJudul)
        values.put(DbBook.MyColumns.Pembaca, getPembaca)
        values.put(DbBook.MyColumns.Rating, getRating)
        values.put(DbBook.MyColumns.Penerbit, getPenerbit)
        values.put(DbBook.MyColumns.Deskripsi, getDeskripsi)
        values.put(DbBook.MyColumns.Harga, getHarga)
        values.put(DbBook.MyColumns.Foto, resultUri.toString())

        //Untuk Menentukan Data/Item yang ingin diubah, berdasarkan NIM
        val selection = DbBook.MyColumns.KodeBuku + " LIKE ?"
        val selectionArgs = arrayOf(getNewKode)
        ReadData.update(DbBook.MyColumns.NamaTabel, values, selection, selectionArgs)
        Toast.makeText(applicationContext, "Berhasil Diubah", Toast.LENGTH_SHORT).show()
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
            && resultCode == RESULT_OK
        ) {
            val imageuri = CropImage.getPickImageResultUri(this, data)
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                resultUri = imageuri
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            } else {
                startCrop(imageuri)
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                NewImage!!.setImageURI(result.uri)
                resultUri = result.uri
            }
        }
    }

    private fun startCrop(imageuri: Uri) {
        CropImage.activity(imageuri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(3, 4)
            .start(this)
        resultUri = imageuri
    }
}