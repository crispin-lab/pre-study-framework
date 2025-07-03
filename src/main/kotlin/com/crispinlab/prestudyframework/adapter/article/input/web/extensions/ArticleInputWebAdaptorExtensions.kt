package com.crispinlab.prestudyframework.adapter.article.input.web.extensions

import jakarta.servlet.ServletRequest

fun ServletRequest.getUserIdOrNull(): Long? = this.getAttribute("userId") as? Long
