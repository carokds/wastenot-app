package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoang.wastenot.R
import com.hoang.wastenot.adapters.RecipeAdapter
import com.hoang.wastenot.databinding.FragmentRecipesBinding
import com.hoang.wastenot.viewmodels.RecipeViewModel


class RecipesFragment : Fragment(R.layout.fragment_recipes) {

    private lateinit var binding: FragmentRecipesBinding
    private val viewModel by viewModels<RecipeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecipesBinding.bind(view)
viewModel.getRecipes()

        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = RecipeAdapter()
            }

        viewModel.recipes.observe(viewLifecycleOwner) {
            (binding.recyclerView.adapter as RecipeAdapter).dataSet = it
//            Toast.makeText(context, "${it[0].title}",Toast. LENGTH_LONG ).show()

        }



//        binding.
//            .setOnClickListener {
//            Navigation.findNavController(view).navigate(R.id.action_recipesFragment_to_recipeDetailFragment)
//        }

        binding.btnHomeRecipesfragment.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_global_inventoryFragment)
        }

    }
}
