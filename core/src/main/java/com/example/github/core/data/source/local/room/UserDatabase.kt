package com.example.github.core.data.source.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.github.core.data.source.local.entity.FavoriteUserEntity
import com.example.github.core.data.source.local.entity.UserMinimalEntity

@Database(
    entities = [UserMinimalEntity::class, FavoriteUserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
}