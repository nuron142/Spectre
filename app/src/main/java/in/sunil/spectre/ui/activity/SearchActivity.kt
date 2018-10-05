package `in`.sunil.spectre.ui.activity

import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivitySearchBinding
import `in`.sunil.spectre.network.NetworkService
import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.ui.adapter.SearchAdapter
import `in`.sunil.spectre.ui.adapter.SearchArtistViewModel
import `in`.sunil.spectre.ui.adapter.SearchTrackViewModel
import `in`.sunil.spectre.ui.adapter.ViewModel
import `in`.sunil.spectre.util.getJson
import `in`.sunil.spectre.util.workOnBackgroundThread
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
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

    @Inject
    lateinit var networkService: NetworkService

    private lateinit var binding: ActivitySearchBinding

    private var searchAdapter: SearchAdapter? = null

    private var disposable = CompositeDisposable()
    private var searchDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as SpectreApplication).appComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        disposable.add(RxTextView.textChanges(binding.searchEdittext)
                .debounce(600, TimeUnit.MILLISECONDS)
                .map { charSequence -> charSequence.toString() }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ query ->
                    getSearchList(query)
                }, { e -> Log.d(TAG, "" + e.message) }))


        binding.closeButtonLayout.setOnClickListener {
            binding.searchEdittext.setText("")
//            ArtistDetailActivity.launch(this)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        val dataSet = ArrayList<ViewModel>()

        dataSet.add(SearchArtistViewModel("Sunil"))
        dataSet.add(SearchTrackViewModel("Darth Vader"))
        dataSet.add(SearchArtistViewModel("Spectre"))
        dataSet.add(SearchTrackViewModel("Commander Shepard"))
        dataSet.add(SearchArtistViewModel("Citadel"))
        dataSet.add(SearchTrackViewModel("Tali"))
        dataSet.add(SearchArtistViewModel("Liara"))

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        searchAdapter = SearchAdapter(dataSet)
        binding.recyclerView.adapter = searchAdapter

    }

    private fun getSearchList(query: String) {

        searchDisposable?.dispose()

        if (query.isNotEmpty()) {

            binding.closeButtonLayout.visibility = View.VISIBLE
            searchDisposable = workOnBackgroundThread({

                val searchResponse = networkService.getSearchQuery(query)
                Log.d(TAG, "Testing4 : " + searchResponse?.getJson())
            })

        } else {

            binding.closeButtonLayout.visibility = View.INVISIBLE
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
