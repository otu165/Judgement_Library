package com.example.judgement.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.example.judgement.R
import com.example.judgement.data.ScrapData
import com.example.judgement.extension.logd
import com.example.judgement.view.detail_result.DetailResultActivity
import com.example.judgement.view.main.scrap.ScrapManager

class ScrapAdapter(
    private val context: Context,
    private val scrapManager: ScrapManager
) : RecyclerView.Adapter<ScrapAdapter.ScrapViewHolder>() {

    private var data = listOf<ScrapData>()
    private var itemViewType: Int = 0
//    val toDelete = mutableListOf<Int>() // 삭제 리스트
    val toDelete = mutableMapOf<Int, String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_scrap_item, parent, false)

        if (itemViewType == GONE_TYPE) {
            val toggle = view.findViewById<ToggleButton>(R.id.toggle_scrap_item_delete)
            toggle.visibility = View.GONE
        } else {
            val toggle = view.findViewById<ToggleButton>(R.id.toggle_scrap_item_delete)
            toggle.visibility = View.VISIBLE
        }
        return ScrapViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScrapViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int = itemViewType

    fun setItemViewType(viewType: Int) {
        itemViewType = viewType
        notifyDataSetChanged()
    }

    fun updateData(latestData: List<ScrapData>) {
        data = latestData
        notifyDataSetChanged()
    }

    /* ViewHolder */
    inner class ScrapViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.txt_scrap_title)
        private val description: TextView = view.findViewById(R.id.txt_scrap_description)
        private val date: TextView = view.findViewById(R.id.txt_scrap_date)
        private val toggleButton: ToggleButton = view.findViewById(R.id.toggle_scrap_item_delete)

        fun bind(data: ScrapData, position: Int) {
            title.text = data.title
            description.text = data.description
            date.text = data.date
            if (toggleButton.isChecked) toggleButton.toggle()

            toggleButton.setOnClickListener {
                if (toggleButton.isChecked) {
                    toDelete[position] = data.serial
                } else {
                    toDelete.remove(position)
                }
            }

            // show DetailResultActivity
            view.setOnClickListener {
                val intent =
                    Intent(view.context.applicationContext, DetailResultActivity::class.java)
                        .putExtra("precId", data.serial)
                        .putExtra("title", data.title)
                        .putExtra("pos", position + 1)
                        .putExtra("description", data.description)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                view.context.startActivity(intent)
            }
        }
    }

    companion object {
        const val GONE_TYPE = 0
        const val VISIBLE_TYPE = 1
    }
}