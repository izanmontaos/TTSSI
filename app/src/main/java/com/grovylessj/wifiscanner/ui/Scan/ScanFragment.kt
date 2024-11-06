package com.grovylessj.wifiscanner.ui.Scan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.grovylessj.wifiscanner.databinding.FragmentScanBinding
import com.grovylessj.wifiscanner.util.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanFragment : Fragment() {

    private val scanViewModel by viewModels<ScanViewModel>()
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)

        // Configuración del adaptador para ListView
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        binding.listNetworks.adapter = adapter

        // Configura el botón para iniciar el escaneo
        binding.btnScanNetworks.setOnClickListener {
            if (PermissionUtils.hasLocationPermission(requireActivity())) {
                scanViewModel.startNetworkScan()
            } else {
                PermissionUtils.requestLocationPermission(requireActivity())
            }
        }

        // Observa los resultados de las redes disponibles y actualiza la UI
        scanViewModel.availableNetworks.observe(viewLifecycleOwner) { networks ->
            adapter.clear()
            adapter.addAll(networks)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
