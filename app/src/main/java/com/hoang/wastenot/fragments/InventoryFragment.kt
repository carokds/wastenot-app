package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentInventoryBinding

class InventoryFragment : Fragment(R.layout.fragment_inventory) {

    private lateinit var binding: FragmentInventoryBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentInventoryBinding.bind(view)

        binding.btnAdd.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_inventoryFragment_to_addFragment)
        }

        binding.ivTemporary.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_inventoryFragment_to_foodDetailFragment)
        }
    }
}