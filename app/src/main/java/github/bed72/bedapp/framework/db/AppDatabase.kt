package github.bed72.bedapp.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import github.bed72.bedapp.framework.db.daos.CharacterDao
import github.bed72.bedapp.framework.db.daos.FavoriteDao
import github.bed72.bedapp.framework.db.daos.RemoteKeyDao
import github.bed72.bedapp.framework.db.entities.CharacterEntity
import github.bed72.bedapp.framework.db.entities.FavoriteEntity
import github.bed72.bedapp.framework.db.entities.RemoteKeyEntity

@Database(
    entities = [
        FavoriteEntity::class,
        CharacterEntity::class,
        RemoteKeyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    abstract fun characterDao(): CharacterDao

    abstract fun remoteKeyDao(): RemoteKeyDao
}
