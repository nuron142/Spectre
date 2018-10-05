package `in`.sunil.spectre.ui.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.android.databinding.library.baseAdapters.BR

import `in`.sunil.spectre.R
import android.databinding.ObservableArrayList
import android.databinding.ObservableList

/**
 * Created by Sunil on 10/5/18.
 */
class SearchAdapter : RecyclerView.Adapter<BindingHolder> {

    companion object {

        const val VIEW_TYPE_ARTIST = 0
        const val VIEW_TYPE_TRACK = 1
    }

    private val dataSet: ObservableArrayList<ViewModel>

    constructor(dataSet: ObservableArrayList<ViewModel>) {

        this.dataSet = dataSet
        setUpListener()
    }

    private fun setUpListener() {

        dataSet.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<ViewModel>>() {

            override fun onChanged(sender: ObservableList<ViewModel>) {

                notifyDataSetChanged()
            }

            override fun onItemRangeChanged(sender: ObservableList<ViewModel>, positionStart: Int, itemCount: Int) {

                notifyItemRangeChanged(positionStart, itemCount)
            }

            override fun onItemRangeInserted(sender: ObservableList<ViewModel>, positionStart: Int, itemCount: Int) {

                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeMoved(sender: ObservableList<ViewModel>, fromPosition: Int, toPosition: Int, itemCount: Int) {

                notifyItemMoved(fromPosition, toPosition)
            }

            override fun onItemRangeRemoved(sender: ObservableList<ViewModel>, positionStart: Int, itemCount: Int) {

                notifyItemRangeRemoved(positionStart, itemCount)
            }
        })

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingHolder {

        val binding: ViewDataBinding

        if (viewType == VIEW_TYPE_ARTIST) {

            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.item_search_artist, parent, false)
        } else {

            binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                    R.layout.item_search_track, parent, false)
        }

        return BindingHolder(binding)
    }

    override fun onBindViewHolder(holder: BindingHolder, position: Int) {

        holder.binding.setVariable(BR.vm, dataSet[position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {

        return dataSet[position].getType()
    }
}