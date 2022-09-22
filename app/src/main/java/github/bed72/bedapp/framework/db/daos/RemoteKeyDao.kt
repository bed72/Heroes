package github.bed72.bedapp.framework.db.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import github.bed72.bedapp.framework.db.entities.RemoteKeyEntity
import github.bed72.core.data.constants.DbConstants

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeyEntity)

    @Query("SELECT * FROM ${DbConstants.REMOTE_KEYS_TABLE_NAME}")
    suspend fun remoteKey(): RemoteKeyEntity

    @Query("DELETE FROM ${DbConstants.REMOTE_KEYS_TABLE_NAME}")
    suspend fun clearAll()
}
