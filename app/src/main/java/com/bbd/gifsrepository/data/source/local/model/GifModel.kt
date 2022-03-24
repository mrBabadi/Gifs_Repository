package com.bbd.gifsrepository.data.source.local.model

import android.os.Parcelable
import com.bbd.gifsrepository.data.enums.RatingSystem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GifModel(
    val id:String,
    val url: String,
    val shortUrl: String,
    val title: String,
    val rating: RatingSystem,
    val previewUrl : String?=""
):Parcelable