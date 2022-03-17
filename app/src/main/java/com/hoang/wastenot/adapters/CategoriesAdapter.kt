package com.hoang.wastenot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hoang.wastenot.R
import com.hoang.wastenot.models.Food

class CategoriesAdapter() : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    var onItemClicked: (String) -> Unit = { }

    private var dataSet = mutableListOf<String>()

    fun setData(dataParam: List<String>) {
        dataSet.clear()
        dataSet.addAll(dataParam)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentData: String = dataSet[position]
        holder.foodCategory.text = currentData
        holder.itemView.setOnClickListener { onItemClicked(currentData) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var foodCategory: TextView = view.findViewById(R.id.tv_item_category)

    }
}