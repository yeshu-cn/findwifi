package work.yeshu.findwifi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_wifi.view.*
import work.yeshu.findwifi.db.TargetWifi

class TargetWifiAdapter : RecyclerView.Adapter<TargetWifiAdapter.ViewHolder>() {
    var data: List<TargetWifi> = emptyList()

    fun updateData(newData: List<TargetWifi>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_target_wifi, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(item: TargetWifi) {
            itemView.apply {
                tv_mac.text = item.mac
            }
        }
    }
}