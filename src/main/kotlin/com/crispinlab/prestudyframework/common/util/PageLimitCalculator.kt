package com.crispinlab.prestudyframework.common.util

internal object PageLimitCalculator {
    fun calculatePageLimit(
        page: Int,
        pageSize: Int,
        pageGroupSize: Int = 10
    ): Int {
        val groupNumber: Int = ((page - 1) / pageGroupSize) + 1
        val itemsPreGroup: Int = pageSize * pageGroupSize
        val lastItemIndex: Int = groupNumber * itemsPreGroup + 1
        return lastItemIndex
    }
}
