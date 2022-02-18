package github.bed72.bedapp.presentation.detail.args

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DetailViewArg(
    val name: String,
    val imageUrl: String,
    val characterId: Int
) : Parcelable
