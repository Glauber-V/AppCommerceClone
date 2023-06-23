package com.example.appcommerceclone.ui.connection

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.appcommerceclone.R
import com.example.appcommerceclone.viewmodels.ConnectivityViewModel

class ConnectionFragment(private val connectivityViewModel: ConnectivityViewModel) : Fragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            connectivityViewModel.isConnected.observe(viewLifecycleOwner) { hasConnection ->
                if (!hasConnection) {
                    setContent {
                        ConnectionScreen()
                    }
                } else {
                    findNavController().popBackStack()
                }
            }
        }
    }
}

@Composable
fun ConnectionScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.ic_baseline_wifi_off_24),
            contentDescription = stringResource(id = R.string.content_desc_no_internet_connection_image)
        )
        Text(
            text = stringResource(id = R.string.connection_lost),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewConnectionScreen() {
    MaterialTheme {
        ConnectionScreen()
    }
}