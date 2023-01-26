package com.hoang.wastenot.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoang.wastenot.R
import com.hoang.wastenot.adapters.RecipeAdapter
import com.hoang.wastenot.databinding.FragmentRecipesBinding
import com.hoang.wastenot.models.Food
import com.hoang.wastenot.viewmodels.RecipeViewModel


class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private lateinit var binding: FragmentRecipesBinding
    private val viewModel by viewModels<RecipeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = FragmentRecipesBinding.bind(view)
        var noDiet = " "
        binding.loadingAnimation.visibility = View.VISIBLE
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            binding.loadingAnimation.visibility = View.GONE
        }, 500)


        this.arguments?.getParcelable<Food>("Food").apply {
            if (this != null) {
                viewModel.getRecipes(this.category, noDiet)
                binding.tvTitleRecipes.text = "Recipes with " + this.category
            } else {
                viewModel.getRecipes("chicken", noDiet)

            }

        }





        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = RecipeAdapter().apply {
                onItemClicked = {
//                it.id?.let { it1 -> viewModel.getRecipeUrl(it1) }
                    val bundle = Bundle()
                    it.id.let { it1 ->
                        if (it1 != null) {
                            bundle.putInt("RecipeId", it1)
                        }
                    }
                    Navigation.findNavController(view)
                        .navigate(R.id.action_recipesFragment_to_recipeDetailFragment, bundle)
                }

            }


        }



        viewModel.recipes.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as RecipeAdapter).dataSet = it.results
            if (it.results.isEmpty()){
                binding.recyclerView.visibility = View.GONE
                binding.emptyView.visibility = View.VISIBLE
            }
            else{
                binding.recyclerView.visibility = View.VISIBLE
                binding.emptyView.visibility = View.GONE
            }

        }



        this.arguments?.getParcelable<Food>("Food").apply {
            binding.btnDietNone.setOnClickListener {
                this?.let { viewModel.getRecipes(it.category, "") }

            }
            binding.btnDietVegan.setOnClickListener {
                this?.let { viewModel.getRecipes(it.category, "vegan") }
            }
            binding.btnDietVegetarian.setOnClickListener {
                this?.let { viewModel.getRecipes(it.category, "vegetarian") }
            }
            binding.btnDietGluten.setOnClickListener {
                this?.let { viewModel.getRecipes(it.category, "gluten free") }
            }
            binding.btnDietPescatarian.setOnClickListener {
                this?.let { viewModel.getRecipes(it.category, "pescetarian") }
            }
            binding.btnDietKeto.setOnClickListener {
                this?.let { viewModel.getRecipes(it.category, "ketogenic") }

            }
            binding.btnDietFodmap.setOnClickListener {
                this?.let { viewModel.getRecipes(it.category, "low fodmap") }
            }


        }


        binding.btnHomeRecipesfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }

        setStatusBarAppearance()
    }

    private fun setStatusBarAppearance() {
        val window = activity?.window
        window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.statusBarColor = resources.getColor(R.color.green_2)
    }

}
