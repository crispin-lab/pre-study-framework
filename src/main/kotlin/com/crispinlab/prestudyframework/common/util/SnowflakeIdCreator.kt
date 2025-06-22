package com.crispinlab.prestudyframework.common.util

import com.crispinlab.Snowflake

object SnowflakeIdCreator {
    private val snowflake: Snowflake = Snowflake.create()

    fun nextId(): Long = snowflake.nextId()
}
