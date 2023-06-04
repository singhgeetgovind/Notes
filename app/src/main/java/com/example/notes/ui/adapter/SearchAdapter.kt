package com.example.notes.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.notes.databinding.SearchListItemBinding
import com.example.notes.model.Notes

class SearchAdapter:ListAdapter<Notes, SearchAdapter.SearchViewHolder>(DiffCallBack) {

    object DiffCallBack:DiffUtil.ItemCallback<Notes>(){
        override fun areItemsTheSame(oldItem: Notes, newItem: Notes): Boolean = oldItem==newItem

        override fun areContentsTheSame(oldItem: Notes, newItem: Notes): Boolean = oldItem==newItem

    }

    inner class SearchViewHolder(private val binding: SearchListItemBinding):ViewHolder(binding.root){
        fun bind(notes: Notes) {
            binding.searchText.text = notes.title
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = SearchListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}

