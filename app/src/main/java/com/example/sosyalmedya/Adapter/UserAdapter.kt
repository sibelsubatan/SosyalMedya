package com.example.sosyalmedya.Adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sosyalmedya.R
import com.example.sosyalmedya.data.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.row_item.view.*

class UserAdapter(var UserList: List<User>) : RecyclerView.Adapter<UserAdapter.PaylasimViewHolder>() {

    class PaylasimViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val user: TextView = itemView.user
      //  private val PostUserImage: CircleImageView = itemView.PostUserImage
        //   private val tvKullaniciEmail: TextView = itemView.rcycItemTvKullaniciEmail

        fun bind(userModel: User) {
            userModel.apply {
                user.text=username
             //   Picasso.get().load(userModel.image).into(PostUserImage)

            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaylasimViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_item, parent, false)

        return PaylasimViewHolder(view)

    }

    override fun onBindViewHolder(holder: PaylasimViewHolder, position: Int) {
        holder.bind(UserList[position])
    }

    override fun getItemCount(): Int {
        return UserList.size
    }

    fun updateUserList(UserList: List<User>) {
        this.UserList = UserList
        this.notifyDataSetChanged()
    }

}


