package com.android.musiclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.musiclub.databinding.FragmentFriendListBinding
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class FriendListFragment : Fragment() {

    private var _binding: FragmentFriendListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFriendListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null) {
            findNavController().navigate(R.id.action_friendFragment_to_loginFragment)
        } else {
            fetchCurrentUser()

            binding.btnLogout.setOnClickListener{
               FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_friendFragment_to_loginFragment)
            }
        }
    }

    private fun fetchCurrentUser() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}