package com.android.musiclub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.musiclub.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        binding.txtRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun performLogin() {
   //   val client = OkHttpClient()
//
   //     val request = Request.Builder()
   //         .url("https://publicobject.com/helloworld.txt")
   //         .build()
//
//
   //         client.newCall(request).execute().use { response ->
   //             if (!response.isSuccessful) {
   //                 throw IOException("������ � ������� �� ��� �������:" +
   //                         " ${response.code} ${response.message}")
   //             }
   //             // ������ ��������� ����������� ��������� ������
   //             println("Server: ${response.header("Server")}")
   //             // ����� ���� ������
   //             println(response.body!!.string())
   //         }
//
    }

    override fun onBackPressed() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}