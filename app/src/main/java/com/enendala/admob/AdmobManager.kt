/**
 * FileName: AdmobManager
 *
 * Author: Codd Zhang<codd.zhang@gmail.com>
 *
 * Date: 2020/5/5 10:56 AM
 *
 * Description: Admob单例
 */

package com.enendala.admob

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.UnifiedNativeAd

object AdmobManager {
    const val TAG = "AdmobManager"

    /**
     * 一个id对应一个广告仓库
     */
    var rewardedAdRepoMap: MutableMap<String, RewardAdRepository> = HashMap()

    var nativeAdRepoMap: MutableMap<String, NativeAdRepository> = HashMap()

    /**
     * 初始化
     */
    fun init(context: Context) {
        MobileAds.initialize(context)
    }


    //-------------------------激励广告-----------------


    /**
     * 是否有[id]对应的已经加载完成的激励广告
     */
    fun isRewardAdReady(context: Context?, id: String): Boolean {
        if (context == null || TextUtils.isEmpty(id)) {
            return false
        }

        var rewardAdRepo = rewardedAdRepoMap[id]

        if (rewardAdRepo == null) {
            rewardAdRepo = RewardAdRepository(id)
            rewardedAdRepoMap[id] = rewardAdRepo
        }

        return rewardAdRepo.isReady(context)
    }

    /**
     * 加载[id]激励广告，如果仓库没有建立，则建立完再加载
     */
    fun loadRewardAd(context: Context?, id: String, preloadCount: Int = 1) {
        if (context == null || TextUtils.isEmpty(id)) {
            return
        }

        var rewardAdRepo = rewardedAdRepoMap[id]

        if (rewardAdRepo == null) {
            rewardAdRepo = RewardAdRepository(id)
            rewardedAdRepoMap[id] = rewardAdRepo
        }

        rewardAdRepo.setPreloadCount(preloadCount)
        rewardAdRepo.loadAds(context)
    }

    /**
     * 显示激励广告
     *
     * return true : 显示成功
     * return false : 显示失败，可能广告仓库没建立，或者广告还没加载完成
     */
    fun showRewardAd(activity: Activity, id: String, listener: RewardAdShowListener?): Boolean {
        if (activity == null || TextUtils.isEmpty(id)) {
            return false
        }

        var rewardAdRepo = rewardedAdRepoMap[id]

        if (rewardAdRepo == null) {
            rewardAdRepo = RewardAdRepository(id)
            rewardedAdRepoMap[id] = rewardAdRepo
        }

        rewardAdRepo?.let {
            if (it.isReady(activity)) {
                it.show(activity, listener)

                return true
            }
        }

        Log.e(TAG, "$id reward ad is not ready")

        return false
    }

    /**
     * 添加激励广告加载监听器。注意：监听器会在广告的一轮加载成功后被全部移除。
     */
    fun addRewardAdLoadListener(context: Context?, id: String, listener: RewardAdLoadListener?) {
        if (context == null || TextUtils.isEmpty(id) || listener == null) {
            return
        }

        var rewardAdRepo = rewardedAdRepoMap[id]

        if (rewardAdRepo == null) {
            rewardAdRepo = RewardAdRepository(id)
            rewardedAdRepoMap[id] = rewardAdRepo
        }

        rewardAdRepo.addLoadListener(listener)
    }

    /**
     * 移除激励广告加载监听器
     */
    fun removeRewardAdLoadListener(context: Context?, id: String, listener: RewardAdLoadListener?) {
        if (context == null || TextUtils.isEmpty(id) || listener == null) {
            return
        }

        var rewardAdRepo = rewardedAdRepoMap[id]

        if (rewardAdRepo == null) {
            rewardAdRepo = RewardAdRepository(id)
            rewardedAdRepoMap[id] = rewardAdRepo
        }

        rewardAdRepo.removeLoadListener(listener)
    }


    //-------------------------原生广告-----------------

    /**
     * 是否有加载好的[id]对应的原生广告
     */
    fun isNativeAdReady(context: Context?, id: String): Boolean {
        if (context == null || TextUtils.isEmpty(id)) {
            return false
        }

        var nativeAdRepo = nativeAdRepoMap[id]

        if (nativeAdRepo == null) {
            nativeAdRepo = NativeAdRepository(id)
            nativeAdRepoMap[id] = nativeAdRepo
        }

        return nativeAdRepo.isReady(context)
    }

    /**
     * 加载原生广告
     */
    fun loadNativeAd(context: Context?, id: String, preloadCount: Int = 1) {
        if (context == null || TextUtils.isEmpty(id)) {
            return
        }

        var nativeAdRepo = nativeAdRepoMap[id]

        if (nativeAdRepo == null) {
            nativeAdRepo = NativeAdRepository(id)
            nativeAdRepoMap[id] = nativeAdRepo
        }

        nativeAdRepo.setPreloadCount(preloadCount)
        nativeAdRepo.loadAds(context)
    }

    /**
     * 获取原生广告内容
     */
    fun getNativeAd(context: Context?, id: String): UnifiedNativeAd? {
        if (context == null || TextUtils.isEmpty(id)) {
            return null
        }

        var nativeAdRepo = nativeAdRepoMap[id]

        if (nativeAdRepo == null) {
            nativeAdRepo = NativeAdRepository(id)
            nativeAdRepoMap[id] = nativeAdRepo
        }

        return nativeAdRepo.getAd(context)
    }

    /**
     * 添加原生广告加载监听器。注意：监听器会在广告的一轮加载成功后被全部移除。
     */
    fun addNativeAdLoadListener(context: Context?, id: String, listener: NativeAdLoadListener?) {
        if (context == null || TextUtils.isEmpty(id) || listener == null) {
            return
        }

        var nativeAdRepo = nativeAdRepoMap[id]

        if (nativeAdRepo == null) {
            nativeAdRepo = NativeAdRepository(id)
            nativeAdRepoMap[id] = nativeAdRepo
        }

        nativeAdRepo.addLoadListener(listener)
    }

    /**
     * 移除原生广告加载监听器
     */
    fun removeNativeAdLoadListener(context: Context?, id: String, listener: NativeAdLoadListener?) {
        if (context == null || TextUtils.isEmpty(id) || listener == null) {
            return
        }

        var nativeAdRepo = nativeAdRepoMap[id]

        if (nativeAdRepo == null) {
            nativeAdRepo = NativeAdRepository(id)
            nativeAdRepoMap[id] = nativeAdRepo
        }

        nativeAdRepo.removeLoadListener(listener)
    }
}