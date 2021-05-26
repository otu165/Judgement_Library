package com.example.judgement.feature.navigation.scrap

import android.content.Context
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.ScrapRvData

class ScrapRvAdapter(
    private val context: Context
) : RecyclerView.Adapter<ScrapRvAdapter.ScrapRvVH>() {

    private var data = listOf<ScrapRvData>()
    private var itemViewType: Int = 0

    inner class ScrapRvVH(val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txt_scrap_title)
        private val description: TextView = view.findViewById(R.id.txt_scrap_description)
        private val date: TextView = view.findViewById(R.id.txt_scrap_date)
        private val toggleButton: ToggleButton = view.findViewById(R.id.toggle_scrap_item_delete)

        fun bind(data: ScrapRvData) {
            title.text = data.title
            description.text = data.description
            date.text = data.date
            if (toggleButton.isChecked) toggleButton.toggle()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapRvVH {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_scrap_item, parent, false)

        if (itemViewType == GONE_TYPE) {
            val toggle = view.findViewById<ToggleButton>(R.id.toggle_scrap_item_delete)
            toggle.visibility = View.GONE
        } else {
            val toggle = view.findViewById<ToggleButton>(R.id.toggle_scrap_item_delete)
            toggle.visibility = View.VISIBLE
        }
        return ScrapRvVH(view)
    }

    override fun onBindViewHolder(holder: ScrapRvVH, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = itemViewType

    fun updateData(latestData: List<ScrapRvData>) {
        data = latestData
        notifyDataSetChanged()
    }

    fun setItemViewType(viewType: Int) {
        itemViewType = viewType
        notifyDataSetChanged()
    }

    companion object {
        const val GONE_TYPE = 0
        const val VISIBLE_TYPE = 1
    }
}