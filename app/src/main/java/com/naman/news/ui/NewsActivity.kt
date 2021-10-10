package com.naman.news.ui

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.naman.news.DBroom.ArticleDatabase
import com.naman.news.R
import com.naman.news.repository.NewsRepository
import com.google.android.gms.ads.*
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.android.synthetic.main.activity_news.*
import android.widget.Toast

import com.google.android.gms.tasks.OnCompleteListener

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
// important library for Google adMob
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds


class NewsActivity : AppCompatActivity() {

    lateinit var viewModel: NewsViewModel

    var mFirebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    private lateinit var template: TemplateView
    private var adLoaded = false
    private var adLoader: AdLoader? = null



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)


//initialize mobile ads
        MobileAds.initialize(this) {}

//setting of radio button
         val sharedPreference: SharedPreferences = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)

        if (sharedPreference != null) {
            if (sharedPreference.getString("cc", "") == "in") {
                radio2.isChecked = true
            } else {
                radio1.isChecked = true

            }
        }
//listener of radio button
        radioGroup1.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { _, checkedId -> // checkedId is the RadioButton selected
            val rb: RadioButton = findViewById<View>(checkedId) as RadioButton
            val editor = sharedPreference.edit()
            editor.putString("cc", rb.text.toString())
            editor.apply()
            finish()
            startActivity(getIntent())
            Toast.makeText(applicationContext, rb.text, Toast.LENGTH_SHORT).show()
        })


//        calling repository to populate data
        val newsRepository = NewsRepository(ArticleDatabase(this), this)
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(NewsViewModel::class.java)



//        Firebase RemoteConfig to se ads on/off
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this, OnCompleteListener<Boolean?> { task ->
                if (task.isSuccessful) {
                    val showAd = mFirebaseRemoteConfig.getBoolean("ads")
                    if (showAd) {
                        showAds()
                    }

                    Toast.makeText(
                        this@NewsActivity, "Fetch and activate succeeded",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@NewsActivity, "Fetch failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    private fun showAds() {
        adLoader = AdLoader.Builder(this, "ca-app-pub-5793669294286522/6281621795")
            .forNativeAd { nativeAd ->
                val styles =
                    NativeTemplateStyle.Builder()
                        .withMainBackgroundColor(ColorDrawable(Color.WHITE)).build()
                template = findViewById<TemplateView>(R.id.nativeTemplateView)
                template.setStyles(styles)
                template.setNativeAd(nativeAd)
                adLoaded = true

                loadNativeAd()

            }
            .build()


    }

    private fun loadNativeAd() {
        // Creating  an Ad Request
        val adRequest = AdRequest.Builder().build()

        // load Native Ad with the Request
        adLoader?.loadAd(adRequest)


        // Showing a simple Toast message to user when Native an ad is Loading
        Toast.makeText(this@NewsActivity, "Native Ad is loading ", Toast.LENGTH_LONG).show()
        showNativeAd()

    }

    private fun showNativeAd() {

        if (adLoaded) {
            template.visibility = View.VISIBLE
            // Showing a simple Toast message to user when an Native ad is shown to the user
            Toast.makeText(
                this@NewsActivity,
                "Native Ad  is loaded and Now showing ad  ",
                Toast.LENGTH_LONG
            ).show()
        } else {
            //Load the Native ad if it is not loaded
            loadNativeAd()
            // Showing a simple Toast message to user when Native ad is not loaded
            Toast.makeText(this@NewsActivity, "Native Ad is not Loaded ", Toast.LENGTH_LONG).show()
            loadNativeAd()

        }
    }
}
