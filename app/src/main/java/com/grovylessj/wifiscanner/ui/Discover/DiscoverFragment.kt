package com.grovylessj.wifiscanner.ui.Discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.grovylessj.wifiscanner.databinding.FragmentDiscoverBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoverFragment : Fragment() {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DiscoverViewModel by viewModels()

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, mutableListOf())
        binding.listHosts.adapter = adapter

        binding.btnDiscoverHosts.setOnClickListener {
            val subnet = "192.168.1"  // Este es un ejemplo; podrías obtener el prefijo de tu red
            viewModel.discoverHosts(subnet)
        }

        viewModel.hosts.observe(viewLifecycleOwner) { hosts ->
            adapter.clear()
            adapter.addAll(hosts.map { "IP: ${it.ipAddress}, Host: ${it.hostName ?: "Unknown"}" })
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
