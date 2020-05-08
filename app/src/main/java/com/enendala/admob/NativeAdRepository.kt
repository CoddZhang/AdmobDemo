/**
 * FileName: NativeAdRepository
 *
 * Author: Codd Zhang<codd.zhang@gmail.com>
 *
 * Date: 2020/5/5 10:59 AM
 *
 * Description: 原生广告仓库，一个广告位id对应一个仓库
 */
package com.enendala.admob

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd
import java.util.*
import kotlin.collections.ArrayList


/**
 * 一个id对应一个广告仓库，仓库会自动进行广告的预加载（个数preloadCount），并向外提供广告内容
 *
 * id默认是测试广告
 */
class NativeAdRepository constructor(private var id: String = "ca-app-pub-3940256099942544/2247696110",
                                     private var preloadCount: Int = 1) {
    /**
     * 已加载的广告列表
     */
    var adList = LinkedList<UnifiedNativeAd>()

    /**
     * 是否正在进行广告加载
     */
    var isLoading = false

    /**
     * 正在加载的广告个数
     */
    var loadingCount = 0

    var loadListenerList = ArrayList<NativeAdLoadListener>()

    /**
     * 进行广告加载
     */
    fun loadAds(context: Context) {
        if (isLoading || adList.size >= preloadCount) {
            Log.w(AdmobManager.TAG, "$id NativeAd don't need load")
            return
        }

        isLoading = true //开始加载

        val request = AdRequest.Builder().build()

        val adLoader = AdLoader.Builder(context, id).forUnifiedNativeAd {//广告加载完成
            adList.offer(it)

            for(listener in loadListenerList) {
                listener.onLoaded()
            }

            onAdLoaded()
        }.withAdListener(object : AdListener() {
            override fun onAdFailedToLoad(errorCode: Int) {
                onAdLoaded()

                Log.w(AdmobManager.TAG, "$id NativeAd load failed, errorCode = $errorCode")
            }
        }).build()

        //开始加载
        loadingCount = preloadCount - adList.size
        adLoader.loadAds(request, loadingCount)
    }

    /**
     * 一个广告加载完成时调用
     */
    private fun onAdLoaded() {
        //更新正在加载的广告个数
        loadingCount--

        //所有广告加载完成，状态更新成未在加载中
        if (loadingCount <= 0) {
            isLoading = false

            //一般广告加载成功，收到一次通知即可，所以将所有监听器在这里进行移除
            if(adList.size > 0) {
                loadListenerList.clear()
            }
        }
    }

    /**
     * 是否已经有广告加载完成
     */
    fun isReady(context: Context): Boolean {
        return adList.isNotEmpty()
    }

    /**
     * 获取加载好的原生广告
     */
    fun getAd(context: Context): UnifiedNativeAd? {
        var ad = adList.poll()

        //广告被取走后，需要重新加载广告，保证池子里面有一定数量的预加载广告
        loadAds(context)

        return ad
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
    fun addLoadListener(listener: NativeAdLoadListener) {
        if (!loadListenerList.contains(listener)) {
            loadListenerList.add(listener)
        }
    }

    /**
     * 移除广告加载监听器
     */
    fun removeLoadListener(listener: NativeAdLoadListener) {
        loadListenerList.remove(listener)
    }
}

