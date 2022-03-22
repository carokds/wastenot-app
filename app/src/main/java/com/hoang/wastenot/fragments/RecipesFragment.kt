package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
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
        var ingredient = ""

      this.arguments?.getParcelable<Food>("Food").apply{
            if (this != null) {
                viewModel.getRecipes(this.category, noDiet)
                binding.tvTitleRecipes.text = "Recipes with " + this.category
            }else{
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
                }}
            }


        viewModel.recipes.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as RecipeAdapter).dataSet = it.results
        }

//        val diets: Array<out String> = resources.getStringArray(R.array.diet_array)
//        val textView = (binding.autocompleteDiet as AutoCompleteTextView)
//        ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, diets).also { adapter ->
//            textView.setAdapter(adapter)
//        }
//
//       textView.setText(diets[0],false)
//        // Get input text
//        val inputText = textView?.text.toString()
//
//
//        textView?.doOnTextChanged { inputText, _, _, _ ->
//            Toast.makeText(context,inputText, Toast.LENGTH_SHORT).show()
//            binding.menu.error = null
//
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
//                    diets[1] -> this?.let { viewModel.getRecipes(it.category, "gluten free") }
//                    diets[2] -> this?.let { viewModel.getRecipes(it.category, "ketogenic") }
//                    diets[3] -> this?.let { viewModel.getRecipes(it.category, "vegetarian") }
//                    diets[4] -> this?.let { viewModel.getRecipes(it.category, "vegan") }
//                    diets[5] -> this?.let { viewModel.getRecipes(it.category, "pescetarian") }
//                    diets[6] -> this?.let { viewModel.getRecipes(it.category, "low fodmap") }
//                    diets[0] -> this?.let { viewModel.getRecipes(it.category, " ") }
//                    else -> {
//                        binding.menu.error = "error"
//                    }



            }
//     }

//        binding.
//            .setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_recipesFragment_to_recipeDetailFragment)
//        }

        binding.btnHomeRecipesfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }

    }

}
