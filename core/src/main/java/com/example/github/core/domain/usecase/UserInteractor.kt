package com.example.github.core.domain.usecase

import com.example.github.core.data.Resource
import com.example.github.core.domain.repository.IUserRepository
import com.example.github.core.ui.model.UserPresenter
import com.example.github.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class UserInteractor @Inject constructor(private val userRepository: IUserRepository) :
    UserUseCase {

    override suspend fun getListUser(): Flow<Resource<List<UserPresenter>>> =
        userRepository.getListUser().mapNotNull { resource ->
            when (resource) {
                is Resource.Success -> {
                    val listDomain = resource.data
                    val listPresenter = listDomain?.let {
                        it.map { domain -> DataMapper.mapUserDomainToUserPresenter(domain) }
                    }
                    listPresenter?.let { Resource.Success(it) }
                }
                is Resource.Loading -> Resource.Loading()
                is Resource.Error -> Resource.Error(resource.message ?: "Unknown Error")
            }
        }

    override suspend fun getDetailUser(username: String): Flow<Resource<UserPresenter>> =
        userRepository.getDetailUser(username).mapNotNull { resource ->
            when (resource) {
                is Resource.Success -> {
                    val domain = resource.data
                    val presenter = domain?.let { DataMapper.mapUserDomainToUserPresenter(it) }
                    presenter?.let { Resource.Success(it) }
                }
                is Resource.Loading -> Resource.Loading()
                is Resource.Error -> Resource.Error(resource.message ?: "Unknown Error")
            }
        }

    override suspend fun isFavorited(id: Int): Flow<Boolean> = userRepository.isFavorited(id)

    override suspend fun getListFavoriteUser(): Flow<List<UserPresenter>> =
        userRepository.getListFavoriteUser().map { listDomain ->
            listDomain.map { domain -> DataMapper.mapUserDomainToUserPresenter(domain) }
        }

    override suspend fun insertFavoriteUser(presenter: UserPresenter) {
        val domain = DataMapper.mapUserPresenterToUserDomain(presenter)
        userRepository.insertFavoriteUser(domain)
    }

    override suspend fun deleteFavoriteUser(presenter: UserPresenter) {
        val domain = DataMapper.mapUserPresenterToUserDomain(presenter)
        userRepository.deleteFavoriteUser(domain)
    }

    override suspend fun getSearchListUser(username: String): Flow<Resource<List<UserPresenter>>> =
        userRepository.getSearchListUser(username).mapNotNull { resource ->
            when (resource) {
                is Resource.Success -> {
                    val listDomain = resource.data
                    val listPresenter = listDomain?.let {
                        it.map { domain -> DataMapper.mapUserDomainToUserPresenter(domain) }
                    }
                    listPresenter?.let { Resource.Success(it) }
                }
                is Resource.Loading -> Resource.Loading()
                is Resource.Error -> Resource.Error(resource.message ?: "Unknown Error")
            }
        }
}