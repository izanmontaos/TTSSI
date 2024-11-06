package com.grovylessj.wifiscanner.ui.Analisis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.grovylessj.wifiscanner.databinding.FragmentAnalisisBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalisisFragment : Fragment() {

    private var _binding: FragmentAnalisisBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AnalisisViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalisisBinding.inflate(inflater, container, false)

        viewModel.networkDetails.observe(viewLifecycleOwner) { details ->
            details?.let {
                binding.ssidText.text = "SSID: ${it.ssid}"
                binding.ipText.text = "IP Address: ${it.ipAddress}"
                binding.signalText.text = "Signal Strength: ${it.signalStrength}%"
            } ?: run {
                binding.ssidText.text = "No network connected"
            }
        }

        viewModel.loadNetworkDetails()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
