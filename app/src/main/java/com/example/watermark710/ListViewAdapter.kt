package com.example.watermark710

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ListViewAdapter(val list: MutableList<DataModel>):BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(p0: Int): Any {
        return list[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            convertView =  LayoutInflater.from(parent?.context).inflate(R.layout.listview_item,parent,false)
        }
        val date = convertView?.findViewById<TextView>(R.id.listViewDateArea)
        val memo = convertView?.findViewById<TextView>(R.id.listViewMemoArea)

        date!!.text = list[position].date
        memo!!.text = list[position].memo

        return convertView!!
    }
}