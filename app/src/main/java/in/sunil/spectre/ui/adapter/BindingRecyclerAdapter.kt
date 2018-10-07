package `in`.sunil.spectre.ui.adapter

import android.databinding.DataBindingUtil
import android.databinding.ObservableArrayList
import android.databinding.ObservableList
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR

/*
    Base recyclerview adapter which supports databinding
 */

/**
 * Created by Sunil on 10/5/18.
 */
class BindingRecyclerAdapter : RecyclerView.Adapter<BindingHolder> {

    private val dataSet: ObservableArrayList<ViewModel>
    private val viewModelLayoutIdMap: HashMap<Class<out ViewModel>, Int>

    constructor(dataSet: ObservableArrayList<ViewModel>, viewModelLayoutIdMap: HashMap<Class<out ViewModel>, Int>) {

        this.dataSet = dataSet
        this.viewModelLayoutIdMap = viewModelLayoutIdMap

        setUpListener()
    }

    override fun onCreateViewHolder(parent: ViewGroup, layoutId: Int): BindingHolder {

        val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutId, parent, false)

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

        val viewModel = dataSet[position]

        return viewModelLayoutIdMap[viewModel.javaClass] ?: -1
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
}
