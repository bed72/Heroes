package github.bed72.bedapp.presentation.detail.entities

import androidx.annotation.StringRes

data class DetailChildViewEntity(
    val id: Int,
    val imageUrl: String
)

data class DetailParentViewEntity(
    @StringRes
    val categoryStringResId: Int,
    val detailChildViewEntity: List<DetailChildViewEntity> = listOf()
)