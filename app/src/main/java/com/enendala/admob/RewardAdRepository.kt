/**
 * FileName: RewardAdRepository
 *
 * Author: Codd Zhang<codd.zhang@gmail.com>
 *
 * Date: 2020/5/5 11:45 AM
 *
 * Description: 激励广告仓库
 */


package com.enendala.admob

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdCallback
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


class RewardAdRepository constructor(private var id: String = "ca-app-pub-3940256099942544/5224354917",
                                     private var preloadCount: Int = 1) {
    /**
     * 是否正在加载
     */
    var isLoading = false

    var adList = ArrayList<RewardedAd>()

    /**
     * 正在加载的广告个数
     */
    var loadingCount = 0

    /**
     * 正在播放的广告需要有明确的引用持有，否则在有些系统里面广告会被强制回收关闭
     */
    var rewardedAdShowing: RewardedAd? = null

    var loadListenerList = ArrayList<RewardAdLoadListener>()

    val rewardAdLoaderCallback = object : RewardedAdLoadCallback() {
        override fun onRewardedAdLoaded() {
            Log.i(AdmobManager.TAG, "$id Reward Ad loaded")

            for(listener in loadListenerList) {
                listener.onLoaded()
            }

            onAdLoaded()
        }

        override fun onRewardedAdFailedToLoad(var1: Int) {
            Log.e(AdmobManager.TAG, "$id Reward Ad load failed, errorCode = $var1")

            onAdLoaded()
        }
    }

    private fun onAdLoaded() {
        loadingCount--

        if (loadingCount <= 0) {
            isLoading = false

            //一般广告加载成功，收到一次通知即可，所以将所有监听器在这里进行移除
            if(adList.size > 0) {
                loadListenerList.clear()
            }
        }
    }

    /**
     * 加载广告
     */
    fun loadAds(context: Context) {
        if (isLoading) {
            return
        }

        //用来放没有加载成功的广告
        var removeList = ArrayList<RewardedAd>()

        for (rewardAd in adList) {
            //没加载成功的广告就丢到待删除列表里
            if (!rewardAd.isLoaded) {
                removeList.add(rewardAd)
            }
        }

        adList.removeAll(removeList)

        loadingCount = preloadCount - adList.size

        val request = AdRequest.Builder().build()

        while (adList.size < preloadCount) {
            var rewardedAd =  RewardedAd(context, id)
            adList.add(rewardedAd)
            rewardedAd.loadAd(request, rewardAdLoaderCallback)

            isLoading = true

            Log.i(AdmobManager.TAG, "$id Reward Ad loading")
        }
    }

    /**
     * 是否有加载完成的激励广告
     */
    fun isReady(context: Context): Boolean {
        for(rewardAd in adList) {
            if (rewardAd.isLoaded) {
                return true
            }
        }

        return false
    }

    /**
     * 显示激励广告
     */
    fun show(activity: Activity, listener: RewardAdShowListener?) {
        for(rewardAd in adList) {
            if (rewardAd.isLoaded) {
                Log.i(AdmobManager.TAG, "$id Reward Ad show")

                rewardedAdShowing = rewardAd

                rewardAd.show(activity, object : RewardedAdCallback() {
                    override fun onRewardedAdOpened() {
                        listener?.onShowed()
                    }

                    override fun onRewardedAdClosed() {
                        listener?.onClosed()

                        rewardedAdShowing = null
                    }

                    override fun onUserEarnedReward(@NonNull var1: RewardItem) {
                        listener?.onEarned()
                    }

                    override fun onRewardedAdFailedToShow(var1: Int) {
                        listener?.onShowFailed(var1)
                    }
                })
            }
        }
    }

    /**
     * 设置预加载个数
     */
    fun setPreloadCount(value: Int) {
        preloadCount = value
    }

    /**
     * 添加广告加载监听器。注意：监听器会在广告的一轮加载成功后被全部移除。
     */
    fun addLoadListener(listener: RewardAdLoadListener) {
        if (!loadListenerList.contains(listener)) {
            loadListenerList.add(listener)
        }
    }

    /**
     * 移除广告加载监听器
     */
    fun removeLoadListener(listener: RewardAdLoadListener) {
        loadListenerList.remove(listener)
    }
}