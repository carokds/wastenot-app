package com.hoang.wastenot.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentBarcodeScannerBinding
import com.hoang.wastenot.repositories.BarcodeRepository
import com.hoang.wastenot.viewmodels.BarcodeSharedViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BarcodeScannerFragment : Fragment(R.layout.fragment_barcode_scanner), KoinComponent {
    private lateinit var binding: FragmentBarcodeScannerBinding
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var bottomSheetAddFragment : BottomSheetAddFragment

    private val viewModel: BarcodeSharedViewModel by activityViewModels()

    private lateinit var test: String

    val repository: BarcodeRepository by inject()

    var code: String? = null




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBarcodeScannerBinding.bind(view)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            activity?.requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        viewModel.showBottomSheetDialog.observe(viewLifecycleOwner){
            if (it == true) {
                displayFragment()
            } else {
                code = null
            }
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS).build()

        val barcodeScanner: BarcodeScanner = BarcodeScanning.getClient(options)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            cameraProviderFuture.addListener({
                // Used to bind the lifecycle of cameras to the lifecycle owner
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                // Preview
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                    }

                // Select back camera as a default
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                val imageAnalyzer = ImageAnalysis.Builder()
                    .build()
                    .also {
                        it.setAnalyzer(
                            cameraExecutor,
                            ImageAnalysis.Analyzer { imageProxy ->
                                processImageProxy(barcodeScanner, imageProxy)
                            }
                        )
                    }

                try {
                    // Unbind use cases before rebinding
                    cameraProvider.unbindAll()

                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalyzer
                    )



                } catch (exc: Exception) {
                    Log.e(TAG, "Use case binding failed", exc)
                }

            }, requireActivity().mainExecutor)
        }
    }

    @SuppressLint("UnsafeOptInUsageError")
    fun processImageProxy(
        barcodeScanner: BarcodeScanner,
        imageProxy: ImageProxy,

    ) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    barcodes.forEach { barcode ->
                        if (code == null) {
                            code = barcode.rawValue
                            viewModel.getInfo(barcode.rawValue)
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message ?: it.toString())
                }
                .addOnCompleteListener {
                    // When the image is from CameraX analysis use case, must call image.close() on received
                    // images when finished using them. Otherwise, new images may not be received or the camera
                    // may stall.
                    mediaImage.close()
                    imageProxy.close()
                }
        }
    }

    private fun displayFragment() {
        val bottomSheetAddFragment = BottomSheetAddFragment()
        bottomSheetAddFragment.show(parentFragmentManager, "BottomSheetDialog")
    }


    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        activity?.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}