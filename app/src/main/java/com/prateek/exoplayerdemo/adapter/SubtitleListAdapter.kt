package com.prateek.exoplayerdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.prateek.exoplayerdemo.R
import com.prateek.exoplayerdemo.SubtitleTracksData

import java.util.ArrayList


class SubtitleListAdapter(private val items: ArrayList<SubtitleTracksData>) :
    BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(i: Int): SubtitleTracksData {
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
        holder.tvTitle.text = "${getItem(position).label} (${getItem(position).language})"
        holder.ivImage.visibility = View.GONE
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