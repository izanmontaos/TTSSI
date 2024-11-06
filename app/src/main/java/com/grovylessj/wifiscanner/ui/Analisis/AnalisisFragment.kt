package com.grovylessj.wifiscanner.ui.Analisis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grovylessj.wifiscanner.R
import com.grovylessj.wifiscanner.databinding.FragmentAnalisisBinding
import com.grovylessj.wifiscanner.databinding.FragmentScanBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalisisFragment : Fragment() {

    private var _binding: FragmentAnalisisBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalisisBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


}