package com.example.appcommerceclone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentUserProfileBinding
import com.example.appcommerceclone.model.user.Address
import com.example.appcommerceclone.model.user.Name
import com.example.appcommerceclone.model.user.User
import com.example.appcommerceclone.util.UserExt.verifyUserExistsToProceed
import com.example.appcommerceclone.util.ViewExt
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserProfileFragment : Fragment() {

    private lateinit var binding: FragmentUserProfileBinding
    val connectivityViewModel by activityViewModels<ConnectivityViewModel>()
    val userViewModel by activityViewModels<UserViewModel>()

    private var isEditMode = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUserExistsToProceed(userViewModel) { user ->
            binding.user = user
            observeProfilePictureChanges()
            setupProfilePictureUpdateClickListener()
            setupSaveUpdateBtnClickListener(user)
            setupCancelBtnClickListener()
            setupLogoutBtnClickListener()
        }
    }


    private fun observeProfilePictureChanges() {
        userViewModel.userProfilePic.observe(viewLifecycleOwner) { hasUri ->
            hasUri?.let { uri ->
                Glide.with(requireActivity())
                    .load(uri)
                    .fitCenter()
                    .sizeMultiplier(0.25f)
                    .into(binding.userProfileImage)
            }
        }
    }


    private fun setupProfilePictureUpdateClickListener() {
        binding.userProfileUpdateProfilePicture.setOnClickListener {
            val toDestination = UserProfileFragmentDirections.actionUserProfileFragmentToPictureChooserDialogFragment()
            findNavController().navigate(toDestination)
        }
    }

    private fun setupSaveUpdateBtnClickListener(user: User) {
        binding.userProfileSaveOrUpdateBtn.setOnClickListener {
            if (isEditMode) updateProfile(user)
            else startUserUpdateProcess()
        }
    }

    private fun setupCancelBtnClickListener() {
        binding.userProfileCancelBtn.setOnClickListener {
            finishUserUpdateProcess()
        }
    }

    private fun setupLogoutBtnClickListener() {
        binding.userProfileLogoutBtn.setOnClickListener {
            userViewModel.logout()
        }
    }


    private fun changeEditMode() {
        binding.userProfileConstraintLayout.requestFocus()
        binding.isEditMode = isEditMode
    }

    private fun startUserUpdateProcess() {
        binding.userProfileCancelBtn.visibility = View.VISIBLE
        binding.userProfileSaveOrUpdateBtn.text = getString(R.string.user_profile_save_update_btn)
        isEditMode = true
        changeEditMode()
    }

    private fun validateUpdatedUserInfo(): Boolean {
        val isValid1 = ViewExt.validateEditText(binding.userProfileEmail, binding.userProfileEmailText, getString(R.string.user_error_no_email))
        val isValid2 = ViewExt.validateEditText(binding.userProfileUsername, binding.userProfileUsernameText, getString(R.string.user_error_no_username))
        val isValid3 = ViewExt.validateEditText(binding.userProfilePassword, binding.userProfilePasswordText, getString(R.string.user_error_no_password))
        return isValid1 && isValid2 && isValid3
    }

    private fun updateProfile(user: User) {
        if (validateUpdatedUserInfo()) {
            val updatedUser = user.copy(
                name = Name(
                    firstname = binding.userProfileFirstNameText.text.toString(),
                    lastname = binding.userProfileLastNameText.text.toString()
                ),
                username = binding.userProfileUsernameText.text.toString(),
                password = binding.userProfilePasswordText.text.toString(),
                email = binding.userProfileEmailText.text.toString(),
                phone = binding.userProfilePhoneText.text.toString(),
                address = Address(
                    city = binding.userProfileCityText.text.toString(),
                    number = binding.userProfileHouseNumberText.text.toString().toInt(),
                    street = binding.userProfileStreetText.text.toString(),
                    zipcode = binding.userProfileZipcodeText.text.toString()
                )
            )

            userViewModel.updateUser(updatedUser)

            finishUserUpdateProcess()
            ViewExt.showSnackbar(binding.root, getString(R.string.user_profile_updated_message))
        }
    }

    private fun finishUserUpdateProcess() {
        binding.userProfileCancelBtn.visibility = View.GONE
        binding.userProfileSaveOrUpdateBtn.text = getString(R.string.user_profile_start_update_btn)
        isEditMode = false
        changeEditMode()
    }
}