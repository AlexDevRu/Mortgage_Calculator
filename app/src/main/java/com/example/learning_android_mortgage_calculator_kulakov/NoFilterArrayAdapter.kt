package com.example.learning_android_mortgage_calculator_kulakov

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class NoFilterArrayAdapter(
    context: Context,
    private val items: Array<String>
): ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items) {

    override fun getFilter(): Filter {
        return KNoFilter()
    }

    inner class KNoFilter : Filter() {
        override fun performFiltering(p0: CharSequence?): FilterResults {
            val result = FilterResults()
            result.values = items
            result.count = items.size
            return result
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            notifyDataSetChanged()
        }
    }

}