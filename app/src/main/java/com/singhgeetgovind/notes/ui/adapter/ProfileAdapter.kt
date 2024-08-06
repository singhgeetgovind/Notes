package com.singhgeetgovind.notes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.SearchListItemBinding
import com.singhgeetgovind.notes.utils.avatar.Avatar

class ProfileAdapter() : ListAdapter<Avatar, ProfileAdapter.SearchViewHolder>(DiffCallBack) {
    private lateinit var context : Context
    constructor(context: Context):this(){
        this.context = context
    }

    fun <T:Enum<T>> submit(list: MutableList<T>) {
        submitList(list as MutableList<Avatar>)
    }

    object DiffCallBack:DiffUtil.ItemCallback<Avatar>(){
        override fun areItemsTheSame(oldItem: Avatar, newItem: Avatar): Boolean = true

        override fun areContentsTheSame(oldItem: Avatar, newItem: Avatar): Boolean = true

    }

    inner class SearchViewHolder(private val binding: SearchListItemBinding):ViewHolder(binding.root){
        fun bind(url: String) {
                Glide.with(context)
                    .load(url)
                    .centerCrop()
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .into(binding.profileImage)
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = SearchListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(currentList[position].getBASEURL())
    }

    override fun getItemCount(): Int {
        return currentList.size
    }
}

