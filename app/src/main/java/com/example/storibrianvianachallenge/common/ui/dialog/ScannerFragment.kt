package com.example.storibrianvianachallenge.common.ui.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.databinding.FragmentOnSuccessBinding
import com.example.storibrianvianachallenge.databinding.FragmentScannerBinding
import com.example.storibrianvianachallenge.main.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@AndroidEntryPoint
class ScannerFragment(private val type: String, private val isFront: Boolean) : DialogFragment() {

    companion object {
        private const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var isFlashOn = false
    private lateinit var camera: Camera
    private var imageCapture: ImageCapture? = null
    private val viewModel by viewModels<LoginViewModel>()
    private var _binding: FragmentScannerBinding? = null
    private val binding get() = _binding!!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        initUI()
        sharedPreferences = requireContext().getSharedPreferences("nombre_pref", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    private fun initUI() {
        if (checkCameraPermission()) {
            startCamera()
            initListener()
        } else {
            requestPermissionLauncher.launch(CAMERA_PERMISSION)
        }
    }

    private fun initListener() {
        binding.ivCamareRound.setOnClickListener { takePhoto() }
        binding.ivCamareFlash.setOnClickListener { toggleFlash() }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        val photoFile = File.createTempFile(
            "photo",
            ".jpg",
            requireContext().externalCacheDir
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    showPhotoPreview(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("Camera", "Error al tomar foto: ${exception.message}", exception)
                }
            }
        )
    }

    private fun showPhotoPreview(photoFile: File) {
        binding.viewScanner.visibility = View.GONE
        binding.ivPreview.visibility = View.VISIBLE
        binding.ivCamareRound.visibility = View.GONE
        binding.ivCamareAccept.visibility = View.VISIBLE
        binding.ivCamareReject.visibility = View.VISIBLE


        binding.ivPreview.setImageURI(photoFile.toUri())

        binding.ivPreview.alpha = 0f
        binding.ivPreview.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(null)

        binding.ivCamareAccept.setOnClickListener {
            lifecycleScope.launch {
                viewModel.savePhoto(photoFile.toUri(), isFront )
            }
        }

        viewModel.downloadUrlFront.observe(viewLifecycleOwner) { downloadUrl ->
            if (!downloadUrl.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Foto frotal guardada en Firebase Storage", Toast.LENGTH_SHORT).show()
                editor.putString("claveF", downloadUrl)
                editor.apply()
            } else {
                Toast.makeText(requireContext(), "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }

        viewModel.downloadUrlBack.observe(viewLifecycleOwner) { downloadUrl ->
            if (!downloadUrl.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "Foto trasera guardada en Firebase Storage", Toast.LENGTH_SHORT).show()
                editor.putString("claveB", downloadUrl)
                editor.apply()
            } else {
                Toast.makeText(requireContext(), "Error al guardar la foto", Toast.LENGTH_SHORT).show()
            }
            dismiss()
        }

        binding.ivCamareReject.setOnClickListener {
            restartCamera()
        }
    }

    private fun toggleFlash() {
        isFlashOn = !isFlashOn

        if (isFlashOn) {
            binding.ivCamareFlash.setImageResource(R.drawable.ic_flash_off)
            camera.cameraControl.enableTorch(true)
        } else {
            binding.ivCamareFlash.setImageResource(R.drawable.ic_flash_active)
            camera.cameraControl.enableTorch(false)
        }
    }

    private fun restartCamera() {
        binding.viewScanner.visibility = View.VISIBLE
        binding.ivCamareRound.visibility = View.VISIBLE
        binding.ivPreview.visibility = View.GONE
        binding.ivCamareAccept.visibility = View.GONE
        binding.ivCamareReject.visibility = View.GONE
        startCamera()
    }

    private fun startCamera() {
        binding.tvMessage.text = type
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewScanner.surfaceProvider)
                }
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Log.e("Camera", "Error ${e.message}")
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startCamera()
        } else {
            Toast.makeText(
                requireContext(),
                "Acepta los permisos para entrar a esta experiencia",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkCameraPermission(): Boolean {
        return PermissionChecker.checkSelfPermission(
            requireContext(),
            CAMERA_PERMISSION
        ) == PermissionChecker.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannerBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}