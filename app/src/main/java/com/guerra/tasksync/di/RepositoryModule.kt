package com.guerra.tasksync.di

import com.guerra.tasksync.data.ITeamRepository
import com.guerra.tasksync.data.IUserRepository
import com.guerra.tasksync.data.TeamRepository
import com.guerra.tasksync.data.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): IUserRepository

    @Binds
    @Singleton
    abstract fun bindTeamRepository(
        teamRepository: TeamRepository
    ): ITeamRepository
}