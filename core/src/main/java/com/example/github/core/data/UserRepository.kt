package com.example.github.core.data

import com.example.github.core.data.source.local.LocalDataSource
import com.example.github.core.data.source.remote.RemoteDataSource
import com.example.github.core.data.source.remote.network.ApiResponse
import com.example.github.core.data.source.remote.response.UserResponse
import com.example.github.core.domain.model.UserDomain
import com.example.github.core.domain.repository.IUserRepository
import com.example.github.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : IUserRepository {

    override suspend fun getListUser(): Flow<Resource<List<UserDomain>>> =
        object : NetworkBoundResource<List<UserDomain>, List<UserResponse>>() {
            override suspend fun loadFromDB(): Flow<List<UserDomain>> =
                localDataSource.getListUserMinimal().map { listEntity ->
                    listEntity.map { DataMapper.mapUserMinimalEntityToUserDomain(it) }
                }

            override fun shouldFetch(data: List<UserDomain>?): Boolean =
                data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<UserResponse>>> =
                remoteDataSource.getListUser()

            override suspend fun saveCallResult(data: List<UserResponse>) {
                val listEntity = data.map { DataMapper.mapUserResponseToUserMinimalEntity(it) }
                localDataSource.insertListUserMinimal(listEntity)
            }

        }.asFlow()

    override suspend fun getDetailUser(username: String): Flow<Resource<UserDomain>> =
        remoteDataSource.getDetailUser(username).map { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val domain = DataMapper.mapUserResponseToUserDomain(response.data)
                    Resource.Success(domain)
                }
                is ApiResponse.Empty -> {
                    val domain = DataMapper.mapUserResponseToUserDomain(null)
                    Resource.Success(domain)
                }
                is ApiResponse.Error -> Resource.Error(response.errorMessage)
            }
        }

    override suspend fun isFavorited(id: Int): Flow<Boolean> = localDataSource.isFavorited(id)

    override suspend fun getListFavoriteUser(): Flow<List<UserDomain>> =
        localDataSource.getListFavoriteUser().map { listEntity ->
            listEntity.map { entity -> DataMapper.mapFavoriteUserEntityToUserDomain(entity) }
        }

    override suspend fun insertFavoriteUser(domain: UserDomain) {
        val entity = DataMapper.mapUserDomainToFavoriteUserEntity(domain)
        localDataSource.insertFavoriteUser(entity)
    }

    override suspend fun deleteFavoriteUser(domain: UserDomain) {
        val entity = DataMapper.mapUserDomainToFavoriteUserEntity(domain)
        localDataSource.deleteFavoriteUser(entity)
    }

    override suspend fun getSearchListUser(username: String): Flow<Resource<List<UserDomain>>> =
        remoteDataSource.getSearchListUser(username).map { response ->
            when (response) {
                is ApiResponse.Success -> {
                    val listResponse = response.data
                    val listDomain = listResponse.map { DataMapper.mapUserResponseToUserDomain(it) }
                    Resource.Success(listDomain)
                }
                is ApiResponse.Empty -> {
                    val listDomain = listOf<UserDomain>()
                    Resource.Success(listDomain)
                }
                is ApiResponse.Error -> Resource.Error(response.errorMessage)
            }
        }
}