package com.vimosanan.dataanalysersg.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vimosanan.dataanalysersg.R
import com.vimosanan.dataanalysersg.repository.models.RecordViewData
import kotlinx.android.synthetic.main.card_view_year_data.view.*

class YearDataAdapter(private var dataSet: List<RecordViewData>): RecyclerView.Adapter<YearDataAdapter.YearViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.card_view_year_data, parent, false)

        return YearViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setAdapter(dataYears: List<RecordViewData>){
        dataSet = dataYears
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        dataSet.let{
            dataSet[position].let { data ->
                holder.itemView.txtYear.text = data.actualData.year

                data.total.toString().let{
                    val totalStr = "Total data volume - $it PB"
                    holder.itemView.txtVolume.text = totalStr
                }

                if(!data.isRegular){
                    holder.itemView.cardViewImage.visibility = View.VISIBLE
                    holder.itemView.imgGradeLow.setImageResource(R.drawable.grade_low)
                } else {
                    holder.itemView.cardViewImage.visibility = View.INVISIBLE
                }

                val str = "Q1 - ${data.actualData.firstQuarter} PB | Q2 - ${data.actualData.secondQuarter} PB | Q3 - ${data.actualData.thirdQuarter} PB | Q4 - ${data.actualData.forthQuarter} PB"
                holder.itemView.txtDetails.text = str
            }
        }
    }

    class YearViewHolder(dataYearView: View): RecyclerView.ViewHolder(dataYearView)

}