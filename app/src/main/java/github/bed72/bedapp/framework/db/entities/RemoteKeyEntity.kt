package github.bed72.bedapp.framework.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import github.bed72.core.data.constants.DbConstants

@Entity(tableName = DbConstants.REMOTE_KEYS_TABLE_NAME)
data class RemoteKeyEntity(

    @PrimaryKey
    val id: Int = 0,

    @ColumnInfo(name = DbConstants.REMOTE_KEYS_COLUMN_INFO_OFFSET)
    val nextOffset: Int?
)
