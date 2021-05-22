package com.zfang.graphicdemo.view.test

object GuideInfoHelper {

    val guideInfoList = mutableListOf<GuideInfo>()
    var currentGuideIndex = 0

    fun addGuideInfo(guideInfo: GuideInfo) {
        guideInfoList.add(guideInfo)
    }

    fun getNext(): GuideInfo {
        return guideInfoList.get(currentGuideIndex++)
    }

    fun clearGuideInfo() {
        currentGuideIndex = 0
        guideInfoList.clear()
    }

    fun hasNext(): Boolean {
        return currentGuideIndex < guideInfoList.size
    }
}