package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hoang.wastenot.R
import com.hoang.wastenot.adapters.CategoriesAdapter
import com.hoang.wastenot.databinding.FragmentCategoriesBinding
import com.hoang.wastenot.repositories.CSVReader
import java.io.IOException


class CategoryFragment : Fragment(R.layout.fragment_categories) {
    private lateinit var binding: FragmentCategoriesBinding

    private val categoriesAdapter: CategoriesAdapter
        get() = binding.rvFoodsCategories.adapter as CategoriesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentCategoriesBinding.bind(view)

        binding.rvFoodsCategories.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            adapter = CategoriesAdapter().apply {
                onItemClicked = {
                    val bundle = Bundle()
                    bundle.putString("Category", it)
                    findNavController().navigate(R.id.addFragment, bundle)
                }
            }
        }

        readIngredients()

    }

    private fun readIngredients() {
        var rows = mutableListOf<String>()
        val csvReader = CSVReader(requireContext(), "top_1k_ingredients")
        try {
            rows = csvReader.readCSV()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        rows.size
        categoriesAdapter.setData(rows)
    }
}