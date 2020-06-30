package com.teguh.kawankombur.fragments

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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

    lateinit var userAdapter: UserAdapter
    val lm = LinearLayoutManager(activity)
    val setUsersList: MutableList<Users> = ArrayList()

    val userUID = FirebaseAuth.getInstance().currentUser!!.uid
    val refUsers = FirebaseDatabase.getInstance().reference.child("Users")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        search_recyclerview.layoutManager = lm
        userAdapter = UserAdapter(requireActivity(), false)
        search_recyclerview.adapter = userAdapter

        initView()
    }

    // function tampil semua users
    private fun initView() {

        refUsers.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(context, "error pada fragment search addValueListener", Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (item in dataSnapshot.children){
                    val user:  Users? = item.getValue(Users::class.java)
                    setUsersList.add(user!!)
                    userAdapter.setUsers(setUsersList)
                }
            }

        })


    }

}