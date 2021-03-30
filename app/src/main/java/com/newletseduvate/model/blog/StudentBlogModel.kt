package com.newletseduvate.model.blog


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StudentBlogModel(
    @SerializedName("id")
    val id: Int,
    @SerializedName("author")
    val author: Author,
    @SerializedName("grade")
    val grade: Any,
    @SerializedName("branch")
    val branch: Any,
    @SerializedName("section")
    val section: Any,
    @SerializedName("genre")
    val genre: Genre,
    @SerializedName("deleted_by")
    val deletedBy: Any,
    @SerializedName("updated_by")
    val updatedBy: Any,
    @SerializedName("feedback_revision_by")
    val feedbackRevisionBy: Any,
    @SerializedName("reviewed_by")
    val reviewedBy: Any,
    @SerializedName("published_by")
    val publishedBy: Any,
    @SerializedName("commented_by")
    val commentedBy: Any,
    @SerializedName("blog_fk_like")
    val blogFkLike: List<Any>,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("thumbnail")
    val thumbnail: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("delete_at")
    val deleteAt: Any,
    @SerializedName("updated_at")
    val updatedAt: Any,
    @SerializedName("likes")
    val likes: Int,
    @SerializedName("word_count")
    val wordCount: Int,
    @SerializedName("views")
    val views: Int,
    @SerializedName("feedback_revision_required")
    val feedbackRevisionRequired: Any,
    @SerializedName("revision_at")
    val revisionAt: Any,
    @SerializedName("remark_rating")
    val remarkRating: Any,
    @SerializedName("overall_remark")
    val overallRemark: Any,
    @SerializedName("average_rating")
    val averageRating: Any,
    @SerializedName("review_at")
    val reviewAt: Any,
    @SerializedName("published_level")
    val publishedLevel: String,
    @SerializedName("comment")
    val comment: Any
) : Serializable {
    data class Author(
        @SerializedName("id")
        val id: Int,
        @SerializedName("username")
        val username: String,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("last_name")
        val lastName: String
    ) : Serializable

    data class Genre(
        @SerializedName("id")
        val id: Int,
        @SerializedName("grade")
        val grade: Grade,
        @SerializedName("deleted_by")
        val deletedBy: Any,
        @SerializedName("created_by")
        val createdBy: CreatedBy,
        @SerializedName("updated_by")
        val updatedBy: Any,
        @SerializedName("genre")
        val genre: String,
        @SerializedName("genre_subtype")
        val genreSubtype: Any,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("delete_at")
        val deleteAt: String,
        @SerializedName("updated_at")
        val updatedAt: String,
        @SerializedName("is_deleted")
        val isDeleted: Boolean
    ) : Serializable {
        data class Grade(
            @SerializedName("id")
            val id: Int,
            @SerializedName("grade_name")
            val gradeName: String
        ) : Serializable

        data class CreatedBy(
            @SerializedName("id")
            val id: Int,
            @SerializedName("username")
            val username: String,
            @SerializedName("first_name")
            val firstName: String,
            @SerializedName("last_name")
            val lastName: String
        ) : Serializable
    }
}