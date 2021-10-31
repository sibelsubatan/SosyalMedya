package com.example.sosyalmedya.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.sosyalmedya.Adapter.PostAdapter
import com.example.sosyalmedya.NewPostActivity
import com.example.sosyalmedya.R
import com.example.sosyalmedya.data.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.android.synthetic.main.fragment_home.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_home.view.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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

//    private lateinit var linearLayoutManager: LinearLayoutManager
//    private lateinit var firebaseUser: FirebaseUser
//    private lateinit var database: DatabaseReference


    private lateinit var recyclerViewList : RecyclerView
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseFirestore
    private var paylasimList = mutableListOf<Post>()
    private lateinit var rvAdapter : PostAdapter
    private lateinit var databaseFirestore : FirebaseFirestore
    private lateinit var firebaseUser: FirebaseUser



    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_home, container, false)


        databaseFirestore = Firebase.firestore
        firebaseUser = FirebaseAuth.getInstance().currentUser!!

        var exitBtn:ImageButton?=null
        exitBtn=view.findViewById(R.id.exit_btn)
        exitBtn.setOnClickListener{
            val intent = Intent (getActivity(), NewPostActivity::class.java)
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
            layoutManager = LinearLayoutManager(this@HomeFragment.context)
        }
        return view
    }

    val messageMap = HashMap<String,Post>()

    private fun onListItemClick(position: Int) {
       // Toast.makeText(this, mRepos[position].name, Toast.LENGTH_SHORT).show()
    }
    private fun getPaylasimData(){
        auth = FirebaseAuth.getInstance()

        database.collection("posts")
               // .whereEqualTo("kullaniciEmail", FirebaseAuth.getInstance().currentUser!!.email.toString())
                //   .orderBy("tarih" , Query.Direction.DESCENDING)
                .addSnapshotListener{ snapshot , exception ->
                    if(exception != null) throw exception

                    snapshot?.let{
                        if(!it.isEmpty){
                            Log.d("snapshot??",snapshot.documents[0].id)

                            for(document in snapshot.documents){
                                var post = document.toObject(Post::class.java)
                                post!!.id = document.id;
                                paylasimList.add(post)
                            }
                            rvAdapter.updateList(paylasimList)
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}