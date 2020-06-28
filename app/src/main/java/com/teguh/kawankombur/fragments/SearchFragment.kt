package com.teguh.kawankombur.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teguh.kawankombur.R
import com.teguh.kawankombur.adapter.UserAdapter
import com.teguh.kawankombur.model.Users
import kotlinx.android.synthetic.main.fragment_search.*
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    // properti

    private var userAdapter: UserAdapter? = null // objek class UserAdapter yg valuenya null
    private var mUsers: List<Users>? = null // objek collecction yg menampung class Users
    private var searchRecyclerview: RecyclerView? = null // objek reclerview yg nilalnya null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)

        // dekllarasi xml dengan findviewbyid

        val searchUserEdit = view.findViewById<EditText>(R.id.searh_user_edit)
        searchRecyclerview = view.findViewById<RecyclerView>(R.id.search_recyclerview)

        // lakukan setting tampilan pada recyclerview

        searchRecyclerview!!.setHasFixedSize(true)
        searchRecyclerview!!.layoutManager = LinearLayoutManager(context)

        mUsers = ArrayList() // buat mUsers sebagai arraylist
        tampilSemuaUsers() // panggil function tampilSemuaUsers


        // ketika editText search user disorot
        searchUserEdit.addTextChangedListener(object: TextWatcher{
            // sebelum sorot
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            // pas disorot, atau sedehananya pengguna aplikasi kita ngetik sesuatu
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // panggil function proses pencarian
                searchUser(p0.toString().toLowerCase(Locale.ROOT))
            }

            // setelah disorot
            override fun afterTextChanged(p0: Editable?) {

            }
        })

        return view
    }


    // function tampil semua users
    private fun tampilSemuaUsers() {

        val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid // uid user yg login
        val refUsers = FirebaseDatabase.getInstance().reference.child("Users") // mengambil data semua Users

        refUsers.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                // mUsers kan pertama bernilai null , maka kita kosongkan
                (mUsers as ArrayList<Users>).clear() // as adalah typecast yg memaksa mUsers untuk di konversikan sebagai arraylist<Users>,  jika tidak sesuai maka error: class cast exception
                // lakukan perulangan untuk mengambil data user dalam objek p0
                for (snapshot in p0.children){
                    // masukkan data kedalam model users dulu
                    val user: Users? = snapshot.getValue(Users::class.java)
                    // jika user nilai UID nya tidak sama dengan firebaseUserID
                    if (!(user!!.getUID().equals(firebaseUserID))){
                        // masukkan semua data ke arraylist
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }

                // kasi value ke userAdapter yg null tadi
                /**
                 * @param : context didapat dari parent,artinya fragment ini
                 * @param : mUsers dari properti yg uda diisi valuenya menjadi arraylist model Users pada function ini
                 * @param : false merupakan value isChatCheck
                 */
                userAdapter = UserAdapter(context!!, mUsers!!, false)
                // setAdapter untuk adapter recyclerview
                searchRecyclerview!!.adapter = userAdapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    // function proses mencari user sesuai inputan yaitu usename
    private fun searchUser(username: String){

        val firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val querySearch = FirebaseDatabase.getInstance().reference.child("Users")
                                          .orderByChild("search")
                                          .startAt(username)
                                          .endAt(username + "\uf8ff") // asumsi sementara uf8ff itu unicode pemgambil karakter, misalkan ketik fre, maka hasilnya fredy, freya,frecuk

        querySearch.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                // mUsers kan sudah bernilai data dari model Users, yg value nya semua data users, jdi hapus dulu
                (mUsers as ArrayList<Users>).clear() // as adalah typecast yg memaksa mUsers untuk di konversikan sebagai arraylist<Users>,  jika tidak sesuai maka error: class cast exception
                // lakukan perulangan untuk mengambil data user dalam objek p0
                for (snapshot in p0.children){
                    // masukkan data kedalam model users dulu
                    val user: Users? = snapshot.getValue(Users::class.java)
                    // jika user nilai UID nya tidak sama dengan firebaseUserID
                    if (user!!.getUID() != firebaseUserID){
                        // masukkan semua data ke arraylist
                        (mUsers as ArrayList<Users>).add(user)
                        Log.i("searchET", "onDataChange: "+ (mUsers as ArrayList<Users>).size)
                    }
                }
                // kasi value ke userAdapter yg null tadi
                /**
                 * @param : context didapat dari parent,artinya fragment ini
                 * @param : mUsers dari properti yg uda diisi valuenya menjadi arraylist model Users pada function ini
                 * @param : false merupakan value isChatCheck
                 */
                userAdapter = UserAdapter(context!!, mUsers!!, false)
                // setAdapter untuk adapter recyclerview
                searchRecyclerview!!.adapter = userAdapter

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }


}