package com.enendala.admobdemo

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.enendala.admob.AdmobManager
import com.enendala.admob.NativeAdLoadListener
import com.enendala.admob.RewardAdLoadListener
import com.enendala.admob.RewardAdShowListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.native_ad.*

class MainActivity : AppCompatActivity() {

    val REWRDAD_TEST_ID = "ca-app-pub-3940256099942544/5224354917"
    val NATIVEAD_TEST_ID = "ca-app-pub-3940256099942544/2247696110"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AdmobManager.init(this)

        //------------激励广告------------

        refreshRewardAdStatus()

        AdmobManager.addRewardAdLoadListener(this, REWRDAD_TEST_ID, rewardAdLoadListener)

        btn_load_reward_ad.setOnClickListener {
            AdmobManager.loadRewardAd(this, REWRDAD_TEST_ID)
        }

        btn_show_reward_ad.setOnClickListener {
            AdmobManager.showRewardAd(this, REWRDAD_TEST_ID, object : RewardAdShowListener {
                override fun onShowed() {
                    Toast.makeText(this@MainActivity, "激励广告展示", Toast.LENGTH_SHORT).show()
                }

                override fun onClosed() {
                    Toast.makeText(this@MainActivity, "激励广告关闭", Toast.LENGTH_SHORT).show()
                }

                override fun onEarned() {
                    Toast.makeText(this@MainActivity, "得到奖励", Toast.LENGTH_SHORT).show()
                }

                override fun onShowFailed(errorCode: Int) {
                    Toast.makeText(this@MainActivity, "激励广告展示失败", Toast.LENGTH_SHORT).show()
                }
            })
        }

        //------------原生广告------------
        AdmobManager.addNativeAdLoadListener(this, NATIVEAD_TEST_ID, object : NativeAdLoadListener {
            override fun onLoaded() {
                showNativeAd()
            }
        })
        btn_load_native_ad.setOnClickListener {
            AdmobManager.loadNativeAd(this, NATIVEAD_TEST_ID)
        }
    }

    private var rewardAdLoadListener = object : RewardAdLoadListener {
        override fun onLoaded() {
            refreshRewardAdStatus()
        }
    }

    fun refreshRewardAdStatus() {
        if (AdmobManager.isRewardAdReady(this, REWRDAD_TEST_ID)) {
            tv_load_status.text = "激励广告已经加载完成，可以进行展示"
            btn_show_reward_ad.isEnabled = true
        } else {
            tv_load_status.text = "激励广告还未加载，无法展示"
            btn_show_reward_ad.isEnabled = false
        }
    }

    private fun showNativeAd() {
        native_ad_view.visibility = View.VISIBLE

        var nativeAd = AdmobManager.getNativeAd(this, NATIVEAD_TEST_ID)

        native_ad_view.headlineView = tv_header_line
        native_ad_view.mediaView = mv
        native_ad_view.iconView = iv_icon
        native_ad_view.bodyView = tv_desc

        mv.setImageScaleType(ImageView.ScaleType.CENTER_INSIDE)

        native_ad_view.setNativeAd(nativeAd)

        tv_header_line.text = nativeAd?.headline
        tv_desc.text = nativeAd?.body


        Glide.with(this).load(nativeAd?.icon?.uri).into(iv_icon)
    }
}
