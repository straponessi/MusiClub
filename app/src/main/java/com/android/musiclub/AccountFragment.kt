package com.android.musiclub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import coil.load
import coil.transform.CircleCropTransformation
import com.android.musiclub.databinding.FragmentAccountBinding
import com.android.musiclub.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

@Suppress("DEPRECATION")
class AccountFragment : BaseFragment<FragmentAccountBinding>() {

    private lateinit var currentUserUid: String
    private lateinit var  currentUser: UserModel
    private var userProfileImageUri: Uri? = null

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAccountBinding {
        return  FragmentAccountBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentUserUid = FirebaseAuth.getInstance().uid!!
        binding.progressbar.visibility = View.VISIBLE

        fetchCurrentUser()

        binding.imageProfile.setOnClickListener{
            cropImage(null)
        }

        binding.btnUpdate.setOnClickListener{
            val userName = binding.etxUsername.text.toString()
            if(userName == currentUser.userName && userProfileImageUri == null) return@setOnClickListener

            if (userName != currentUser.userName) performUpdateDB(userName)
            else saveImageToFirebaseStorage()
        }

        binding.btnSignout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(requireContext(), "Successfully signed out", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCurrentUser() {
        Firebase.database.reference
            .child("/users/$currentUser")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()) {
                        currentUser = snapshot.getValue<UserModel>()!!

                        initRenderView()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun initRenderView(){
        binding.apply {
            currentUser.also {
                etxEmail.setText(FirebaseAuth.getInstance().currentUser!!.email)
                etxUsername.setText(it.userName)

                if(it.userImageUri == "") {
                    imageProfile.load(R.drawable.baseline_account_circle_24)
                    binding.progressbar.visibility = View.GONE
                } else {
                    imageProfile.load(it.userImageUri) {
                        transformations(CircleCropTransformation())
                    }
                    binding.progressbar.visibility = View.GONE
                }
            }
        }
    }


    private fun cropImage(uri: Uri?) {
        context?.let {
            CropImage.activity(uri)
                .setActivityTitle("Crop Image")
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setCropMenuCropButtonTitle("Ok")
                .start(it, this)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = CropImage.getActivityResult(data)

        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE ->{
                result.uri?.let { uri ->
                    userProfileImageUri = uri
                    binding.imageProfile.load(userProfileImageUri) {
                        transformations(CircleCropTransformation())
                    }
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE -> {
                Log.e("TAG", result.error.toString())
            }
        }
    }

    private fun performUpdateDB(userName: String) {
        val mapUserName = mutableMapOf<String, String>()
        mapUserName["userName"] = userName

        Firebase.database.reference
            .child("/users/$currentUserUid")
            .updateChildren(mapUserName as Map<String, Any>)
            .addOnCanceledListener {
                if(userProfileImageUri != null) saveImageToFirebaseStorage()
                Toast.makeText(requireContext(),
                    "Successfully updated", Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveImageToFirebaseStorage() {
        binding.progressbar.visibility = View.VISIBLE

        val ref = Firebase.storage.reference
            .child("/userProfileImages/$currentUserUid")

        ref.putFile(userProfileImageUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    val imageUriMap = mutableMapOf<String, String>()
                    imageUriMap["userImageUri"] = it.toString()

                    Firebase.database.reference
                        .child("/users/$currentUserUid")
                        .updateChildren(imageUriMap as Map<String, Any>)
                        .addOnSuccessListener {
                            binding.progressbar.visibility = View.GONE
                            Toast.makeText(requireContext(),
                                "Successfully updated", Toast.LENGTH_SHORT).show()
                        }
                }
            }
    }

    override fun onBackPressed() {
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}