package com.bbd.gifsrepository

import com.bbd.gifsrepository.data.enums.RatingSystem
import com.bbd.gifsrepository.data.source.local.model.GifModel

fun getFakeSearchData() = arrayListOf(
    GifModel("1", "banana.com", "https://bnn.com", "Banana", RatingSystem.PG, "banana.preview"),
    GifModel("2", "apple.com", "https://appl.com", "Apple", RatingSystem.G, "apple.preview"),
    GifModel("3", "kiwi.com", "https://kw.com", "Kiwi", RatingSystem.PG_13, "kiwi.preview"),
    GifModel("4", "orange.com", "https://orng.com", "Orange", RatingSystem.R, "orange.preview"),
)