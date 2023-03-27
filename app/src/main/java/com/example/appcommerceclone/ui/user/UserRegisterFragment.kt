package com.example.appcommerceclone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentUserRegisterBinding
import com.example.appcommerceclone.util.ViewExt
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserRegisterFragment : Fragment() {

    private lateinit var binding: FragmentUserRegisterBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRegisterBtn()
    }


    private fun setupRegisterBtn() {
        binding.userRegisterBtn.setOnClickListener {
            if (validateRegister()) startRegisterProcess()
        }
    }

    private fun validateRegister(): Boolean {
        val isValid1 = ViewExt.validateEditText(binding.userRegisterEmail, binding.userRegisterEmailText, getString(R.string.user_error_no_email))
        val isValid2 = ViewExt.validateEditText(binding.userRegisterUsername, binding.userRegisterUsernameText, getString(R.string.user_error_no_username))
        val isValid3 = ViewExt.validateEditText(binding.userRegisterPassword, binding.userRegisterPasswordText, getString(R.string.user_error_no_password))
        return isValid1 && isValid2 && isValid3
    }

    private fun startRegisterProcess() {
        ViewExt.showSnackbar(binding.root, getString(R.string.user_register_successfully_message))
        findNavController().navigateUp()
    }
}