package com.example.storibrianvianachallenge.main.ui.home.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.storibrianvianachallenge.R
import com.example.storibrianvianachallenge.main.domain.model.TransactionModel

class HomeAdapter(
    private var movementList: List<TransactionModel> = emptyList(),
    private val onItemClicked: (TransactionModel) -> Unit
) : RecyclerView.Adapter<HomeViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(list: List<TransactionModel>) {
        movementList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_movements, parent, false)
        )
    }

    override fun getItemCount(): Int = movementList.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.render(movementList[position], onItemClicked)
    }

}