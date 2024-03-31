package com.example.storibrianvianachallenge.main.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.storibrianvianachallenge.common.utils.ObjectUtlis.formatAsCurrency
import com.example.storibrianvianachallenge.common.utils.ObjectUtlis.formatDateTimeTo12Hour
import com.example.storibrianvianachallenge.common.utils.ObjectUtlis.formatTimestampToLongDate
import com.example.storibrianvianachallenge.common.utils.ObjectUtlis.maskAsteriskNumber
import com.example.storibrianvianachallenge.databinding.ItemMovementsBinding
import com.example.storibrianvianachallenge.main.domain.model.TransactionModel


class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemMovementsBinding.bind(view)

    fun render(movement: TransactionModel, onItemsSelected: (TransactionModel) -> Unit) {
        binding.apply {
            tvDate.text = formatTimestampToLongDate(movement.fecha!!)
            tvHour.text = formatDateTimeTo12Hour(movement.fechaHora!!)
            tvTitle.text = movement.detalle
            tvAmount.text = movement.montoTotal?.formatAsCurrency()
            tvSubtitle.text = movement.referencia?.maskAsteriskNumber()
            btNext.setOnClickListener { onItemsSelected(movement) }
        }
    }
}