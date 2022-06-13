package github.bed72.bedapp.framework.db.entities

import androidx.room.Entity
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

import github.bed72.core.data.constants.DbConstants

@Entity(tableName = DbConstants.CHARACTERS_TABLE_NAME)
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val autoId: Int = 0,

    @ColumnInfo(name = DbConstants.CHARACTERS_COLUMN_INFO_ID)
    val id: Int,

    @ColumnInfo(name = DbConstants.CHARACTERS_COLUMN_INFO_NAME)
    val name: String,

    @ColumnInfo(name = DbConstants.CHARACTERS_COLUMN_INFO_IMAGE_URL)
    val imageUrl: String
)