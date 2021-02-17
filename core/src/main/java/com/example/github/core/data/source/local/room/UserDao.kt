package com.example.github.core.data.source.local.room

import androidx.room.*
import com.example.github.core.data.source.local.entity.FavoriteUserEntity
import com.example.github.core.data.source.local.entity.UserMinimalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getListUserMinimal(): Flow<List<UserMinimalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListUserMinimal(listUserMinimal: List<UserMinimalEntity>)

    @Query("SELECT * FROM favorite_users")
    fun getListFavoriteUser(): Flow<List<FavoriteUserEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_users WHERE id = :id LIMIT 1)")
    fun isFavorited(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteUser(user: FavoriteUserEntity): Long

    @Delete
    suspend fun deleteFavoriteUser(user: FavoriteUserEntity)
}