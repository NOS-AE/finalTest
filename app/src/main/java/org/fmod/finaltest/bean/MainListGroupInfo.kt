package org.fmod.finaltest.bean


/**
 * 每个Item对应一个GroupInfo
 *
 * 在加载数据库时被初始化
 */

class MainListGroupInfo(
    var groupId: Int,
    var pos: Int,
    var title: String,
    var size: Int
){
    fun isFirstInGroup() = pos == 0
    fun isLastInGroup() = pos - 1 == size
}