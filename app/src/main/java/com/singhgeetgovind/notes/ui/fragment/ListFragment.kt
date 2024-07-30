package com.singhgeetgovind.notes.ui.fragment


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.SelectionTracker.SelectionObserver
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.search.SearchView
import com.singhgeetgovind.notes.R
import com.singhgeetgovind.notes.databinding.FragmentListBinding
import com.singhgeetgovind.notes.model.EmptyNotes
import com.singhgeetgovind.notes.model.Notes
import com.singhgeetgovind.notes.services.cancelAlarm
import com.singhgeetgovind.notes.ui.activity.MainActivity
import com.singhgeetgovind.notes.ui.adapter.ItemAdapter
import com.singhgeetgovind.notes.ui.adapter.NotesDetailLookUp
import com.singhgeetgovind.notes.ui.adapter.NotesKeyProvider
import com.singhgeetgovind.notes.ui.adapter.SearchAdapter
import com.singhgeetgovind.notes.ui.baseinterface.OnClickListener
import com.singhgeetgovind.notes.viewmodels.MyViewModel
import com.singhgeetgovind.notes.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment : Fragment(), OnClickListener,
    Toolbar.OnMenuItemClickListener {

    companion object {
        const val TAG = "ListFragment"
    }
    private var actionMode: ActionMode? = null
    private var keyList: MutableList<Int> = mutableListOf()
    private val viewModel: MyViewModel by activityViewModels()
    private val searchAdapter: SearchAdapter by lazy { SearchAdapter() }
    private val searchViewModel : SearchViewModel by viewModels()
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
                    Log.d(TAG, "onItemStateChanged: ")
                    keyList.remove(key.toInt())
                    if (itemAdapter.selectionTracker?.selection?.isEmpty == true) {
                        actionMode?.finish()
                    }
                }
                actionMode?.title="${keyList.size}"
            }catch (e:Exception){
                Log.e(TAG, "onItemStateChanged: ${e.message}" )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).sharedPreferences.fetchSharedPrefData<String>("ProfileImage").apply {
            viewModel.profileUrl = this ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentListBinding.inflate(inflater, container, false)
        itemAdapter = ItemAdapter(this)
        binding.listOfAffirmations.adapter = itemAdapter
        itemAdapter.selectionTracker = initTracker()
        binding.loadImage()
        binding.searchTopBar.setOnMenuItemClickListener {

            when(it.itemId){
                R.id.profile->{
                    findNavController().navigate(ListFragmentDirections.actionListFragmentToSettingFragment())
                    true
                }else->{
                    false
                }
            }
        }
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
//        binding.topBar.setOnMenuItemClickListener(this)
        binding.addButton.setOnClickListener {
            findNavController().navigate(ListFragmentDirections.actionListFragmentToEditFragment())
        }
        searchViewModel.searchResult.observe(viewLifecycleOwner){
            searchAdapter.submitList(it)
            binding.searchResult.adapter = searchAdapter
        }
        with(binding){
            searchField.editText.setOnEditorActionListener { v, actionId, event ->
                    when (actionId) {
                        EditorInfo.IME_ACTION_SEARCH -> {
                            searchViewModel.searchQuery.value = v.text.toString()
                            true
                        }
                        else -> {
                            false
                        }
                    }
                }
            searchField.addTransitionListener { _, _, newState ->
                when(newState){
                    SearchView.TransitionState.SHOWING,SearchView.TransitionState.SHOWN->{
                        addButton.isVisible= false
                    }
                    SearchView.TransitionState.HIDDEN,SearchView.TransitionState.HIDING->{
                        addButton.isVisible= true
                        searchAdapter.submitList(emptyList())
                    }
                    else->{
                        addButton.isVisible= false
                    }
                }
            }
        }
        itemAdapter.selectionTracker?.addObserver(ListSelectionObserver())
    }

    private fun FragmentListBinding.loadImage() {

            try {
                Glide.with(requireContext())
                    .load(viewModel.profileUrl)
                    .error(R.drawable.ic_baseline_account_circle_24)
                    .centerCrop()
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
//                    .sizeMultiplier(0.50f)
                    .addListener(object:RequestListener<Drawable>{
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e(TAG, "loadImage: failed")
                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.d(TAG, "loadImage: ready")
                            resource?.let { renderProfileImage(it) }
                            return true
                        }

                    }).submit()

            } catch (e: Exception) {
                Log.e(TAG, "loadImage: ${e.message}")
            }
    }

    private fun FragmentListBinding.renderProfileImage(resource: Drawable) {
        lifecycleScope.launch(Dispatchers.Main){ searchTopBar.menu.findItem(R.id.profile).icon = resource }
    }

    override fun onItemClickListener(item: Notes) {

        val action = ListFragmentDirections.actionListFragmentToEditFragment(item)
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
                            cancelOutReminder(keyList, itemAdapter.currentList as List<Notes>)
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

    private fun cancelOutReminder(key:List<Int>,notesList:List<Notes>) {
        val itemList:List<Notes> = notesList
        CoroutineScope(Dispatchers.Main).launch {
            key.forEach { item ->
                val intentId =
                    (itemList).filter { it.id == item && it.eventDate != null && it.eventDone == 0 }
                if (intentId.isNotEmpty()) {
                    intentId.first().intentId?.let { cancelAlarm(context, it) }
                }
            }
            this@ListFragment.keyList.clear()
            itemAdapter.selectionTracker?.clearSelection()
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search -> {
                findNavController().navigate(ListFragmentDirections.actionListFragmentToSearchDialogFragment())
                true
            }

            R.id.settings -> {
                findNavController().navigate(ListFragmentDirections.actionListFragmentToSettingFragment())
                true
            }

            else -> {true}
        }
    }


}

