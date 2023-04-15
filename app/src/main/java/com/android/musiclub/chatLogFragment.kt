package com.android.musiclub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.android.musiclub.databinding.FragmentChatLogBinding
import com.android.musiclub.models.UserModel

class ChatLogFragment : BaseFragment<FragmentChatLogBinding>() {

    private val args: ChatLogFragmentArgs by navArgs()

    companion object {
        lateinit var friend: UserModel
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatLogBinding {
        return FragmentChatLogBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friend = args.friendSelected

    }

    override fun onBackPressed() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}