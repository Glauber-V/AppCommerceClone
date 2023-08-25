package com.example.appcommerceclone.ui.user

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.example.appcommerceclone.databinding.FragmentPictureChooserDialogBinding
import com.example.appcommerceclone.util.getUri
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PictureChooserDialogFragment(private val userViewModel: UserViewModel) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPictureChooserDialogBinding

    private var resultImageUri: Uri? = null
    private val takePictureFromGallery = registerForActivityResult(ActivityResultContracts.GetContent()) { hasUri ->
        hasUri?.let { uri ->
            userViewModel.updateProfilePicture(uri)
            dismiss()
        }
    }
    private val takePictureFromCamera = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) resultImageUri?.let { uri ->
            userViewModel.updateProfilePicture(uri)
            dismiss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPictureChooserDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pictureChooserOptGallery.setOnClickListener {
            takePictureFromGallery.launch("image/*")
        }

        binding.pictureChooserOptCamera.setOnClickListener {
            resultImageUri = getUri(requireActivity().applicationContext)
            takePictureFromCamera.launch(resultImageUri)
        }
    }
}