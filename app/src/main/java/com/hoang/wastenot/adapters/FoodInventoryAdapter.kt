package com.hoang.wastenot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hoang.wastenot.R
import com.hoang.wastenot.models.Food

class FoodInventoryAdapter() : RecyclerView.Adapter<FoodInventoryAdapter.FoodsViewHolder>() {

    var onItemClicked: (Food) -> Unit = { }

    private var dataSet = mutableListOf<Food>()

    fun setData(dataParam: List<Food>) {
        dataSet.clear()
        dataSet.addAll(dataParam.sortedBy { it.expirationDate })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food_copy, parent, false)
        return FoodsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodsViewHolder, position: Int) {
        val currentData: Food = dataSet[position]

        holder.foodImage.load(currentData.pictureUrl) {
            placeholder(R.drawable.placeholder_img)
        }
        holder.foodName.text = currentData.name

        val expDate =
            "Exp: ${currentData.expirationDate.toDate().date}/${currentData.expirationDate.toDate().month + 1}/${currentData.expirationDate.toDate().year - 100}"
        holder.foodExpDate.text = expDate
        holder.itemView.setOnClickListener { onItemClicked(currentData) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class FoodsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var foodImage: ImageView = view.findViewById(R.id.iv_food)
        var foodName: TextView = view.findViewById(R.id.tv_food_name)
        var foodExpDate: TextView = view.findViewById(R.id.tv_exp_date)
    }
}