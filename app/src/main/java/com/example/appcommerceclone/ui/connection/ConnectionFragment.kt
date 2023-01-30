package com.example.appcommerceclone.ui.connection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.databinding.FragmentConnectionBinding
import com.example.appcommerceclone.ui.MainActivity
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel

class ConnectionFragment : Fragment() {

    private lateinit var binding: FragmentConnectionBinding
    private val connectivityViewModel by activityViewModels<ConnectivityViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hide()

        connectivityViewModel.isConnected.observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) findNavController().navigateUp()
        }
    }

    override fun onDetach() {
        super.onDetach()
        show()
    }

    private fun show() {
        val mainActivity = (requireActivity() as MainActivity)
        mainActivity.supportActionBar?.show()
        mainActivity.findViewById<DrawerLayout>(R.id.drawer_layout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun hide() {
        val mainActivity = (requireActivity() as MainActivity)
        mainActivity.supportActionBar?.hide()
        mainActivity.findViewById<DrawerLayout>(R.id.drawer_layout)?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }
}