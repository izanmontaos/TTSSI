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

        // Iniciar el descubrimiento de hosts cuando se haga clic en el botón
        binding.btnDiscoverHosts.setOnClickListener {
            viewModel.discoverHosts()  // Llama al descubrimiento sin especificar subred
        }

        // Observar los cambios en la lista de hosts descubiertos
        viewModel.hosts.observe(viewLifecycleOwner) { hosts ->
            adapter.clear()
            adapter.addAll(hosts.map { host ->
                "IP: ${host.ipAddress}, Host: ${host.hostName ?: "Desconocido"}"
            })
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
