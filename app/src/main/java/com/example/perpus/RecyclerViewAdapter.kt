package com.example.perpus

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.circularimageview.CircularImageView
import java.text.NumberFormat
import java.util.*

class RecyclerViewAdapter  //Membuat Konstruktor pada Class RecyclerViewAdapter
internal constructor(
    listFragment: ListFragment?,
    private val judulList: ArrayList<*>,
    private val penerbitList: ArrayList<*>,
    private val kodeList: ArrayList<*>,
    private val hargaList: ArrayList<*>,
    private val fotoList: ArrayList<*>
) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    private var context: Context? = null
    var mRecyclerView: RecyclerView? = null

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val Detail: TextView
        private val Judul: TextView
        private val Penerbit: TextView
        private val Kode: TextView
        private val Harga: TextView
        private val Foto: CircularImageView
        private val Delete: ImageButton

        init {

            //Mendapatkan Context dari itemView yang terhubung dengan Activity ViewData
            context = itemView.context

            //Menginisialisasi View-View untuk kita gunakan pada RecyclerView
            mRecyclerView = itemView.findViewById(R.id.recycler)
            Judul = itemView.findViewById(R.id.Judul)
            Penerbit = itemView.findViewById(R.id.Penerbit)
            Kode = itemView.findViewById(R.id.KodeBukuText)
            Harga = itemView.findViewById(R.id.Harga)
            Detail = itemView.findViewById(R.id.Detail)
            Foto = itemView.findViewById(R.id.image)
            Delete = itemView.findViewById(R.id.delete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        val V = LayoutInflater.from(parent.context).inflate(R.layout.view_recycler, parent, false)
        return ViewHolder(V)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

        //Memanggil Nilai/Value Pada View-View Yang Telah Dibuat pada Posisi Tertentu
        val Judul =
            judulList[position] as String //Mengambil data (Judul) sesuai dengan posisi yang telah ditentukan
        val Penerbit = penerbitList[position] as String
        val Kode = kodeList[position] as String
        val Harga = hargaList[position] as Double
        val Foto = fotoList[position] as String
        holder.Judul.text = Judul
        holder.Penerbit.text = Penerbit
        holder.Kode.text = Kode
        //        holder.Harga.setText(Harga);
        holder.Harga.text = formatRupiah.format(Harga)
        holder.Foto.setImageURI(Uri.parse(Foto))

//      Panggil Onclik Hapus untuk Hapus db dan juga recyclveiw didalam showdialog
        holder.Delete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                showDialog()
            }

            //          dialog hapus
            private fun showDialog() {
                val alertDialogBuilder = AlertDialog.Builder(context)

                // set title dialog
                alertDialogBuilder.setTitle("Apakah Anda Ingin Menghapus Data ini?")

                // set pesan dari dialog
                alertDialogBuilder
                    .setIcon(R.drawable.ic_delete)
                    .setCancelable(false)
                    .setPositiveButton("Hapus") { dialog, id -> // jika tombol diklik, maka akan menutup activity ini

                        //Menghapus Data Dari Database
                        val getDatabase = DbBook(context)
                        val DeleteData = getDatabase.writableDatabase
                        //Menentukan di mana bagian kueri yang akan dipilih
                        val selection = DbBook.MyColumns.KodeBuku + " LIKE ?"
                        //Menentukan Judul Dari Data Yang Ingin Dihapus
                        val selectionArgs = arrayOf(holder.Kode.text.toString())
                        DeleteData.delete(DbBook.MyColumns.NamaTabel, selection, selectionArgs)

                        //Menghapus Data pada List dari Posisi Tertentu
                        val position = kodeList.indexOf(Kode)
                        kodeList.removeAt(position)
                        notifyItemRemoved(position)
                        Toast.makeText(context, "Data Dihapus", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel") { dialog, id -> // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel()
                    }

                // membuat alert dialog dari builder
                val alertDialog = alertDialogBuilder.create()

                // menampilkan alert dialog
                alertDialog.show()
            }
        })

//        onklik see detail
        holder.Detail.setOnClickListener { view ->
            val dataForm = Intent(view.context, DetailActivity::class.java)
            dataForm.putExtra("SendKode", holder.Kode.text.toString())
            context!!.startActivity(dataForm)
            (context as Activity?)!!.finish()
        }
    }

    override fun getItemCount(): Int {
        return kodeList.size
    }
}