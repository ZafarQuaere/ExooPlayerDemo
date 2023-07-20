package com.prateek.exoplayerdemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView


class ListPopupWindowAdapter(private val items: List<ListPopupItem>) :
    BaseAdapter() {

    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): ListPopupItem {
        return items[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder
        if (convertView == null) {
            convertView =
                LayoutInflater.from(parent.context).inflate(R.layout.popup_list_item, null)
            holder = ViewHolder(convertView)
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.tvTitle.setText(getItem(position).title)
        holder.ivImage.setImageResource(getItem(position).imgRes)
        return convertView!!
    }

    internal class ViewHolder(view: View) {
        var tvTitle: TextView
        var ivImage: ImageView

        init {
            tvTitle = view.findViewById<TextView>(R.id.text)
            ivImage = view.findViewById<ImageView>(R.id.image)
        }
    }
}