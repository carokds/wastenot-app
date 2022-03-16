package com.hoang.wastenot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.button.MaterialButton
import com.hoang.wastenot.R
import com.hoang.wastenot.models.Food

class FoodInventoryAdapter() : RecyclerView.Adapter<FoodInventoryAdapter.FoodsViewHolder>() {

    var onItemClicked: (Food) -> Unit = { }
    var onDeleteBtnClicked: (Food) -> Unit = { }
    var onRecipesBtnClicked: (Food) -> Unit = { }

    private var dataSet = mutableListOf<Food>()

    fun setData(dataParam: List<Food>) {
        dataSet.clear()
        dataSet.addAll(dataParam)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodsViewHolder, position: Int) {
        val currentData: Food = dataSet[position]

        holder.foodImage.load(currentData.pictureUrl) {
            placeholder(R.drawable.placeholder_img)
        }
        holder.foodName.text = currentData.name
        holder.foodExpDate.text = currentData.expirationDate.toDate().toString()
        holder.itemView.setOnClickListener { onItemClicked(currentData) }
        holder.deleteBtn.setOnClickListener { onDeleteBtnClicked(currentData) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class FoodsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var foodImage: ImageView = view.findViewById(R.id.iv_food)
        var foodName: TextView = view.findViewById(R.id.tv_food_name)
        var foodExpDate: TextView = view.findViewById(R.id.tv_exp_date)
        var deleteBtn: MaterialButton = view.findViewById(R.id.btn_delete)
        var recipesBtn: MaterialButton = view.findViewById(R.id.btn_recipes)

    }
}