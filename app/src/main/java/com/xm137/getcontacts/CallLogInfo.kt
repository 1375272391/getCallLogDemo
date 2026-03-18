package com.xm137.getcontacts

data class CallLogInfo (
    val id: Int,
    val formattedNumber: String?,
    val duration: Int,
    val number: String,
    val date: Long,
    val type: Int
)