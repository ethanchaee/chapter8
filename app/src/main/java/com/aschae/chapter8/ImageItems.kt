package com.aschae.chapter8

import android.net.Uri

sealed class ImageItems(open val id: String) {
    data class Image(
        val uri: Uri,
        override val id: String
    ) : ImageItems(id)

    object LoadMore : ImageItems(id = "footer")
}