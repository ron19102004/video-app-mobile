package com.video.app.api.models

enum class Privacy {
    PUBLIC,
    PRIVATE
}

data class PlaylistModel(
    var id: Long? = null,
    var name: String? = null,
    var image: String? = null,
    var privacy: Privacy? = Privacy.PRIVATE
)

//dto
data class CreatePlaylistDto(
    var name: String,
    var isPublic: Boolean
)