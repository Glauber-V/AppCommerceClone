package com.example.appcommerceclone.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentRegisterBinding
import com.example.appcommerceclone.util.addOnTextChangedListener
import com.example.appcommerceclone.util.showSnackbar
import com.example.appcommerceclone.util.validate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerEmail.addOnTextChangedListener()
        binding.registerUsername.addOnTextChangedListener()
        binding.registerPassword.addOnTextChangedListener()
        binding.registerBtn.setOnClickListener {
            val isEmailValid = binding.registerEmail.validate(getString(R.string.error_no_email))
            val isUsernameValid = binding.registerUsername.validate(getString(R.string.error_no_username))
            val isPasswordValid = binding.registerPassword.validate(getString(R.string.error_no_password))

            if (isEmailValid && isUsernameValid && isPasswordValid) {
                showSnackbar(binding.root, getString(R.string.register_success_message))
                findNavController().popBackStack()
            }
        }
    }
}