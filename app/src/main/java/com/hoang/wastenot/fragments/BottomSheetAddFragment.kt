package com.hoang.wastenot.fragments

import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentBottomSheetAddBinding
import com.hoang.wastenot.viewmodels.BarcodeSharedViewModel
import com.squareup.picasso.Picasso


class BottomSheetAddFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetAddBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val viewModel: BarcodeSharedViewModel by activityViewModels()

    /*
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        //inflating layout
        val view = View.inflate(context, R.layout.fragment_bottom_sheet_add, null)

        //binding views to data binding.
        binding = FragmentBottomSheetAddBinding.bind(view)

        //setting layout with bottom sheet
        bottomSheet.setContentView(view)

        bottomSheetBehavior = BottomSheetBehavior.from(view.parent as View)

        bottomSheetBehavior.isDraggable = true

        //setting Peek at the 16:9 ratio keyline of its parent.
        //bottomSheetBehavior.peekHeight = BottomSheetBehavior.PEEK_HEIGHT_AUTO
        bottomSheetBehavior.peekHeight = 500

        //setting max height of bottom sheet
        binding.extraSpace.minimumHeight = Resources.getSystem().displayMetrics.heightPixels

        return bottomSheet
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
    */

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
        binding.extraSpace.minimumHeight = Resources.getSystem().displayMetrics.heightPixels
        viewModel.product.observe(viewLifecycleOwner) {
            val product = it.product
            Picasso.get().load(product.imageFrontUrl).into(binding.ivProduct)
            if (product.genericName != null && product.genericName != "") {
                binding.tvProductName.text = product.genericName
            } else {
                binding.tvProductName.text = product.productName
            }
            binding.tvProductBrand.text = product.brands
        }
    }


}