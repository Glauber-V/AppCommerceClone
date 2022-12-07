package com.example.appcommerceclone.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentUserRegisterBinding
import com.example.appcommerceclone.ui.BaseUser.verifyUserConnectionToProceed
import com.example.appcommerceclone.util.ViewExt
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel
import com.example.appcommerceclone.viewmodels.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserRegisterFragment : Fragment() {

    private lateinit var binding: FragmentUserRegisterBinding
    val connectivityViewModel by activityViewModels<ConnectivityViewModel>()
    val userViewModel by activityViewModels<UserViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        verifyUserConnectionToProceed(connectivityViewModel) {
            setupRegisterBtn()
        }
    }


    private fun setupRegisterBtn() {
        binding.userRegisterBtn.setOnClickListener {
            val emailInput = binding.userRegisterEmailText.text.toString()
            val usernameInput = binding.userRegisterUsernameText.text.toString()
            val passwordInput = binding.userRegisterPasswordText.text.toString()
            if (validateRegister()) startRegisterProcess(emailInput, usernameInput, passwordInput)
        }
    }

    private fun validateRegister(): Boolean {
        val isValid1 = ViewExt.prepareEditText(binding.userRegisterEmail, binding.userRegisterEmailText, getString(R.string.user_error_no_email))
        val isValid2 = ViewExt.prepareEditText(binding.userRegisterUsername, binding.userRegisterUsernameText, getString(R.string.user_error_no_username))
        val isValid3 = ViewExt.prepareEditText(binding.userRegisterPassword, binding.userRegisterPasswordText, getString(R.string.user_error_no_password))
        return isValid1 && isValid2 && isValid3
    }

    private fun startRegisterProcess(emailInput: String, usernameInput: String, passwordInput: String) {
        ViewExt.showMessage(binding.root, getString(R.string.user_register_successfully_message))
        findNavController().navigateUp()
    }
}