package com.example.storibrianvianachallenge.main.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.common.ui.dialog.OnFailureDialog
import com.example.storibrianvianachallenge.databinding.FragmentDetailMovementBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMovementFragment : Fragment() {

    private val args: DetailMovementFragmentArgs by navArgs()
    private val detailMovementViewModel by viewModels<DetailMovementViewModel>()
    private var _binding: FragmentDetailMovementBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailMovementViewModel.getMovement(args.movementId)
        initUI()
    }

    private fun initUI() {
        initUIState()
        initToolbar()
        initListeners()
    }

    private fun initListeners() {
        val back = activity?.findViewById<ImageView>(R.id.ivBack)
        back?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailMovementViewModel.state.collect {
                    when (it) {
                        is DetailMovementState.Error -> errorState(it.error)
                        DetailMovementState.Loading -> loadingState()
                        is DetailMovementState.SuccessMovement -> successMovementState(it)
                    }
                }
            }
        }
    }

    private fun successMovementState(detailMovementState: DetailMovementState.SuccessMovement) {

        binding.apply {
            val balanceAmount = "$ ${detailMovementState.montoTotal ?: 0.0}"
            tvMonto.text = balanceAmount
        }

    }

    private fun loadingState() {
        binding.pbar.isVisible = true
    }

    private fun errorState(message: String? = null) {
        binding.pbar.isVisible = false
        val fragmentManager = childFragmentManager
        val faiulureDialog = OnFailureDialog(
            message = message ?: "Ups, algo salio mal",
        )
        faiulureDialog.show(fragmentManager, "failure")
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val back = activity?.findViewById<ImageView>(R.id.ivBack)
        back?.visibility = View.VISIBLE
        val title = activity?.findViewById<TextView>(R.id.tvTitleMain)
        title?.text = getString(R.string.detail)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.hide()
        val back = activity?.findViewById<ImageView>(R.id.ivBack)
        back?.visibility = View.GONE
        val title = activity?.findViewById<TextView>(R.id.tvTitleMain)
        title?.text = getString(R.string.StoriApp)
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