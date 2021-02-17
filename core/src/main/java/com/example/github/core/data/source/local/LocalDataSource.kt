package com.example.github.core.data.source.local

import android.util.Log
import com.example.github.core.data.source.local.entity.FavoriteUserEntity
import com.example.github.core.data.source.local.entity.UserMinimalEntity
import com.example.github.core.data.source.local.room.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSource @Inject constructor(private val userDao: UserDao) {

    suspend fun getListUserMinimal(): Flow<List<UserMinimalEntity>> =
        withContext(Dispatchers.IO) {
            userDao.getListUserMinimal()
        }

    suspend fun insertListUserMinimal(listUserMinimal: List<UserMinimalEntity>) =
        try {
            withContext(Dispatchers.IO) {
                userDao.insertListUserMinimal(listUserMinimal)
                Log.e("InsertList", "Success")
            }
        } catch (e: Exception) {
            Log.e("InsertList Failed", e.message.toString())
        }

    suspend fun getListFavoriteUser(): Flow<List<FavoriteUserEntity>> =
        withContext(Dispatchers.IO) {
            userDao.getListFavoriteUser()
        }

    suspend fun isFavorited(id: Int): Flow<Boolean> =
        withContext(Dispatchers.IO) {
            userDao.isFavorited(id)
        }

    suspend fun insertFavoriteUser(user: FavoriteUserEntity) {
        try {
            withContext(Dispatchers.IO) {
                val id = userDao.insertFavoriteUser(user)
                Log.e("InsertFav Id", id.toString())
            }
        } catch (e: Exception) {
            Log.e("InsertFav Failed", e.message.toString())
        }
    }

    suspend fun deleteFavoriteUser(user: FavoriteUserEntity) {
        try {
            withContext(Dispatchers.IO) {
                userDao.deleteFavoriteUser(user)
                Log.e("DeleteFav", "Success")
            }
        } catch (e: Exception) {
            Log.e("DeleteFav Failed", e.message.toString())
        }
    }
}