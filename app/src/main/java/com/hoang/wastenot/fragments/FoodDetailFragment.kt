package com.hoang.wastenot.fragments

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import coil.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentFoodDetailBinding
import com.hoang.wastenot.models.Food

class FoodDetailFragment : Fragment(R.layout.fragment_food_detail){

    private lateinit var binding: FragmentFoodDetailBinding
    private var food: Food = Food()

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

        val bundle = this.arguments

        bundle?.getParcelable<Food>("Food").apply {
            Firebase.firestore.collection("foods").document(this!!.id!!)
                .addSnapshotListener { document, error ->
                    setFood(document?.toObject(Food::class.java))
                }
        }
    }

    private fun setFood(food: Food?) {
        if (food == null) {
            return
        }
        this.food = food
        binding.tvTitleDetails.text = food.name
        binding.ivTemporaryDetail.load(food.pictureUrl)

    }
}