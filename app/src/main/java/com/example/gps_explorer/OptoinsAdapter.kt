package com.example.gps_explorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OptionsAdapter(
    private val items: List<OptionItem>,
    private val onChanged: () -> Unit
) : RecyclerView.Adapter<OptionsAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val root: View = view.findViewById(R.id.itemRoot)
        val icon: ImageView = view.findViewById(R.id.ivIcon)
        val label: TextView = view.findViewById(R.id.tvLabel)
        val check: ImageView = view.findViewById(R.id.ivCheck)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_option, parent, false)
        return VH(v)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconRes)
        holder.label.text = item.label
        render(holder, item.selected)

        holder.root.setOnClickListener {
            val pos = holder.bindingAdapterPosition
            if (pos == RecyclerView.NO_POSITION) return@setOnClickListener
            items[pos].selected = !items[pos].selected
            notifyItemChanged(pos)
            onChanged()
        }
    }

    private fun render(h: VH, selected: Boolean) {
        h.root.setBackgroundResource(
            if (selected) R.drawable.bg_option_selected else R.drawable.bg_option_default
        )
        h.check.setImageResource(if (selected) R.drawable.checkbox_on else R.drawable.checkbox_off)
    }
}