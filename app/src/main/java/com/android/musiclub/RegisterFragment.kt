package com.android.musiclub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.musiclub.databinding.FragmentRegisterBinding
import com.android.musiclub.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            performRegister()
        }

        binding.txtLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun performRegister() {
        val nickname = binding.etxNickname.text.toString()
        val email = binding.etxEmail.text.toString()
        val password = binding.etxPassword.text.toString()
        val confirmPassword = binding.etxConfirmPassword.text.toString()

        if(nickname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(requireContext(),
            "please input all fields", Toast.LENGTH_SHORT).show()
            return
        }
        if( password != confirmPassword ) {
            Toast.makeText(requireContext(),
                "passwords don't match", Toast.LENGTH_SHORT).show()
            return
        }
        if( password.length < 6) {
            Toast.makeText(requireContext(),
                "password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }
        signUpMember(nickname, email, password, confirmPassword)
    }

    private fun signUpMember(nickname: String, email: String, password: String, confirmPassword: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                Toast.makeText(requireContext(),
                    "successfully created", Toast.LENGTH_SHORT).show()
                val userUid = it.result.user!!.uid
                saveUserToFirebaseDB(UserModel(nickname, userUid, null, null))
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(),
                    "Failed to create", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToFirebaseDB(userModel: UserModel) {

        Firebase.database.reference
            .child("users")
            .child(userModel.userUid)
            .setValue(userModel)
            .addOnSuccessListener {
                Toast.makeText(requireContext(),
                    "successfully saved", Toast.LENGTH_SHORT).show()
                this.findNavController().navigate(R.id.action_registerFragment_to_friend_List_Fragment)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(),
                    "save failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}