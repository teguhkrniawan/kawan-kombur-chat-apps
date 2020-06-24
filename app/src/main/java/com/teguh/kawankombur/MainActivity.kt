package com.teguh.kawankombur

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.teguh.kawankombur.fragments.ChatsFragment
import com.teguh.kawankombur.fragments.SearchFragment
import com.teguh.kawankombur.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        // Deklarasi variabel Toolbar setting
        val toolbar: Toolbar = findViewById(R.id.main_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "" // judul toolbar
        // Tablayout
        val tabLayout: TabLayout = findViewById(R.id.main_tablayout)
        // ViewPager
        val viewPager: ViewPager = findViewById(R.id.main_viewpager)
        // buat objek adapter view pager
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)

        viewPagerAdapter.addFragment(ChatsFragment(), "Chats") // index fragments [0]
        viewPagerAdapter.addFragment(SearchFragment(), "Search") // index fragments [1]
        viewPagerAdapter.addFragment(SettingsFragment(), "Settings") // index fragments [2]
        // set adapter ke xml
        viewPager.adapter = viewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * params: fragmentManager
     * extends : FragmentManagerAdapter
     */
    internal class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager)
    {
        private val fragments: ArrayList<Fragment> // fragment collection
        private val titles: ArrayList<String> // string collection

        init {
            fragments = ArrayList<Fragment>()
            titles = ArrayList<String>()
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position] // return index fragment sesuai value parameter yg ditangkap
        }

        override fun getCount(): Int {
           return fragments.size // return jumlah keseluruhan size fragments dalam index
        }

        // function menambahkan fragment
        fun addFragment(fragment: Fragment, title: String){
            // tambahkan ke collection fragments
            fragments.add(fragment)
            // tambahkan ke collection titles
            titles.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titles[position] // return index titles sesuai parameter
        }

    }
}