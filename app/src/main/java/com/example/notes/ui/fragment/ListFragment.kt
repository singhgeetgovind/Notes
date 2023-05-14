package com.example.notes.ui.fragment


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.R
import com.example.notes.databinding.FragmentListBinding
import com.example.notes.model.EmptyNotes
import com.example.notes.model.Notes
import com.example.notes.services.cancelAlarm
import com.example.notes.ui.adapter.ItemAdapter
import com.example.notes.ui.adapter.NotesDetailLookUp
import com.example.notes.ui.adapter.NotesKeyProvider
import com.example.notes.ui.baseinterface.OnClickListener
import com.example.notes.viewmodels.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment(), OnClickListener{

    companion object {
        const val TAG = "ListFragment"
    }
    private var actionMode: ActionMode? = null
    private var keyList: MutableList<Int> = mutableListOf()
    private val viewModel: MyViewModel by activityViewModels()
    private lateinit var binding: FragmentListBinding
    private lateinit var itemAdapter: ItemAdapter

    private fun initTracker(): SelectionTracker<Long>? {
        val rv = binding.listOfAffirmations
        return try{
            SelectionTracker.Builder(
                "selection-id",
                rv,
                NotesKeyProvider(itemAdapter),
                NotesDetailLookUp(rv),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(SelectionPredicates.createSelectAnything()).build()
        }catch (e:Exception){
            Log.d("TAG", "selectionTracker: ${e.message}")
            null
        }
    }

    inner class ListSelectionObserver: SelectionObserver<Long>() {
        init{
            Log.d("TAG", "onItemStateChanged: ${hashCode()}")
        }
        override fun onItemStateChanged(key: Long, selected: Boolean) {
            try{
                if (selected) {
                    keyList.add(key.toInt())
                    if (itemAdapter.selectionTracker?.selection?.isEmpty != true) {
                        if (actionMode == null) {
                            actionMode = activity?.startActionMode(callback)
                        }
                    }
                } else {
                    keyList.remove(key.toInt())
                    if (itemAdapter.selectionTracker?.selection?.isEmpty == true) {
                        actionMode?.finish()
                    }
                }
            }catch (e:Exception){
                Log.e(TAG, "onItemStateChanged: ${e.message}" )
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        itemAdapter = ItemAdapter(this)
        binding.listOfAffirmations.adapter = itemAdapter
        itemAdapter.selectionTracker = initTracker()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.getData().observe(viewLifecycleOwner) {
            if(it.isNotEmpty()){
                if (it.size == 1) {
                    binding.listOfAffirmations.layoutManager =
                        StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                }
                itemAdapter.submitList(it)

            }
            else{
                itemAdapter.submitList(listOf(EmptyNotes(0,getString(R.string.emptyNotesMessage))))
                binding.listOfAffirmations.layoutManager = LinearLayoutManager(requireContext(),
                    LinearLayoutManager.HORIZONTAL,false)
            }
        }

        binding.addButton.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToAddFragment())
        }
        itemAdapter.selectionTracker?.addObserver(ListSelectionObserver())
    }

    override fun onItemClickListener(item: Notes) {

        val action = ListFragmentDirections.actionListFragmentToUpdateFragment(item)
        findNavController().navigate(action)
    }

    override fun onLongItemClickListener(item: Notes, view: View, position: Int): Boolean {
//        viewModel.deleteData(item.id)
        return true
    }


    /** ###  Method is overrides for menu on long press of list item */
    private var callback= object:ActionMode.Callback{
        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            Log.d(TAG, "onCreateActionMode: ")
            binding.addButton.visibility = View.GONE
            mode?.menuInflater?.inflate(R.menu.menu1, menu)
            return true
        }

        override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
            Log.d(TAG, "onPrepareActionMode: ")
            return false
        }

        override fun onActionItemClicked(mode: ActionMode?, menuItem: MenuItem?): Boolean {
            return when (menuItem?.itemId) {
                R.id.delete -> {
                    itemAdapter.selectionTracker?.hasSelection()?.run {
                        if(this){
                            viewModel.deleteData(keyList.toList())
                            CoroutineScope(Dispatchers.Main).launch{
                                keyList.forEach { item ->
                                    val intentId =
                                        (itemAdapter.currentList as List<Notes>).filter { it.id == item && it.eventDate != null }
                                    Log.d(TAG, "onActionItemClicked: $intentId")
                                    if (intentId.isNotEmpty()) {
                                        Log.d(TAG, "onActionItemClicked:if $intentId")
                                        intentId.first().intentId?.let { cancelAlarm(context, it) }
                                    }
                                }
                            }
                            actionMode?.finish()
                        }
                    }
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(p0: ActionMode?) {
            Log.d(TAG, "onDestroyActionMode: ")
            binding.addButton.visibility = View.VISIBLE
            keyList.clear()
            itemAdapter.selectionTracker?.clearSelection()
            actionMode = null
        }
    }


}

