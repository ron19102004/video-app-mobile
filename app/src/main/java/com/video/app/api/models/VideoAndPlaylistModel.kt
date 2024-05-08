package com.video.app.api.models

enum class Privacy {
    PUBLIC,
    PRIVATE
}

data class VideoModel(
    var id: Long? = null,
    var name:String?=null,
    var src: String? = null,
    var slug: String? = null,
    var duration: String? = null,
    var tag: String? = null,
    var privacy: Privacy? = Privacy.PRIVATE,
    var image: String? = null,
    var release: String? = null,
    var vip: Boolean? = false,
    var description: String? = null,
    var category: CategoryModel? = null,
    var country: CountryModel? = null,
    var uploader: UserModel? = null
)

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