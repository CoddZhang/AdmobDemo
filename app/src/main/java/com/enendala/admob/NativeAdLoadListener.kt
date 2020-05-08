/**
 * FileName: NativeAdLoadListener
 *
 * Author: Codd Zhang<codd.zhang@gmail.com>
 *
 * Date: 2020/5/6 8:11 AM
 *
 * Description: 暴露给业务层的原生广告加载监听器
 */

package com.enendala.admob

interface NativeAdLoadListener {
    fun onLoaded()
}