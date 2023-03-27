package com.example.appcommerceclone.ui.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.databinding.FragmentConnectionBinding
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel

class ConnectionFragment(private val connectivityViewModel: ConnectivityViewModel) : Fragment() {

    private lateinit var binding: FragmentConnectionBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeConnectionChanges()
    }


    private fun observeConnectionChanges() {
        connectivityViewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) findNavController().navigateUp()
        }
    }
}