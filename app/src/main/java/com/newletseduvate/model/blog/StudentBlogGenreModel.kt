package com.newletseduvate.model.blog

import com.google.gson.annotations.SerializedName
import com.newletseduvate.model.BottomSheetHelper
import com.newletseduvate.model.BottomSheetItem

data class StudentBlogGenreModel(
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
): BottomSheetHelper {
    data class Grade(
        @SerializedName("id")
        val id: Int,
        @SerializedName("grade_name")
        val gradeName: String
    )

    data class CreatedBy(
        @SerializedName("id")
        val id: Int,
        @SerializedName("username")
        val username: String,
        @SerializedName("first_name")
        val firstName: String,
        @SerializedName("last_name")
        val lastName: String
    )

    override fun convertToBottomSheetItem(): BottomSheetItem {
        return BottomSheetItem(id, genre)
    }
}