package github.bed72.bedapp.framework.db.daos

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import github.bed72.core.data.constants.DbConstants
import github.bed72.bedapp.framework.db.entities.FavoriteEntity

@Dao
interface FavoriteDao {

    @Query(
        """
            SELECT ${DbConstants.FAVORITES_COLUMN_INFO_ID}, 
                ${DbConstants.FAVORITES_COLUMN_INFO_NAME},
                ${DbConstants.FAVORITES_COLUMN_INFO_IMAGE_URL}
            FROM ${DbConstants.FAVORITES_TABLE_NAME}
        """
    )
    suspend fun loadFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Delete
    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity)
}