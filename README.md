# 前言

关于Admob如何接入，官方文档已经写的很清楚，所以不是本文的重点。本文主要讲Admob的调用封装。因为作为开发者接入Admob之后，其实工作远没有结束，可能想做预加载，想做些封装让接口更加简洁友好，此外还想让业务代码尽可能不与SDK耦合，等等。其实这部分内容也是一个轮子，那我们就不要重复造轮子了。

# Admob接入

1\. 官方网站：[https://developers.google.cn/admob/android/quick-start](https://developers.google.cn/admob/android/quick-start)

官网文档有中英文版本，阅读上不是什么问题，实际接入难度不大。只是随着版本的更新，文档同步的不是很及时。

2\. 官方开发论坛：[https://groups.google.com/forum/#!forum/google-admob-ads-sdk](https://link.jianshu.com/?t=https%3A%2F%2Fgroups.google.com%2Fforum%2F%23%21forum%2Fgoogle-admob-ads-sdk)

遇到一些问题可以到论坛搜索下。我在开发时遇到一个兼容性问题，某些手机上激励广告播放时会突然被强制关闭，后来就是在论坛上找到了解决方案：正在播放的激励广告需要有明确的引用持有，否则可能被系统回收导致广告关闭。

# 二次封装

完整代码可以查看本文后面的github项目地址。

#### AdmobManager

框架内唯一暴露给业务层的接口，是一个单例。业务层只需要跟它打交道就可以进行加载、获取、显示广告等操作。

#### NativeAdRepository

原生高级广告仓库，一个广告位id对应一个广告仓库。本质是一个广告池，可实现给定数量的广告预加载，并在广告被使用后自动进行个数的加载补足。


#### RewardAdRepository

激励广告仓库，一个广告位id对应一个广告参考，实现方式与原生高级广告基本一致。

# Demo说明

包含：

1\. 对SDK调用的封装代码

2\. 使用测试id实现激励广告和原生高级广告。

![](https://github.com/CoddZhang/image/blob/master/4593626-ad89befac4a1bc1f.webp)

# 写在最后

Admob其实是被Google收购的，并非土著的Google开发团队作品，所以SDK的设计水准感觉一般，友好性和自由度上做的不太好，不够赏心悦目。

