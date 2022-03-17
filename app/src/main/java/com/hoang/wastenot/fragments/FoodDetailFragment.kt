package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentFoodDetailBinding

class FoodDetailFragment : Fragment(R.layout.fragment_food_detail){

    private lateinit var binding: FragmentFoodDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFoodDetailBinding.bind(view)

        binding.btnUseMe.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_foodDetailFragment_to_recipesFragment)
        }

        binding.btnHomeFooddetailfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }
    }
}