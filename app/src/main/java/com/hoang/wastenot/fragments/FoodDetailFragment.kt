package com.hoang.wastenot.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentFoodDetailBinding
import com.hoang.wastenot.models.Food

class FoodDetailFragment : Fragment(R.layout.fragment_food_detail){

    private lateinit var binding: FragmentFoodDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFoodDetailBinding.bind(view)

        val currentFood = this.arguments

        currentFood?.getParcelable<Food>("Food").apply {
        binding.btnUseMe.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Food", this)
            Navigation.findNavController(view).navigate(R.id.action_foodDetailFragment_to_recipesFragment, bundle)
        }}




        binding.btnHomeFooddetailfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }
    }
}