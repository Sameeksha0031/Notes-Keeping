package com.example.noteskeeping.view

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.noteskeeping.databinding.FragmentDialogBinding
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices
import com.example.noteskeeping.viewModel.CustomDialogModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class CustomDialogFragment : DialogFragment() {
    lateinit var binding: FragmentDialogBinding
    lateinit var customDialogModel: CustomDialogModel
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFireStore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogBinding.inflate(inflater,container,false)
        customDialogModel = CustomDialogModel(UserAuthServices())
        auth = FirebaseAuth.getInstance()
        firebaseFireStore = FirebaseFirestore.getInstance()
        //firebaseStore = FirebaseStorage.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storageReference = FirebaseStorage.getInstance().reference
        firebaseStore = FirebaseStorage.getInstance()

        customDialogModel.viewProfile(
            User(
                userId = "",
                userName = "",
                email = "",
                password = "",
                profile = ""
            )
        )
        customDialogModel.profileView.observe(viewLifecycleOwner, Observer {
            if (it.status) {
                binding.retriveEmail.text = it.user.email
                binding.retriveName.text = it.user.userName
                var media = it.user.profile
                if(media != null){
                    Glide.with(requireContext())
                        .load(media)
                        .into(binding.profileImage)
                }
                Toast.makeText(requireContext(), it.msg, Toast.LENGTH_LONG).show()
            }
        })
        binding.profileImage.setOnClickListener{
            launchGallery()
            Toast.makeText(context,"Opening Gallery",Toast.LENGTH_SHORT).show()
        }

        binding.addProfileImage.setOnClickListener{
            val user = User(userId = "", userName = "", email = "", password = "", profile = "")
            customDialogModel.changeProfileImage(user,filePath)
            customDialogModel.profileImage.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(context,it.msg,Toast.LENGTH_SHORT).show()
                }
            })
        }

        binding.logOut.setOnClickListener{

            auth.signOut()
            Log.d(TAG, "Sign Out")
            var intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)

        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
       if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if(data == null || data.data == null){
                return
            }

            filePath = data.data!!
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                binding.profileImage.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
//        if(requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK){
//            filePath = data?.data!!
//        }
    }
}