package github.bed72.bedapp.presentation.common.recycler

interface ListItem {
    val key: Long

    fun areContentsTheSame(other: ListItem) = this == other
    fun areItemsTheSame(other: ListItem) = this.key == other.key
}
