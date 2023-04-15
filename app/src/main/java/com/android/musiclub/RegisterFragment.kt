package com.android.musiclub

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.android.musiclub.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth


class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
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

        signUpMember(nickname, email, password, confirmPassword)
    }

    private fun signUpMember(nickname: String, email: String, password: String, confirmPassword: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) return@addOnCompleteListener
                Toast.makeText(requireContext(),
                    "successfully created", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(),
                    "Failed to create", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}