package github.bed72.bedapp.framework.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import github.bed72.bedapp.framework.db.entities.FavoriteEntity
import github.bed72.core.data.constants.DbConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM ${DbConstants.FAVORITES_TABLE_NAME}")
    fun loadFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM ${DbConstants.FAVORITES_TABLE_NAME} WHERE id = :characterId")
    suspend fun hasFavorite(characterId: Int): FavoriteEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity)
}
