package com.example.storibrianvianachallenge.main.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.databinding.FragmentDetailMovementBinding
import com.example.storibrianvianachallenge.databinding.FragmentHomeBinding
import com.example.storibrianvianachallenge.main.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMovementFragment : Fragment() {

    private val detailMovementViewModel by viewModels<DetailMovementViewModel>()
    private var _binding: FragmentDetailMovementBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initUIState()
    }

    private fun initUIState() {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovementBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}