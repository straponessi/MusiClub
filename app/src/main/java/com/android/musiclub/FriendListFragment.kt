package com.android.musiclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.experimental.UseExperimental
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.memory.MemoryCache
import com.android.musiclub.adapters.FriendListRecyclerAdapter
import com.android.musiclub.databinding.FragmentFriendListBinding
import com.android.musiclub.models.UserModel
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class FriendListFragment : Fragment() {

    private var _binding: FragmentFriendListBinding? = null
    private val binding get() = _binding!!

    private val friendListRecyclerAdapter: FriendListRecyclerAdapter by lazy {
        FriendListRecyclerAdapter()
    }

    companion object {
        var currentUser: UserModel? = null
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycleviewFriendList.apply {
            adapter = friendListRecyclerAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        }

        if (FirebaseAuth.getInstance().currentUser == null) {
            findNavController().navigate(R.id.action_friend_List_Fragment_to_loginFragment)
        } else {
            fetchCurrentUser()
            fetchUsers()
        }

        friendListRecyclerAdapter.onItemClickListener = { friend ->

        }
    }

    private  fun fetchUsers() {
        Firebase.database.reference
            .child("users")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if ( snapshot.exists()) {
                        val list = mutableListOf<UserModel>()
                        snapshot.children.forEach {
                            val user = it.getValue<UserModel>()
                            if (user!!.userUid != FirebaseAuth.getInstance().uid) list.add(user)
                        }
                        friendListRecyclerAdapter.setData(list)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun fetchCurrentUser() {
        val currentUserUid = FirebaseAuth.getInstance().uid
        Firebase.database.reference
            .child("/users/$currentUserUid")
            .addListenerForSingleValueEvent(object  : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        currentUser = snapshot.getValue<UserModel>()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}