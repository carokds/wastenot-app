package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentBottomSheetAddBinding
import com.hoang.wastenot.viewmodels.BarcodeSharedViewModel
import com.squareup.picasso.Picasso


class BottomSheetAddFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetAddBinding
    private val viewModel: BarcodeSharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBottomSheetAddBinding.bind(view)

        viewModel.product.observe(viewLifecycleOwner) {
            val product = it.product
            Picasso.get().load(product.imageFrontUrl).into(binding.ivProduct)
            if (product.genericName != "") {
                binding.tvProductName.text = product.genericName
            } else {
                binding.tvProductName.text = product.productName
            }
            binding.tvProductBrand.text = product.brands
        }
    }
}