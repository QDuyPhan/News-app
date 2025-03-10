package com.example.newsapp.data.response

import com.google.gson.annotations.SerializedName

data class IntrospectResponse(
    @SerializedName("valid") val valid: Boolean
)
