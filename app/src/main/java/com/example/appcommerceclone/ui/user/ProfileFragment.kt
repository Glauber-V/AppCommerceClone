package com.example.appcommerceclone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.model.Address
import com.example.appcommerceclone.data.user.model.Name
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.databinding.FragmentProfileBinding
import com.example.appcommerceclone.util.addOnTextChangedListener
import com.example.appcommerceclone.util.showSnackbar
import com.example.appcommerceclone.util.validate
import com.example.appcommerceclone.util.verifyUserToProceed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment(private val userViewModel: UserViewModel) : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var user: User
    private var isEditMode = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.currentUser.observe(viewLifecycleOwner) { _currentUser ->
            verifyUserToProceed(_currentUser) { verifiedUser: User ->
                user = verifiedUser
                binding.user = user
            }
        }

        userViewModel.profilePicture.observe(viewLifecycleOwner) { hasUri ->
            hasUri?.let { uri ->
                Glide.with(requireActivity())
                    .load(uri)
                    .fitCenter()
                    .placeholder(R.drawable.place_holder)
                    .into(binding.profileImage)
            }
        }

        binding.profileUpdateProfilePicture.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToPictureChooserDialogFragment()
            )
        }

        binding.profileEmail.addOnTextChangedListener()
        binding.profileUsername.addOnTextChangedListener()
        binding.profilePassword.addOnTextChangedListener()

        binding.profileSaveOrUpdateBtn.setOnClickListener {
            if (isEditMode) updateProfile()
            else startUserUpdateProcess()
        }

        binding.profileCancelBtn.setOnClickListener {
            cancelProfileUpdate()
        }

        binding.profileLogoutBtn.setOnClickListener {
            userViewModel.logout()
        }
    }

    private fun changeEditMode() {
        binding.profileConstraintLayout.requestFocus()
        binding.isEditMode = isEditMode
    }

    private fun startUserUpdateProcess() {
        binding.profileCancelBtn.visibility = View.VISIBLE
        binding.profileSaveOrUpdateBtn.text = getString(R.string.profile_save_update_btn)
        isEditMode = true
        changeEditMode()
    }

    private fun updateProfile() {
        val isEmailValid = binding.profileEmail.validate(getString(R.string.error_no_email))
        val isUsernameValid = binding.profileUsername.validate(getString(R.string.error_no_username))
        val isPasswordValid = binding.profilePassword.validate(getString(R.string.error_no_password))

        if (isEmailValid && isUsernameValid && isPasswordValid) {
            val updatedUser = user.copy(
                name = Name(
                    firstname = binding.profileFirstNameText.text.toString(),
                    lastname = binding.profileLastNameText.text.toString()
                ),
                username = binding.profileUsernameText.text.toString(),
                password = binding.profilePasswordText.text.toString(),
                email = binding.profileEmailText.text.toString(),
                phone = binding.profilePhoneText.text.toString(),
                address = Address(
                    city = binding.profileCityText.text.toString(),
                    number = binding.profileHouseNumberText.text.toString().toInt(),
                    street = binding.profileStreetText.text.toString(),
                    zipcode = binding.profileZipcodeText.text.toString()
                )
            )

            user = updatedUser
            binding.user = user

            finishUserUpdateProcess()
            showSnackbar(binding.root, getString(R.string.profile_updated_message))
        }
    }

    private fun cancelProfileUpdate() {
        binding.user = user
        finishUserUpdateProcess()
    }

    private fun finishUserUpdateProcess() {
        binding.profileCancelBtn.visibility = View.GONE
        binding.profileSaveOrUpdateBtn.text = getString(R.string.profile_start_update_btn)

        isEditMode = false

        changeEditMode()
    }
}