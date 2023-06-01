package com.example.perpus

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.etebarian.meowbottomnavigation.MeowBottomNavigation.*

class MainActivity : AppCompatActivity() {
    private val NUM_PAGES = 4
    private var pager: ViewPager2? = null
    private val sendVal = "id"
    private var bottomNavigation: MeowBottomNavigation? = null
    private var adapter: ScreenSlidePagerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pager = findViewById(R.id.pager)
        adapter = ScreenSlidePagerAdapter(this)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        pager.setAdapter(adapter)
        pager.setCurrentItem(0)
        pager.setUserInputEnabled(false)
        bottomNavigation.show(1, true)
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.ic_home))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.ic_add))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.ic_list))
        bottomNavigation.add(MeowBottomNavigation.Model(4, R.drawable.ic_profile))
        bottomNavigation.setOnClickMenuListener(ClickListener { item ->
            when (item.id) {
                1 -> pager.setCurrentItem(0)
                2 -> pager.setCurrentItem(1)
                3 -> pager.setCurrentItem(2)
                4 -> pager.setCurrentItem(3)
                else -> pager.setCurrentItem(0)
            }
        })
        bottomNavigation.setOnShowListener(ShowListener {
            // your codes
        })
        bottomNavigation.setOnReselectListener(ReselectListener {
            // your codes
        })
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity?) : FragmentStateAdapter(
        fa!!
    ) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> TambahFragment()
                2 -> ListFragment()
                3 -> ProfileFragment()
                else -> HomeFragment()
            }
        }

        override fun getItemCount(): Int {
            return NUM_PAGES
        }
    }
}