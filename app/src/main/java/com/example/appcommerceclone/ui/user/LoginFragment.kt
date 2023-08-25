package com.example.appcommerceclone.ui.user

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import com.example.appcommerceclone.R
import com.example.appcommerceclone.data.user.model.User
import com.example.appcommerceclone.databinding.FragmentLoginBinding
import com.example.appcommerceclone.util.LoadingState
import com.example.appcommerceclone.util.addOnTextChangedListener
import com.example.appcommerceclone.util.navigateToProductsFragment
import com.example.appcommerceclone.util.onBackPressedReturnToProductsFragment
import com.example.appcommerceclone.util.showSnackbar
import com.example.appcommerceclone.util.validate
import com.google.android.material.progressindicator.BaseProgressIndicator.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment(private val userViewModel: UserViewModel) : Fragment(), AppBarConfiguration.OnNavigateUpListener {

    private lateinit var binding: FragmentLoginBinding

    private var loadingState: LoadingState = LoadingState.NOT_STARTED
    private var user: User? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onBackPressedReturnToProductsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginProgression.showAnimationBehavior = SHOW_OUTWARD
        binding.loginProgression.hideAnimationBehavior = HIDE_INWARD

        userViewModel.resetLoadingState()

        userViewModel.loadingState.distinctUntilChanged().observe(viewLifecycleOwner) { _loadingState ->
            loadingState = _loadingState
            when (loadingState) {
                LoadingState.NOT_STARTED -> hideProgressBar()
                LoadingState.LOADING -> showProgressBar()
                LoadingState.FAILURE -> {
                    hideProgressBar()
                    showSnackbar(binding.root, getString(R.string.login_failure_message))
                }

                LoadingState.SUCCESS -> {
                    hideProgressBar()
                    showSnackbar(binding.root, getString(R.string.profile_welcome_message, user!!.username))
                    findNavController().popBackStack()
                }
            }
        }

        userViewModel.currentUser.distinctUntilChanged().observe(viewLifecycleOwner) { user = it }

        binding.loginUsername.addOnTextChangedListener()
        binding.loginPassword.addOnTextChangedListener()
        binding.loginBtn.setOnClickListener {
            val isUsernameValid = binding.loginUsername.validate(getString(R.string.error_no_username))
            val isPasswordValid = binding.loginPassword.validate(getString(R.string.error_no_password))
            if (isUsernameValid && isPasswordValid) {
                userViewModel.login(
                    username = binding.loginUsernameText.text.toString(),
                    password = binding.loginPasswordText.text.toString()
                )
            }
        }

        binding.loginRegisterBtn.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }
    }

    override fun onNavigateUp(): Boolean {
        return navigateToProductsFragment()
    }

    private fun showProgressBar() {
        binding.loginProgression.show()
    }

    private fun hideProgressBar() {
        binding.loginProgression.hide()
    }
}