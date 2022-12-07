package com.example.appcommerceclone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentUserLoginBinding
import com.example.appcommerceclone.ui.BaseNavigation.navigateToRegisterFragment
import com.example.appcommerceclone.ui.BaseUser.verifyUserConnectionToProceed
import com.example.appcommerceclone.util.ViewExt
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import com.google.android.material.progressindicator.BaseProgressIndicator.HIDE_INWARD
import com.google.android.material.progressindicator.BaseProgressIndicator.SHOW_OUTWARD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserLoginFragment : Fragment() {

    private lateinit var binding: FragmentUserLoginBinding
    val connectivityViewModel by activityViewModels<ConnectivityViewModel>()
    val userViewModel by activityViewModels<UserViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUserConnectionToProceed(connectivityViewModel) {
            setupProgressBar()
            setupUserLoginBtn()
            setupRegisterBtn()
        }
    }


    private fun setupProgressBar() {
        binding.userLoginProgression.showAnimationBehavior = SHOW_OUTWARD
        binding.userLoginProgression.hideAnimationBehavior = HIDE_INWARD
        hideProgressBar()
    }

    private fun setupUserLoginBtn() {
        binding.userLoginBtn.setOnClickListener {
            val usernameInput = binding.userLoginUsernameText.text.toString()
            val passwordInput = binding.userLoginPasswordText.text.toString()
            if (validateLogin()) startLoginProcess(usernameInput, passwordInput)
        }
        startObserveLoginProcess()
    }

    private fun setupRegisterBtn() {
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


    private fun validateLogin(): Boolean {
        val isValid1 = ViewExt.prepareEditText(binding.userLoginUsername, binding.userLoginUsernameText, getString(R.string.user_error_no_username))
        val isValid2 = ViewExt.prepareEditText(binding.userLoginPassword, binding.userLoginPasswordText, getString(R.string.user_error_no_password))
        return isValid1 && isValid2
    }

    private fun startLoginProcess(usernameInput: String, passwordInput: String) {
        ViewExt.hideTextEditor(binding.userLoginUsername, binding.userLoginPassword)
        showProgressBar()

        userViewModel.login(usernameInput, passwordInput)
    }

    private fun startObserveLoginProcess() {
        userViewModel.loggedUser.observe(viewLifecycleOwner) { user ->
            hideProgressBar()
            ViewExt.showTextEditor(binding.userLoginUsername, binding.userLoginPassword)

            if (user == null) ViewExt.showMessage(binding.root, getString(R.string.user_error_not_found))
            else findNavController().navigateUp()
        }
    }
}