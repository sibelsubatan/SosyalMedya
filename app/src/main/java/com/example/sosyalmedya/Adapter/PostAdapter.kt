package com.example.sosyalmedya.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sosyalmedya.R
import com.example.sosyalmedya.data.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_item.view.*



class PostAdapter(var paylasimList: List<Post>) : RecyclerView.Adapter<PostAdapter.PaylasimViewHolder>() {



    class PaylasimViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var databaseFirestore : FirebaseFirestore
        private val PostImageView: ImageView = itemView.PostImageView
        private val tvYorum: TextView = itemView.likess3
        private val user: TextView = itemView.user
        private val post_image_like_btn5: ImageButton = itemView.post_image_like_btn5
        private val likes: TextView = itemView.likes

        init{
            databaseFirestore = Firebase.firestore
        }
        //private val PostUserImage: CircleImageView = itemView.PostUserImage

        fun bind(paylasimModel: Post) {
            paylasimModel.apply {
                 tvYorum.text = aciklama
                 user.text = kullaniciEmail
               //likes.text=like.toString()
                if(like.toString() === "true"){
                    post_image_like_btn5.setImageResource(R.drawable.icon_heart)
                }
                else{
                    post_image_like_btn5.setImageResource(R.drawable.ic_icons8_heart_24)
                }
                Picasso.get().load(paylasimModel.imageUri).into(PostImageView)
            }

            post_image_like_btn5.setOnClickListener {
                paylasimModel.like =!paylasimModel.like
                databaseFirestore.collection("posts").document(paylasimModel.id).set(paylasimModel)
                        .addOnCompleteListener {


                        }
                        .addOnFailureListener {


                        }
                //usersRef.updateChildren(user.toMap())


            }
        }

    }



    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }
    val items: MutableList<String> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaylasimViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.row_item, parent, false)

        return PaylasimViewHolder(view)

    }

    override fun onBindViewHolder(holder: PaylasimViewHolder, position: Int) {
        holder.bind(paylasimList[position])
    }

    override fun getItemCount(): Int {
        return paylasimList.size
    }

    fun updateList(paylasimList: List<Post>) {
        this.paylasimList = paylasimList

        this.notifyDataSetChanged()
    }

}


