package com.mvvm.okcredit.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.okcredit.R
import com.mvvm.okcredit.databinding.ListrowFoodListlBinding
import com.mvvm.okcredit.ui.home.model.FoodClassResponse

class FoolListAdapter(private var arrayList: MutableList<FoodClassResponse.Meal>) :
    RecyclerView.Adapter<FoolListAdapter.EventDataViewHolder>() {

    lateinit var context: Context
    lateinit var select: OnSelectProductItemListMeal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventDataViewHolder {
        var inflator = LayoutInflater.from(parent.context)
        context = parent.context
        var binding = DataBindingUtil.inflate<ListrowFoodListlBinding>(
            inflator,
            R.layout.listrow_food_listl,
            parent,
            false
        )
        binding.onSelectFoodListItemOnClick = select
        return EventDataViewHolder(binding)
    }
    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: EventDataViewHolder, position: Int) {
        holder.onBindView(arrayList[position])
    }
    fun updateList(productList: ArrayList<FoodClassResponse.Meal>) {
        arrayList.clear()
        arrayList.addAll(productList)
        notifyDataSetChanged()
    }
    class EventDataViewHolder(private var binding: ListrowFoodListlBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBindView(foodResponse: FoodClassResponse.Meal) {
            binding.foodClassModelResponse = foodResponse
            binding.position = adapterPosition
        }
    }
    interface OnSelectProductItemListMeal {
        fun onSelectProductItemDataMeal(position: Int)
    }
    // method for filtering our recyclerview items.
    fun filterList(filterlist: ArrayList<FoodClassResponse.Meal>) {
        updateList(filterlist)
        notifyDataSetChanged()
    }



}