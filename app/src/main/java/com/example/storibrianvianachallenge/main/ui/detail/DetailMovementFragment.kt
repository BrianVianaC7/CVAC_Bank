package com.example.storibrianvianachallenge.main.ui.detail

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.common.ui.dialog.OnFailureDialog
import com.example.storibrianvianachallenge.common.utils.ObjectUtlis.formatAsCurrency
import com.example.storibrianvianachallenge.common.utils.ObjectUtlis.formatTimestampToDashesDate
import com.example.storibrianvianachallenge.common.utils.ObjectUtlis.formatTimestampToLongDate
import com.example.storibrianvianachallenge.databinding.FragmentDetailMovementBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

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
            pbar.isVisible = false
            tvMonto.text = (detailMovementState.montoTotal ?: 0.0).formatAsCurrency()
            tvFechaHeader.text = formatTimestampToDashesDate(detailMovementState.fecha!!)
            tvTransaction.text = detailMovementState.idTransaccion
            tvImporte.text = (detailMovementState.importe ?: 0.0).formatAsCurrency()
            tvIva.text = (detailMovementState.iva ?: 0.0).formatAsCurrency()
            tvMontoTotal.text = tvMonto.text
            tvReferencia.text = detailMovementState.referencia
            tvDate.text = formatTimestampToLongDate(detailMovementState.fecha!!)
            tvType.text = detailMovementState.tipo
            tvDetail.text = detailMovementState.detalle

            lnShare.setOnClickListener {
                requestStoragePermission()
            }
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

    @Suppress("DEPRECATION")
    private fun requestStoragePermission() {
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
        val requestCode = 100

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            shareMovement()
        } else {
            Toast.makeText(
                requireContext(),
                "Permiso de escritura en almacenamiento externo denegado",
                Toast.LENGTH_SHORT
            ).show()
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

    private fun shareMovement() {
        val bitmap = Bitmap.createBitmap(binding.root.width, binding.root.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        binding.lnShare.isVisible = false
        binding.root.draw(canvas)

        val uri = getImageUri(requireContext(), bitmap)
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/png"
        }
        val chooser = Intent.createChooser(shareIntent, "Compartir imagen")
        startActivity(chooser)
    }

    @Suppress("DEPRECATION")
    private fun getImageUri(context: Context, bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
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