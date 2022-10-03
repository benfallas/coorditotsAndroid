package com.coorditots

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView


class KeyboardAdapter(
    private val context: Context?,
    private val keys: List<Alphonic>,
    private val listener: Listener
): BaseAdapter() {



    // 2
    override fun getCount(): Int {
        return keys.size
    }

    // 3
    override fun getItemId(position: Int): Long {
        return 0
    }

    // 4
    override fun getItem(position: Int): Alphonic {
        return keys.get(position)
    }

    // 5
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.key_layout, null)

        // 3

        // 3
        val title: TextView = view.findViewById(R.id.key_title) as TextView

        // 4
        title.setText(keys.get(position).title)
        Log.d("KeyboardTest", "key vocal: " + keys.get(position).voiceUrl)
        val uri = Uri.parse(keys.get(position).voiceUrl)
        Log.d("KeyboardTest", "uri: " + uri)

        view.setOnClickListener {
            listener.onKeyTapped(key = keys.get(position))
        }

        return view
    }


    interface Listener {
        fun onKeyTapped(key: Alphonic)

    }
}