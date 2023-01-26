package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hoang.wastenot.R
import com.hoang.wastenot.databinding.FragmentFoodDetailBinding
import com.hoang.wastenot.models.Food

class FoodDetailFragment : Fragment(R.layout.fragment_food_detail) {

    private lateinit var binding: FragmentFoodDetailBinding
    private var food: Food = Food()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFoodDetailBinding.bind(view)

        onHomeBtnClicked(view)

        retrieveFoodObject(view)

        setStatusBarAppearance()

        setFoodFactsText()
    }

    private fun setFoodFactsText() {
        val foodFacts = resources.getStringArray(R.array.facts)

        //        🔔  Expiring today💡
        binding.tvFoodFacts.text = foodFacts.random()
    }

    private fun setStatusBarAppearance() {
        // To show content behind status and navigation bar
        val window = activity?.window
        window?.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        )
    }


    private fun onHomeBtnClicked(view: View) {
        binding.btnHomeFooddetailfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }
    }

    private fun retrieveFoodObject(view: View) {
        val bundle = this.arguments

        bundle?.getParcelable<Food>("Food").apply {
            Firebase.firestore.collection("foods").document(this!!.id!!)
                .addSnapshotListener { document, error ->
                    setFood(document?.toObject(Food::class.java))
                }
            onRecipesBtnClicked(view)
            onDeleteBtnClicked(this)
            setExpirationDate(this)
            setCategory()

        }
    }

    private fun Food.setCategory() {
        binding.tvProductDetailCategory.text =
            "${this.category[0].toUpperCase()}${this.category.substring(1)}"
    }

    private fun setExpirationDate(food: Food) {
        val expDate =
            "This product is expiring on: ${food.expirationDate.toDate().date}-${food.expirationDate.toDate().month + 1}-${food.expirationDate.toDate().year - 100}"
        binding.tvProductExpDate.text = expDate
    }

    private fun onDeleteBtnClicked(food: Food) {
        binding.btnDeleteProduct.setOnClickListener {
            Firebase.firestore.collection("foods").document(food.id)
                .delete()
            findNavController().navigate(R.id.inventoryFragment)
        }
    }

    private fun Food?.onRecipesBtnClicked(view: View) {
        binding.btnUseMe.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable("Food", this)
            Navigation.findNavController(view)
                .navigate(R.id.action_foodDetailFragment_to_recipesFragment, bundle)
        }
    }

    private fun setFood(food: Food?) {
        if (food == null) {
            return
        }
        this.food = food
        binding.tvTitleDetails.text = food.name
        binding.ivFoodDetail.load(food.pictureUrl)
    }
}