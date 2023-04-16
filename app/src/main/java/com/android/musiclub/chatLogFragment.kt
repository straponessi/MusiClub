package com.android.musiclub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.musiclub.databinding.FragmentChatLogBinding
import com.android.musiclub.databinding.ItemChatLeftBinding
import com.android.musiclub.databinding.ItemChatRightBinding
import com.android.musiclub.models.ChatRoomModel
import com.android.musiclub.models.UserModel
import com.google.firebase.auth.FirebaseAuth

private const val TEXT_LEFT = 1
private const val TEXT_RIGHT = 2

class ChatLogFragment : BaseFragment<FragmentChatLogBinding>() {

    private val args: ChatLogFragmentArgs by navArgs()

    private lateinit var friendUid: String
    private lateinit var currentUserUid: String


    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatLogBinding {
        return FragmentChatLogBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendUid = args.friendUidSelected
        currentUserUid = FirebaseAuth.getInstance().uid!!

    }

    override fun onBackPressed() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    inner class RecyclerviewApadter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        private var chatModelList = mutableListOf<ChatRoomModel.Companion.ChatModel>()

        private var friend: UserModel? = null

        init {

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType){
                TEXT_LEFT -> {
                    val inflater = LayoutInflater.from(parent.context)
                    val binding = ItemChatLeftBinding.inflate(inflater, parent, false)
                    TextLeftViewHolder(binding)
                }
                else -> {
                    val inflater = LayoutInflater.from(parent.context)
                    val binding = ItemChatRightBinding.inflate(inflater, parent, false)
                    TextRightViewHolder(binding)
                }
            }
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemViewType(position: Int): Int {
            return if (chatModelList[position].msgType == "SEND") TEXT_RIGHT else TEXT_LEFT
        }

        inner class TextLeftViewHolder(private val binding: ItemChatLeftBinding) : ViewHolder(binding.root) {
            fun bind(chat: ChatRoomModel.Companion.ChatModel) {

            }
        }
        inner class TextRightViewHolder(private val binding: ItemChatRightBinding) : ViewHolder(binding.root) {
            fun bind(chat: ChatRoomModel.Companion.ChatModel) {

            }
        }

    }

}