package com.android.musiclub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.musiclub.adapters.FriendListRecyclerAdapter
import com.android.musiclub.databinding.FragmentFriendListBinding
import com.android.musiclub.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FriendListFragment : BaseFragment<FragmentFriendListBinding>() {

    private val friendListRecyclerAdapter: FriendListRecyclerAdapter by lazy {
        FriendListRecyclerAdapter()
    }

    companion object {
        var currentUser: UserModel? = null
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFriendListBinding {
        return FragmentFriendListBinding.inflate(inflater, container, false)
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

        friendListRecyclerAdapter.onItemClickListener = { friendUid ->
            findNavController().navigate(
                FriendListFragmentDirections.actionFriendListFragmentToChatLogFragment(friendUid)
            )
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

    override fun onBackPressed() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}