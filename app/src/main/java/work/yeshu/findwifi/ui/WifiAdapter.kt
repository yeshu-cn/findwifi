package work.yeshu.findwifi.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_wifi.view.tv_mac
import kotlinx.android.synthetic.main.item_wifi.view.tv_name
import kotlinx.android.synthetic.main.item_wifi.view.tv_rssi
import work.yeshu.findwifi.R

class WifiAdapter : RecyclerView.Adapter<WifiAdapter.ViewHolder>() {
    var data: List<WifiItem> = emptyList()

    fun updateData(newData: List<WifiItem>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wifi, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
        onItemClickListener?.let {
            it.onItemClick(data[position], position)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: WifiItem) {
            itemView.apply {
                tv_name.text = item.name
                tv_mac.text = item.mac
                tv_rssi.text = item.level.toString()
                if (item.target) {
                    tv_mac.setTextColor(Color.RED)
                } else {
                    tv_mac.setTextColor(Color.BLACK)
                }
            }
        }
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: WifiItem, position: Int)
    }
}