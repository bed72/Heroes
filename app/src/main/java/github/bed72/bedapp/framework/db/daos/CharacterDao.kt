package github.bed72.bedapp.framework.db.daos

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.paging.PagingSource
import androidx.room.OnConflictStrategy

import github.bed72.core.data.constants.DbConstants
import github.bed72.bedapp.framework.db.entities.CharacterEntity

@Dao
interface CharacterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(characters: List<CharacterEntity>)

    @Query("SELECT * FROM ${DbConstants.CHARACTERS_TABLE_NAME}")
    fun pagingSource(): PagingSource<Int, CharacterEntity>

    @Query("DELETE FROM ${DbConstants.CHARACTERS_TABLE_NAME}")
    suspend fun clearAll()
}