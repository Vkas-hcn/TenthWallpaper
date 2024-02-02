package com.good.fast.app.tenthwallpaper.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.good.fast.app.tenthwallpaper.R
import com.good.fast.app.tenthwallpaper.data.DataUtils

class ImageViewAdapter(private var context: Context) :
    RecyclerView.Adapter<ImageViewAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var listener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    companion object {
        private const val TYPE_FIRST_COLUMN = 1
        private const val TYPE_SECOND_COLUMN = 2
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgItem: AppCompatImageView = itemView.findViewById(R.id.aiv_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.img_view, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .load(DataUtils.localImageData[position])
            .thumbnail(0.12f)
            .placeholder(R.mipmap.ic_launcher_round)
            .error(R.mipmap.ic_launcher_round)
            .into(holder.imgItem)
        holder.itemView.setOnClickListener {
            listener?.onItemClick(position)
        }
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        layoutParams.height = getItemViewTypeFun(position)
        holder.itemView.layoutParams = layoutParams
    }

    override fun getItemCount(): Int {
        return DataUtils.localImageData.size
    }

    private fun getItemViewTypeFun(position: Int): Int {
        return when (position % 3) {
            0 -> {
                dp2px(261)
            }

            1, 2 -> {
                dp2px(130)
            }

            else -> {
                dp2px(261)
            }
        }

    }

    private fun px2dp(px: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }

    private fun dp2px(dp: Int): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
