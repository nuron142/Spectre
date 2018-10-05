package `in`.sunil.spectre.ui.activity.search

import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivitySearchBinding
import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.ui.adapter.SearchAdapter
import `in`.sunil.spectre.util.Utilities
import `in`.sunil.spectre.util.itemanimators.AlphaCrossFadeAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager


/**
 * Created by Sunil on 10/4/18.
 */
class SearchActivity : AppCompatActivity() {

    companion object {

        val TAG: String = SearchActivity::class.java.simpleName

        fun launch(activity: Activity) {

            val intent = Intent(activity, SearchActivity::class.java)
            activity.startActivity(intent)
        }
    }

    private lateinit var binding: ActivitySearchBinding

    private var searchAdapter: SearchAdapter? = null
    private lateinit var searchActivityViewModel: SearchActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        searchActivityViewModel = SearchActivityViewModel(SearchActivityService(this, binding))
        (application as SpectreApplication).appComponent.inject(searchActivityViewModel)

        binding.vm = searchActivityViewModel
        binding.executePendingBindings()

        setupRecyclerView()

        Utilities.showKeyBoard(this, binding.searchEditText)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupRecyclerView() {

        val itemAnimator = AlphaCrossFadeAnimator()

        itemAnimator.addDuration = 200
        itemAnimator.removeDuration = 200
        itemAnimator.changeDuration = 200
        itemAnimator.moveDuration = 200

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(searchActivityViewModel.dataSet)

        binding.recyclerView.itemAnimator = itemAnimator
        binding.recyclerView.adapter = searchAdapter

        binding.recyclerView.setOnTouchListener { v, e ->

            Utilities.hideKeyBoard(this, binding.recyclerView)
            return@setOnTouchListener false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchActivityViewModel.onDestroy()
    }
}
