package com.android.musiclub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.messageRecyclerview.apply{

            adapter = RecyclerviewApadter()
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false )

        }

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
            chatModelList = mutableListOf(
                ChatRoomModel.Companion.ChatModel("Daun","PoshelYaNahuy","SEND", 0L, mutableMapOf()),
                ChatRoomModel.Companion.ChatModel("Whoayang","nuhiuya lil young","RECEIVER", 0L, mutableMapOf())
            )
            notifyDataSetChanged()
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

        override fun getItemCount(): Int = chatModelList.size

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (chatModelList[position].msgType == "SEND")
                (holder as TextRightViewHolder).bind(chatModelList[position])
            else
                    (holder as TextLeftViewHolder).bind(chatModelList[position])
        }

        override fun getItemViewType(position: Int): Int {
            return if (chatModelList[position].msgType == "SEND") TEXT_RIGHT else TEXT_LEFT
        }

        inner class TextLeftViewHolder(private val binding: ItemChatLeftBinding) : ViewHolder(binding.root) {
            fun bind(chat: ChatRoomModel.Companion.ChatModel) {
                binding.apply {
                    chat.also {
                        itemUsername.text = it.fromUid
                        itemMessage.text = it.message
                        itemTimestamp.text = "9:45"
                    }
                }

            }
        }
        inner class TextRightViewHolder(private val binding: ItemChatRightBinding) : ViewHolder(binding.root) {
            fun bind(chat: ChatRoomModel.Companion.ChatModel) {
                binding.apply {
                    chat.also {
                        itemMessage.text = it.message
                        itemTimestamp.text = "9:47"
                    }
                }
            }
        }

    }

}