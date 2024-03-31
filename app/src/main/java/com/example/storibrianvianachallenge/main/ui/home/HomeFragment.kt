package com.example.storibrianvianachallenge.main.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.databinding.FragmentHomeBinding
import com.example.storibrianvianachallenge.main.ui.home.adapter.HomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var pointerPosition: Float = 0f
    var canSwipe = false
    private lateinit var movementAdapter: HomeAdapter
    private val homeViewModel by viewModels<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initServices()
        initUI()
    }

    private fun initServices() {
        initLoader()
        getMovements()
        getBalance()
    }

    private fun initUI() {
        initBalanceUIState()
        initMovementsUIState()
        initRecyclerView()
        initSwipeRefresh()
        initToolbar()
    }

    private fun initBalanceUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.balance.collect { balanace ->
                    Log.e("Balance", "Balance: ${balanace.saldoMonto}")
                    val balanceAmount = "$ ${balanace.saldoMonto ?: 0.0}"
                    binding.tvSaldo.text = balanceAmount
                    updatePointerPosition(balanace.saldoMonto ?: 0.0)
                }
            }
        }
    }

    private fun updatePointerPosition(balance: Double) {
        val progressBar = binding.progressBarMenu
        val pointerLayout = binding.pointerProgress

        val progressBarWidth = progressBar.width
        val pointerWidth = pointerLayout.width

        val maxBalance = 10000.0
        val midBalance = 5000.0

        pointerPosition = when {
            balance >= maxBalance -> progressBarWidth - pointerWidth.toFloat()  // Mover al final
            balance >= midBalance -> (progressBarWidth - pointerWidth.toFloat()) / 2  // Mover al centro
            else -> 0f  // Mover al inicio
        }

        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right)
        pointerLayout.startAnimation(animation)

        pointerLayout.translationX = pointerPosition
    }

    private fun initMovementsUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.movements.collect {
                    if (it.isEmpty()) {
                        binding.isEmptyOperations.isVisible = true
                    } else {
                        binding.isEmptyOperations.isVisible = false
                        movementAdapter.updateList(it)
                    }
                }
            }
        }
    }

    private fun initRecyclerView() {
        movementAdapter = HomeAdapter(
            onItemClicked = { movement ->
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailMovementFragment(movement.idMovimiento!!)
                )
                Log.e("Movimiento", "Movimiento: ${movement.idMovimiento}")
            })
        binding.rvMovements.apply {
            layoutManager = GridLayoutManager(context, 1)
            adapter = movementAdapter
        }
    }

    private fun initSwipeRefresh() {
        binding.apply {
            initScrollActionSwipe()
            swipeRefresh.setColorSchemeResources(R.color.purple)
            swipeRefresh.setOnRefreshListener {
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!canSwipe) {
                        Log.e("swipeRefresh", "Llamada al servicio")
                        getBalance()
                        swipeRefresh.isRefreshing = false
                        canSwipe = true
                    } else {
                        Log.e("swipeRefresh", "sin Llamada al servicio")
                        swipeRefresh.isRefreshing = false
                    }
                }, 1000)
            }
        }
        // Configura un temporizador para habilitar el swipe cada 30 segundos
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                canSwipe = false
            }
        }, 30000, 30000)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initScrollActionSwipe() {
        var initialY = 0f
        val scale = 0.4f

        binding.swipeRefresh.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    initialY = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val deltaY = (motionEvent.y - initialY) * scale
                    Log.e("valorInicial", "$initialY")
                    if (deltaY > 0) {
                        binding.rvMovements.translationY += deltaY
                        binding.isEmptyOperations.translationY += deltaY
                    }
                    initialY = motionEvent.y
                }

                MotionEvent.ACTION_UP -> {
                    binding.rvMovements.translationY = 0f
                    binding.isEmptyOperations.translationY = 0f
                }
            }
            false
        }
    }

    private fun getBalance() {
        parentFragmentManager.let { homeViewModel.getBalance("8tw0Yz4F5OZounvCqZG4d0UPtjl2", it) }
        homeViewModel.balance.value.saldoMonto?.let { updatePointerPosition(it) }

    }

    private fun getMovements() {
        parentFragmentManager.let { homeViewModel.getMovements("lcbsP9GoLBfryFrVTIYYaXfnrS03", it) }
    }

    private fun initLoader() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.isLoading.collect { loading ->
                    if (loading) {
                        binding.pbar.isVisible = true
                    } else {
                        binding.pbar.isVisible = false
                    }
                }
            }
        }
    }

    private fun initToolbar() {
        (activity as AppCompatActivity).supportActionBar?.hide()
        val toolbar = activity?.findViewById<ImageView>(R.id.ivLogout)
        toolbar?.visibility = View.VISIBLE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as AppCompatActivity).supportActionBar?.hide()
        val toolbar = activity?.findViewById<ImageView>(R.id.ivLogout)
        toolbar?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
