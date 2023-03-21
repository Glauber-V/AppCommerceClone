package com.example.appcommerceclone.ui.user

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.example.appcommerceclone.databinding.FragmentPictureChooserDialogBinding
import com.example.appcommerceclone.util.UriExt
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PictureChooserDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPictureChooserDialogBinding
    private val userViewModel by activityViewModels<UserViewModel>()

    private var resultImageUri: Uri? = null
    private val takePictureFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { hasUri ->
        hasUri?.let { uri ->
            userViewModel.updateUserProfilePicture(uri)
            dismiss()
        }
    }
    private val takePictureFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) resultImageUri?.let { uri ->
            userViewModel.updateUserProfilePicture(uri)
            dismiss()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPictureChooserDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDialogOptGallery()
        setupDialogOptCamera()
    }


    private fun setupDialogOptGallery() {
        binding.pictureChooserOptGallery.setOnClickListener {
            takePictureFromGallery.launch("image/*")
        }
    }

    private fun setupDialogOptCamera() {
        binding.pictureChooserOptCamera.setOnClickListener {
            resultImageUri = UriExt.getUri(requireActivity().applicationContext)
            takePictureFromCamera.launch(resultImageUri)
        }
    }
}