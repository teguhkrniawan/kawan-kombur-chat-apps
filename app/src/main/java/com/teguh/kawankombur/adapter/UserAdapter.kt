package com.teguh.kawankombur.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teguh.kawankombur.R
import com.teguh.kawankombur.model.Users
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.user_search_item_layout.view.*
import org.w3c.dom.Text

// adapter recyclerview user
class UserAdapter(mContext: Context, mUsers: List<Users>, isChatCheck: Boolean) :
    RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    // properti class
    private val mContext: Context
    private val mUsers: List<Users>
    private val isChatBoolean: Boolean

    init {
        this.mContext = mContext
        this.mUsers = mUsers
        this.isChatBoolean = isChatCheck
    }

    // function yg melakukan inflate item ke recyvlerview
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout, viewGroup, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsers.size // kembalikan nilai size user dalam list
    }

    // function yg melakukan masing" id itu melakukan apa
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        isiModelUsers(holder, position)

        val user: Users? = mUsers[position] // menjadikan list mUsers yg posisinya masing" kedalam objek
        holder.usernameText.text = user!!.getUsername()
        Picasso.get().load(user.getPicture()).placeholder(R.drawable.ic_profile).into(holder.profileSearch)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        // properti pada class
        var usernameText: TextView
        var profileSearch: CircleImageView
        var onlineImage: CircleImageView
        var offlineImage: CircleImageView
        var lastMessageText: TextView

        // init digunakan untuk menambahkan perintah kode lainnya selain primary constructor
        // tetapi tetap dijalankan pertama
        init {
            usernameText = itemView.findViewById(R.id.search_username_text)
            profileSearch = itemView.findViewById(R.id.search_profile_image)
            onlineImage = itemView.findViewById(R.id.profile_online)
            offlineImage = itemView.findViewById(R.id.profile_offline)
            lastMessageText = itemView.findViewById(R.id.message_last)
        }
    }

    fun isiModelUsers(holder: ViewHolder, position: Int){
        val user: Users? = mUsers[position]
        Log.i("Model in BindViewHolder", "username: "+user!!.getUsername())
        Log.i("Model in BindViewHolder", "profilePict: "+user.getPicture())
    }

}