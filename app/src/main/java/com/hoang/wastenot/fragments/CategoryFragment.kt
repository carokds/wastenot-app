package com.hoang.wastenot.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hoang.wastenot.R
import com.hoang.wastenot.repositories.CSVReader
import java.io.IOException


class CategoryFragment : Fragment(R.layout.fragment_categories) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readIngredients()

    }

    private fun readIngredients() {
        var rows = mutableListOf<Array<String>>()
        val csvReader = CSVReader(requireContext(), "top_1k_ingredients")
        try {
            rows = csvReader.readCSV()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        rows.size
    }
}