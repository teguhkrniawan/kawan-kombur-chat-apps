package com.teguh.kawankombur

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.teguh.kawankombur.fragments.ChatsFragment
import com.teguh.kawankombur.fragments.SearchFragment
import com.teguh.kawankombur.fragments.SettingsFragment
import com.teguh.kawankombur.model.Users
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // deklarasi properti
    var firebaseUser: FirebaseUser? = null
    var refUsers: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)

        // instance objek
        firebaseUser = FirebaseAuth.getInstance().currentUser // user yg login
        refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid) // referebce objek dimana uid = firebaseUser

        // binding findviewbyid xml
        val usernameText = findViewById<TextView>(R.id.username_text)
        val profileImage = findViewById<ImageView>(R.id.profile_image)

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

        // menampilkan username dan profile picture sesuai database
        refUsers!!.addValueEventListener(object: ValueEventListener{
            // ketika data diubah
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    // buat objek dari class Users, dimana datasnapshot dikirimkan ke class users
                    val user: Users? = p0.getValue(Users::class.java)
                    usernameText.text = user!!.getUsername()
                    Picasso
                        .get()
                        .load(user.getPicture())
                        .placeholder(R.drawable.ic_profile)
                        .into(profileImage)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
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
        when (item.itemId) {
            R.id.action_logout -> {
                // ambil instance firebaseAuth
                FirebaseAuth.getInstance().signOut()
                // lakukan intent
                val intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

        }
        return false
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