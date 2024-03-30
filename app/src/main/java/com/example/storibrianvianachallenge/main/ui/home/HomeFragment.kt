package com.example.storibrianvianachallenge.main.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.databinding.FragmentHomeBinding
import com.example.storibrianvianachallenge.databinding.FragmentLoginBinding
import com.example.storibrianvianachallenge.main.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel by viewModels<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        Toast.makeText(context, "hola", Toast.LENGTH_SHORT).show()
        //initUIState()
        //initRecyclerView()
        //initSwipeRefresh()
    }

    private fun initUIState() {
        TODO("Not yet implemented")
    }

    private fun initRecyclerView() {
        TODO("Not yet implemented")
    }

    private fun initSwipeRefresh() {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
