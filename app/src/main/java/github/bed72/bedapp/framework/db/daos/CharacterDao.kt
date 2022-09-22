package github.bed72.bedapp.framework.db.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import github.bed72.bedapp.framework.db.entities.CharacterEntity
import github.bed72.core.data.constants.DbConstants

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM ${DbConstants.CHARACTERS_TABLE_NAME}")
    fun pagingSource(): PagingSource<Int, CharacterEntity>

    @Query("DELETE FROM ${DbConstants.CHARACTERS_TABLE_NAME}")
    suspend fun clearAll()
}
