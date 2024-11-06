package com.grovylessj.wifiscanner.ui.Scan

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
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

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        binding.listNetworks.adapter = adapter

        binding.btnScanNetworks.setOnClickListener {
            if (PermissionUtils.hasLocationPermission(requireActivity())) {
                scanViewModel.startNetworkScan()
            } else {
                PermissionUtils.requestLocationPermission(requireActivity())
            }
        }

        scanViewModel.availableNetworks.observe(viewLifecycleOwner) { networks ->
            adapter.clear()
            adapter.addAll(networks)
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtils.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scanViewModel.startNetworkScan()
            } else {
                Toast.makeText(requireContext(), "Location permission is required to scan networks.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        scanViewModel.unregisterReceiver()
    }
}
