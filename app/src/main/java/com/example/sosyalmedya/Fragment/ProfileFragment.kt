package com.example.sosyalmedya.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sosyalmedya.*
import com.example.sosyalmedya.Adapter.PostAdapter
import com.example.sosyalmedya.Adapter.UserAdapter
import com.example.sosyalmedya.data.Post
import com.example.sosyalmedya.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_home.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var auth : FirebaseAuth
    private lateinit var recyclerViewList : RecyclerView
    private lateinit var database : FirebaseFirestore
    private var paylasimList = mutableListOf<Post>()
    private lateinit var rvAdapter : PostAdapter
    private lateinit var databaseFirestore : FirebaseFirestore
    private lateinit var firebaseUser: FirebaseUser


    var full_name_profile_frag:TextView?=null
    var bio_profile_frag:TextView?=null
    var total_posts:TextView?=null
    var ProfileImage:CircleImageView?=null



    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile, container, false)

        auth = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()
        databaseFirestore = Firebase.firestore
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        full_name_profile_frag=view.findViewById(R.id.full_name_profile_frag)
        ProfileImage=view.findViewById(R.id.ProfileImage)
        bio_profile_frag=view.findViewById(R.id.bio_profile_frag)
        total_posts=view.findViewById(R.id.total_posts)

        userInfo()


        //çıkış butonu
       var btn:ImageButton ?= null
        btn=view.findViewById(R.id.exit_btn)
        btn.setOnClickListener{
            auth.signOut()
            val intent = Intent(getActivity(), SignInActivity::class.java)
            getActivity()?.startActivity(intent)
        }


        // profil düzenleme
        var edit_profile_btn:Button ?= null
        edit_profile_btn=view.findViewById(R.id.edit_profile_btn)
        edit_profile_btn.setOnClickListener{
            val intent = Intent(getActivity(), Edit_ProfileActivity::class.java)
            getActivity()?.startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        database = Firebase.firestore
        if(auth.currentUser != null) {
            getPaylasimData()
        }


        rvAdapter = PostAdapter(paylasimList)
        recyclerViewList=view.rvList
        recyclerViewList.apply {
            adapter = rvAdapter
            layoutManager = LinearLayoutManager(this@ProfileFragment.context)
        }
        return view

    }


    private fun userInfo() {
        databaseFirestore.collection("users")
                .whereEqualTo("uid", FirebaseAuth.getInstance().currentUser!!.uid)
                //   .orderBy("tarih" , Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) throw exception

                    snapshot?.let {
                        if (!it.isEmpty) {
                            var user=snapshot.firstOrNull()?.toObject<User>()
                            full_name_profile_frag?.setText(user!!.username)
                            if (user?.image?.isEmpty() == true){
                                ProfileImage?.setImageResource(R.drawable.ic_user_1)

                            }else{
                                Picasso.get().load(user!!.image).into(ProfileImage)
                            }
                            bio_profile_frag?.setText(user!!.bio)
                        }

                    }
                }
    }


    private fun getPaylasimData(){
        auth = FirebaseAuth.getInstance()

        database.collection("posts")
                .whereEqualTo("kullaniciEmail", FirebaseAuth.getInstance().currentUser!!.email.toString())
                //   .orderBy("tarih" , Query.Direction.DESCENDING)
                .addSnapshotListener{ snapshot, exception ->
                    if(exception != null) throw exception

                    snapshot?.let{
                        if(!it.isEmpty){
                            Log.d("snapshot??",snapshot.documents.size.toString())

                            for(document in snapshot.documents){
                                var post = document.toObject(Post::class.java)
                                post!!.id = document.id;
                                paylasimList.add(post)
                            }
                            rvAdapter.updateList(paylasimList)
                            total_posts!!.setText(snapshot.documents.size.toString())

                        }
                    }
                }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}