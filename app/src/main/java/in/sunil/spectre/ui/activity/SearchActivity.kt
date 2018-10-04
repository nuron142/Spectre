package `in`.sunil.spectre.ui.activity

import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivitySearchBinding
import `in`.sunil.spectre.network.NetworkService
import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.util.workOnBackgroundThread
import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Sunil on 10/4/18.
 */
class SearchActivity : AppCompatActivity() {

    companion object {

        val TAG = LoginActivity::class.java.simpleName

        fun launch(activity: Activity) {

            val intent = Intent(activity, SearchActivity::class.java)
            activity.startActivity(intent)
        }
    }

    @Inject
    lateinit var networkService: NetworkService

    private lateinit var binding: ActivitySearchBinding

    private var disposable = CompositeDisposable()
    private var searchDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as SpectreApplication).appComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        disposable.add(RxTextView.textChanges(binding.searchEdittext)
                .debounce(400, TimeUnit.MILLISECONDS)
                .map { charSequence -> charSequence.toString() }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ query ->
                    getBitMapFromNetwork(query)
                }, { e -> Log.d(TAG, "" + e.message) }))

        disposable.add(RxView.clicks(binding.search)
                .debounce(300, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    getBitMapFromNetwork(binding.searchEdittext.text.toString())
                }, { e -> Log.d(TAG, "" + e.message) }))
    }

    private fun getBitMapFromNetwork(query: String) {

        searchDisposable?.dispose()

        if (query.isNotEmpty()) {
            searchDisposable = workOnBackgroundThread({

                val inputStream = networkService.getSearchQuery(query)
            })
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}