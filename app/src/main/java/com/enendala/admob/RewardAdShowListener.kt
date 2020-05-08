/**
 * FileName: RewardAdListener
 *
 * Author: Codd Zhang<codd.zhang@gmail.com>
 *
 * Date: 2020/5/5 12:25 PM
 *
 * Description: 激励广告对业务模块暴露的监听器
 */

package com.enendala.admob

/**
 * 增加一个与sdk中相仿的接口目的在于解除业务层对sdk的依赖
 */
interface RewardAdShowListener {
    fun onShowed()
    fun onClosed()
    fun onEarned()
    fun onShowFailed(errorCode: Int)
}