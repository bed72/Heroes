package github.bed72.bedapp.presentation.detail.data

import androidx.annotation.StringRes

data class DetailChildViewItem(
    val id: Int,
    val imageUrl: String
)

data class DetailParentViewItem(
    @StringRes
    val categoryStringResId: Int,
    val detailChildViewItem: List<DetailChildViewItem> = listOf()
)
