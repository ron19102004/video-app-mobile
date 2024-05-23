package com.video.app.api.models

class ReportModel(
    val email: String,
    val content: String
)

class CommentModel(
    val id: Long? = 0,
    val content: String? = null,
    val user: UserModel? = null,
    val createdAt: String? = null,
    val replies: List<CommentModel>? = emptyList()
)

data class CreateCommentDto(
    val content: String,
    val parentCommentId: Long,
    val videoId: Long
)