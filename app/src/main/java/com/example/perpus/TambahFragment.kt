package com.example.perpus

import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blogspot.atifsoftwares.circularimageview.CircularImageView
import com.theartofdev.edmodo.cropper.CropImage
import java.text.NumberFormat
import java.util.*

class TambahFragment : Fragment() {
    private var KodeBuku: EditText? = null
    private var Judul: EditText? = null
    private var Pembaca: EditText? = null
    private var Rating: EditText? = null
    private var Penerbit: EditText? = null
    private var Deskripsi: EditText? = null
    private var Harga: EditText? = null
    private var setKodeBuku: String? = null
    private var setJudul: String? = null
    private var setPembaca: String? = null
    private var setRating: String? = null
    private var setPenerbit: String? = null
    private var setDeskripsi: String? = null
    private var setHarga: String? = null
    private var dbBook: DbBook? = null
    private var Open: Button? = null
    var imageView: CircularImageView? = null
    var resultUri: Uri? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tambah, container, false)
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        val simpan = view.findViewById<Button>(R.id.btnSubmit)
        Open = view.findViewById(R.id.btnOpen)
        imageView = view.findViewById<View>(R.id.image_profile) as CircularImageView
        KodeBuku = view.findViewById(R.id.formKode)
        Judul = view.findViewById(R.id.formJudul)
        Pembaca = view.findViewById(R.id.formPembaca)
        Rating = view.findViewById(R.id.formRating)
        Harga = view.findViewById(R.id.formHarga)
        Deskripsi = view.findViewById(R.id.formDeskripsi)
        Penerbit = view.findViewById(R.id.formPenerbit)

        //Inisialisasi dan Mendapatkan Konteks dari DbBook
        dbBook = DbBook(activity!!.baseContext)
        simpan.setOnClickListener {
            setData()
            if (setKodeBuku == "" || setJudul == "" || setPembaca == "" || setRating == "" || setPenerbit == "" || setDeskripsi == "" || setHarga == "") {
                Toast.makeText(
                    activity!!.applicationContext,
                    "Data Buku Belum Lengkap atau Belum diisi, Lengkapi Dahulu!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                setData()
                saveData()
                Toast.makeText(
                    activity!!.applicationContext,
                    "Data Buku Tersimpan",
                    Toast.LENGTH_SHORT
                ).show()
                clearData()
            }
        }
        //intent eksternal untuk masuk kedalam folder atau galeri
        Open.setOnClickListener(View.OnClickListener {
            val intent = CropImage.activity().setAspectRatio(3, 4).getIntent(context!!)
            startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        })
        return view
    }

    //Berisi Statement-Statement Untuk Mendapatkan Input Dari User
    private fun setData() {
        setKodeBuku = KodeBuku!!.text.toString()
        setJudul = Judul!!.text.toString()
        setPembaca = Pembaca!!.text.toString()
        setRating = Rating!!.text.toString()
        setPenerbit = Penerbit!!.text.toString()
        setDeskripsi = Deskripsi!!.text.toString()
        setHarga = Harga!!.text.toString()
    }

    //Berisi Statement-Statement Untuk Menyimpan Data Pada Database
    private fun saveData() {
        //Mendapatkan Repository dengan Mode Menulis
        val create = dbBook!!.writableDatabase

        //Membuat Map Baru, Yang Berisi Judul Kolom dan Data Yang Ingin Dimasukan
        val values = ContentValues()
        values.put(DbBook.MyColumns.KodeBuku, setKodeBuku)
        values.put(DbBook.MyColumns.Judul, setJudul)
        values.put(DbBook.MyColumns.Pembaca, setPembaca)
        values.put(DbBook.MyColumns.Rating, setRating)
        values.put(DbBook.MyColumns.Penerbit, setPenerbit)
        values.put(DbBook.MyColumns.Deskripsi, setDeskripsi)
        values.put(DbBook.MyColumns.Harga, setHarga)
        values.put(DbBook.MyColumns.Foto, resultUri.toString())
        create.insert(DbBook.MyColumns.NamaTabel, null, values)
    }

    private fun clearData() {
        KodeBuku!!.setText("")
        Judul!!.setText("")
        Pembaca!!.setText("")
        Rating!!.setText("")
        Penerbit!!.setText("")
        Deskripsi!!.setText("")
        Harga!!.setText("")
        imageView!!.setImageResource(R.drawable.ic_picimg)
    }

    @TargetApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                resultUri = result.uri
                Log.e("resultUri ->", resultUri.toString())
                imageView!!.setImageURI(resultUri)
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.e("error ->", error.toString())
            }
        }
    }
}