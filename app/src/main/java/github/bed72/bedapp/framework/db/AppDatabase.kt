package github.bed72.bedapp.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import github.bed72.bedapp.framework.db.daos.FavoriteDao
import github.bed72.bedapp.framework.db.entities.FavoriteEntity

@Database(entities = [FavoriteEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}