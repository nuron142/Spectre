package `in`.sunil.spectre.ui.adapter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.android.databinding.library.baseAdapters.BR

import `in`.sunil.spectre.R

/**
 * Created by Sunil on 10/5/18.
 */
class SearchAdapter : RecyclerView.Adapter<BindingHolder> {

    companion object {

        const val VIEW_TYPE_ARTIST = 0
        const val VIEW_TYPE_TRACK = 1
    }

    private val viewModelList: List<ViewModel>

    constructor(viewModelList: List<ViewModel>) {
        this.viewModelList = viewModelList
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

        holder.binding.setVariable(BR.vm, viewModelList[position])
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return viewModelList.size
    }

    override fun getItemViewType(position: Int): Int {

        return viewModelList[position].getType()
    }
}
