package com.example.appcommerceclone.ui.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentUserLoginBinding
import com.example.appcommerceclone.util.ViewExt.hideTextEditor
import com.example.appcommerceclone.util.ViewExt.showSnackbar
import com.example.appcommerceclone.util.ViewExt.showTextEditor
import com.example.appcommerceclone.util.ViewExt.validateEditText
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.android.material.progressindicator.BaseProgressIndicator.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLoginFragment(private val userViewModel: UserViewModel) : Fragment() {

    private lateinit var binding: FragmentUserLoginBinding

    private var isLoadingUser = false


    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController = findNavController()
                val startDestination = navController.graph.startDestinationId
                val navOptions = NavOptions.Builder().setPopUpTo(startDestination, true).build()
                navController.navigate(startDestination, null, navOptions)
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLoginProcess()
        setupProgressBar()
        setupUserLoginBtnListener()
        setupRegisterBtnListener()
    }


    private fun setupProgressBar() {
        binding.userLoginProgression.showAnimationBehavior = SHOW_OUTWARD
        binding.userLoginProgression.hideAnimationBehavior = HIDE_INWARD
        hideProgressBar()
    }

    private fun setupUserLoginBtnListener() {
        binding.userLoginBtn.setOnClickListener {
            val usernameInput = binding.userLoginUsernameText.text.toString()
            val passwordInput = binding.userLoginPasswordText.text.toString()
            if (validateLoginCredentials()) startLoginProcess(usernameInput, passwordInput)
        }
    }

    private fun setupRegisterBtnListener() {
        binding.userLoginRegisterBtn.setOnClickListener {
            navigateToRegisterFragment()
        }
    }


    private fun showProgressBar() {
        binding.userLoginProgression.show()
    }

    private fun hideProgressBar() {
        binding.userLoginProgression.hide()
    }


    private fun observeLoginProcess() {
        userViewModel.loggedUser.observe(viewLifecycleOwner) { user ->
            hideProgressBar()
            showTextEditor(binding.userLoginUsername, binding.userLoginPassword)

            if (isLoadingUser) {
                if (user == null) {
                    showSnackbar(binding.root, getString(R.string.user_error_not_found))
                } else {
                    showSnackbar(binding.root, getString(R.string.user_profile_welcome_message, user.username))
                    findNavController().popBackStack()
                }
                isLoadingUser = false
            }
        }
    }

    private fun validateLoginCredentials(): Boolean {
        val isValid1 = validateEditText(binding.userLoginUsername, binding.userLoginUsernameText, getString(R.string.user_error_no_username))
        val isValid2 = validateEditText(binding.userLoginPassword, binding.userLoginPasswordText, getString(R.string.user_error_no_password))
        return isValid1 && isValid2
    }

    private fun startLoginProcess(usernameInput: String, passwordInput: String) {
        isLoadingUser = true
        hideTextEditor(binding.userLoginUsername, binding.userLoginPassword)
        showProgressBar()

        userViewModel.login(usernameInput, passwordInput)
    }


    private fun navigateToRegisterFragment() {
        val toDestination = UserLoginFragmentDirections.actionUserLoginFragmentToUserRegisterFragment()
        findNavController().navigate(toDestination)
    }
}