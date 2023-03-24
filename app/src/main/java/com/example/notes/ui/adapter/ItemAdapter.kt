package com.example.notes.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.notes.databinding.EmptyLayoutBinding
import com.example.notes.databinding.ListItemBinding
import com.example.notes.model.BaseNotes
import com.example.notes.model.EmptyNotes
import com.example.notes.model.Notes
import com.example.notes.ui.baseinterface.OnClickListener
import java.text.SimpleDateFormat

class ItemAdapter(private val clickListener: OnClickListener) :
    ListAdapter<BaseNotes, ViewHolder>(DiffCallback) {
    private val TAG = "ItemAdapter"

    init{
        setHasStableIds(true)
    }

    var selectionTracker: SelectionTracker<Long>? = null

    private val EMPTY_VIEW = 90090

    object DiffCallback : DiffUtil.ItemCallback<BaseNotes>() {
        override fun areItemsTheSame(oldItem: BaseNotes, newItem: BaseNotes): Boolean {
            return if (oldItem is Notes && newItem is Notes) oldItem.id == newItem.id
            // since other layout is only 1 item i.e empty icon or layout so there is nothing to refresh
            else false
        }


        override fun areContentsTheSame(oldItem: BaseNotes, newItem: BaseNotes): Boolean {
            return if (oldItem is Notes && newItem is Notes) oldItem == newItem
            // since other layout is only 1 item i.e empty icon or layout so there is nothing to refresh
            else false
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if(viewType==EMPTY_VIEW) {
            val binding = EmptyLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            EmptyHolder(binding)
        } else {
            val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ItemHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)

        if(getItemViewType(position)!= EMPTY_VIEW){
            (holder as ItemHolder)
            if(item is Notes) {
                try{
                        if(position==0)Toast.makeText(holder.itemView.context, "$selectionTracker", Toast.LENGTH_SHORT).show()
                    selectionTracker?.let{
                        holder.bind(item, it.isSelected(item.id.toLong()))
                    }
                }
                catch (e:Exception){
                    Log.e(TAG, "onBindViewHolder: ${e.message}" )
                }
            }
        }else {
            (holder as EmptyHolder)
            if(item is EmptyNotes) holder.bind(item)
        }
    }

    inner class ItemHolder(private val binding: ListItemBinding) : ViewHolder(binding.root) {
        @SuppressLint("SimpleDateFormat")

        private val simpleDateFormat = SimpleDateFormat("dd MMM, hh:mm a")

        fun bind(notes: Notes,isChecked:Boolean = false) = with(binding){
                Log.d(TAG, "bind: $isChecked $absoluteAdapterPosition")
                notesTitle.text = notes.title
                notesDescription.text = notes.description
                eventDate.text=simpleDateFormat.format(notes.eventDate)
                itemChecked.visibility = if(isChecked) View.VISIBLE else View.GONE
                itemView.setOnLongClickListener {
                    clickListener.onLongItemClickListener(notes, it,absoluteAdapterPosition)
                }
                itemView.setOnClickListener {
                    clickListener.onItemClickListener(notes)
                }
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> {
            return object : ItemDetailsLookup.ItemDetails<Long>() {

                override fun getPosition(): Int{
                    return try{
                        absoluteAdapterPosition
                    } catch (e:Exception){
                        Log.e("TAG", "getPosition: ${e.message}")
                        return 0
                    }
                }
                override fun getSelectionKey(): Long{
                    return try{
                        (currentList[position]as Notes).id.toLong()
                    }
                    catch (e:Exception){
                        Log.e("TAG", "getSelectionKey: ${e.message}" )
                        0
                    }
                }
            }
        }
    }

    inner class EmptyHolder(private val binding: EmptyLayoutBinding) : ViewHolder(binding.root) {
        fun bind(item:EmptyNotes) {
            binding.emptyTitle.text = item.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)){
            is Notes-> position
            else -> EMPTY_VIEW
        }
    }


    override fun getItemId(position: Int): Long {
        return try{
            when (currentList[position]) {
                is Notes -> {
                    (currentList[position] as Notes).id.toLong()
                }
                else -> position.toLong()
            }
        }catch(e:Exception){
            Log.d("TAG", "getItemId: ${e.message}")
            position.toLong()
        }
    }

}

class NotesKeyProvider(private val adapter: ItemAdapter): ItemKeyProvider<Long>(SCOPE_CACHED) {
    override fun getKey(position: Int): Long? = (adapter.currentList[position] as? Notes)?.id?.toLong()

    override fun getPosition(key: Long): Int {
        return adapter.currentList.indexOfFirst { (it as? Notes)?.id?.toLong() == key }
    }
}
class NotesDetailLookUp(private val recyclerView: RecyclerView):ItemDetailsLookup<Long>(){

    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = recyclerView.findChildViewUnder(e.x,e.y)
        return try{
            if (view != null) {
                (recyclerView.getChildViewHolder(view)).run {
                    when (this) {
                        is ItemAdapter.ItemHolder -> {
                            this.getItemDetails()
                        }
                        else -> null
                    }
                }
            } else null
        }catch (e:Exception){
            Log.e("TAG", "getItemDetails: ${e.message}" )
            null
        }
    }

}
