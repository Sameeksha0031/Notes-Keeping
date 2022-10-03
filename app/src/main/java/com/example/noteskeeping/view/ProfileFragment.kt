package com.example.noteskeeping.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.noteskeeping.R
import com.example.noteskeeping.databinding.FragmentProfileBinding
import com.example.noteskeeping.model.User
import com.example.noteskeeping.model.UserAuthServices
import com.example.noteskeeping.viewModel.ProfileModelFragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import java.util.*

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private var firebaseStore: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    lateinit var imagePreview: ImageView
    lateinit var btn_choose_image: Button
    lateinit var btn_upload_image: Button
    var profileModelFragment = ProfileModelFragment(UserAuthServices())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        storageReference = FirebaseStorage.getInstance().reference
        firebaseStore = FirebaseStorage.getInstance()
        btn_choose_image = binding.btnChooseImage
        btn_upload_image = binding.btnUploadImage
        imagePreview = binding.imagePreview

        btn_choose_image.setOnClickListener{launchGallery()}


        btn_upload_image.setOnClickListener {
            val user = User(userId = "", userName = "", email = "", password = "", profile = "")
            profileModelFragment.saveImage(user,filePath!!)
            profileModelFragment.profileUser.observe(viewLifecycleOwner , androidx.lifecycle.Observer {
                if(it.status){
                    Toast.makeText(requireContext(),it.msg,Toast.LENGTH_LONG).show()
                }
            })
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

            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, filePath)
                imagePreview.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /*private fun uploadImage(){
        if(filePath != null){
            val ref = storageReference?.child("myImages/" + UUID.randomUUID().toString())
            val uploadTask = ref?.putFile(filePath!!)

        }else{
            Toast.makeText(requireContext(), "Please Upload an Image", Toast.LENGTH_SHORT).show()
        }
    }*/
}