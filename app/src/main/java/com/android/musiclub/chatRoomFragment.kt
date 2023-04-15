package com.android.musiclub

import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.musiclub.databinding.FragmentChatRoomBinding


class ChatRoomFragment : BaseFragment<FragmentChatRoomBinding>() {
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatRoomBinding {
        return FragmentChatRoomBinding.inflate(inflater, container, false)
    }

    override fun onBackPressed() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}