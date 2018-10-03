package `in`.sunil.spectre.ui.activity

import `in`.sunil.spectre.R
import `in`.sunil.spectre.databinding.ActivityMainBinding
import `in`.sunil.spectre.spectre.Spectre
import `in`.sunil.spectre.spectre.SpectreOptions
import `in`.sunil.spectre.ui.SpectreApplication
import `in`.sunil.spectre.util.workOnMainThread
import android.animation.ObjectAnimator
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import javax.inject.Inject

import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * Created by Sunil on 26/09/17.
 */

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName

    @Inject
    lateinit var spectre: Spectre

    private val imageUrls = listOf(
            "https://i.imgur.com/v3El8IJ.jpg",
            "https://i.imgur.com/xkLkMbd.jpg",
            "https://i.imgur.com/ZOHPK51.jpg",
            "https://i.imgur.com/u1JaVOT.jpg",
            "https://i.imgur.com/wRycaNb.jpg",
            "https://i.imgur.com/JOcWgPC.jpg"
    )

    private var count = 0
    private lateinit var binding: ActivityMainBinding

    private var disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as SpectreApplication).appComponent.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.changeImage.setOnClickListener {
            requestImage()
        }

        disposable.add(
                RxView.clicks(binding.changeImage)
                        .debounce(200, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ _ -> requestImage() }, { e -> Log.d(TAG, "" + e.message) })
        )

        requestImage(R.drawable.placeholder)
    }

    private fun requestImage(placeholder: Int = 0) {

        binding.errorText.visibility = View.GONE

        val index = Random().nextInt(imageUrls.size)
        val imageUrl = imageUrls[index]

        val spectreOptions = SpectreOptions(imageUrl)
                .loadingFromNetwork {
                    showInitialProgress()
                }
                .progressListener { progress ->

                    handleProgressBar(progress)
                }

        if (placeholder != 0) {
            spectreOptions.placeHolder(placeholder)
        }

        spectre.loadImage(binding.image, spectreOptions) {
            disposable.add(
                    workOnMainThread({
                        showImageFailed()
                    })
            )
        }
    }

    private fun showImageFailed() {

        hideProgressBar()
        binding.errorText.visibility = View.VISIBLE
    }

    private fun showInitialProgress() {

        binding.smallTimerProgress.progress = 0
        binding.smallTimerProgress.alpha = 1f

        disposable.add(workOnMainThread({

            val anim = ObjectAnimator.ofInt(binding.smallTimerProgress, "progress", 0, 15)
            anim.duration = 400
            anim.interpolator = DecelerateInterpolator()
            anim.start()
        }))
    }

    private fun handleProgressBar(progress: Int) {

        if (progress in 21..99) {
            showProgressBar(progress)
        } else if (progress == 100) {
            showProgressBar(progress)
            hideProgressBar()
        }
    }

    private fun showProgressBar(progress: Int) {

        binding.smallTimerProgress.progress = progress
        binding.smallTimerProgress.alpha = 1f
    }

    private fun hideProgressBar() {

        binding.smallTimerProgress.animate()
                .alpha(0f)
                .setStartDelay(100)
                .setDuration(300)
                .setInterpolator(LinearInterpolator())
                .start()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
