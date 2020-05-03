package com.imageaccident.pixe.data

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imageaccident.pixe.R
import org.w3c.dom.Text

class ImageCreationFragment : Fragment() {
    private val logTag = "ImageAccident.ICFrag"

    private lateinit var imageCreationListViewModel: ImageCreationListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ImageCreationAdapter
    private lateinit var imageCreationList : List<ImageCreation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(logTag, "onCreate()")

        val factory = ImageCreationListViewModelFactory(requireContext())
        imageCreationListViewModel = ViewModelProvider(this, factory).get(ImageCreationListViewModel::class.java)
    }

    companion object {
        fun newInstance(): ImageCreationFragment {
            return ImageCreationFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        updateUI(emptyList())
        return view
    }

    private fun updateUI(list: List<ImageCreation>) {
        adapter = ImageCreationAdapter(list)
        recyclerView.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageCreationListViewModel.listLiveData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { history ->
                history?.let {
                    Log.d(logTag, "Got history ${history.size}")
                    imageCreationList = history
                    updateUI(history)
                }
            }
        )
    }

    private inner class CreationHolder(view: View): RecyclerView.ViewHolder(view) {
        private lateinit var item : ImageCreation
        val dateTextView : TextView = itemView.findViewById(R.id.date_text_view)
        val algorithmTextView : TextView = itemView.findViewById(R.id.algorithm_text_view)
        val orientationTextView : TextView = itemView.findViewById(R.id.orientation_text_view)
        val versionTextView : TextView = itemView.findViewById(R.id.version_text_view)
        init {
            Log.d(logTag, "holder()")
        }

        fun bind(item: ImageCreation) {
            Log.d(logTag, "holder.bind()")
            this.item = item
            dateTextView.text = this.item.date.toString()
            algorithmTextView.text = this.item.algorithm
            orientationTextView.text = this.item.orientation
            versionTextView.text = this.item.version
        }
    }

    private inner class ImageCreationAdapter(var list: List<ImageCreation>) : RecyclerView.Adapter<CreationHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreationHolder {
            val view = layoutInflater.inflate(R.layout.list_item_history, parent, false)
            return CreationHolder(view)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: CreationHolder, position: Int) {
            holder.bind(list[position])
        }
    }
}