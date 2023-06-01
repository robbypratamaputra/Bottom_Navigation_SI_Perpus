package com.example.perpus

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener

class ListFragment : Fragment() {
    private var MyDatabase: DbBook? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
    private var layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
    private var JudulList: ArrayList<*>? = null
    private var PenerbitList: ArrayList<*>? = null
    private var KodeList: ArrayList<*>? = null
    private var HargaList: ArrayList<*>? = null
    private var FotoList: ArrayList<*>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        swipeRefreshLayout = view.findViewById<View>(R.id.swipeRefresh) as SwipeRefreshLayout
        swipeRefreshLayout!!.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh() {
                JudulList = ArrayList<Any>()
                PenerbitList = ArrayList<Any>()
                KodeList = ArrayList<Any>()
                HargaList = ArrayList<Any>()
                FotoList = ArrayList<Any>()
                MyDatabase = DbBook(activity!!.baseContext)
                recyclerView = view.findViewById(R.id.recycler)
                data
                layoutManager = LinearLayoutManager(activity)
                recyclerView.setLayoutManager(layoutManager)
                recyclerView.setHasFixedSize(true)
                adapter = RecyclerViewAdapter(
                    this@ListFragment,
                    JudulList,
                    PenerbitList,
                    KodeList,
                    HargaList,
                    FotoList
                )
                //Memasang Adapter pada RecyclerView
                recyclerView.setAdapter(adapter)
                //Membuat Underline pada Setiap Item Didalam List
                val itemDecoration = DividerItemDecoration(
                    activity!!.applicationContext,
                    DividerItemDecoration.VERTICAL
                )
                itemDecoration.setDrawable(
                    ContextCompat.getDrawable(
                        activity!!.applicationContext,
                        R.drawable.line
                    )!!
                )
                recyclerView.addItemDecoration(itemDecoration)
                swipeRefreshLayout!!.isRefreshing = false
            }
        })
        JudulList = ArrayList<Any>()
        PenerbitList = ArrayList<Any>()
        KodeList = ArrayList<Any>()
        HargaList = ArrayList<Any>()
        FotoList = ArrayList<Any>()
        MyDatabase = DbBook(activity!!.baseContext)
        recyclerView = view.findViewById(R.id.recycler)
        data
        layoutManager = LinearLayoutManager(activity)
        recyclerView.setLayoutManager(layoutManager)
        recyclerView.setHasFixedSize(true)
        adapter = RecyclerViewAdapter(
            this@ListFragment,
            JudulList,
            PenerbitList,
            KodeList,
            HargaList,
            FotoList
        )
        //Memasang Adapter pada RecyclerView
        recyclerView.setAdapter(adapter)
        //Membuat Underline pada Setiap Item Didalam List
        val itemDecoration =
            DividerItemDecoration(activity!!.applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                activity!!.applicationContext,
                R.drawable.line
            )!!
        )
        recyclerView.addItemDecoration(itemDecoration)
        return view
    }//Berpindah Posisi dari no index 0 hingga no index terakhir

//            Mengambil data dari sesuai kolom array
//Mengambil Repository dengan Mode Membaca
    //Memulai Cursor pada Posisi Awal

    //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
    //Berisi Statement-Statement Untuk Mengambi Data dari Database
    @get:SuppressLint("Recycle")
    protected val data: Unit
        protected get() {
            //Mengambil Repository dengan Mode Membaca
            val ReadData = MyDatabase!!.readableDatabase
            val cursor = ReadData.rawQuery("SELECT * FROM " + DbBook.MyColumns.NamaTabel, null)
            cursor.moveToFirst() //Memulai Cursor pada Posisi Awal

            //Melooping Sesuai Dengan Jumlan Data (Count) pada cursor
            for (count in 0 until cursor.count) {
                cursor.moveToPosition(count) //Berpindah Posisi dari no index 0 hingga no index terakhir

//            Mengambil data dari sesuai kolom array
                KodeList!!.add(cursor.getString(0))
                JudulList!!.add(cursor.getString(1))
                PenerbitList!!.add(cursor.getString(4))
                HargaList!!.add(cursor.getDouble(6))
                FotoList!!.add(cursor.getString(7))
            }
        }
}