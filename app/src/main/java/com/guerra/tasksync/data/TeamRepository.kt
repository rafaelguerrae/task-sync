package com.guerra.tasksync.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeamRepository @Inject constructor (

) :ITeamRepository{

    override suspend fun getTeam(teamId: String): Result<Team> {
        return try {
            Result.success(Team(
                id = "1",
                name = "Rafael Guerra's Team",
                description = "Description",
                profilePictureUrl = "https://media.licdn.com/dms/image/v2/D4D03AQEihpv9eu9ZUQ/profile-displayphoto-shrink_800_800/B4DZUHK61pGcAc-/0/1739582058282?e=1747872000&v=beta&t=N-gA_yCLKA_EnB3sKkhHg7hqvutZxFiQxtsV_rSxnVg",
                members = listOf(
                    User(), User(), User(), User()
                ),
                tasks = listOf()
            ))
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTeams(userId: String): Result<List<Team>> {
        return try {
            Result.success(
                listOf(
                    Team(
                        id = "1",
                        name = "Rafael Guerra's Team",
                        description = "Description",
                        profilePictureUrl = "https://media.licdn.com/dms/image/v2/D4D03AQEihpv9eu9ZUQ/profile-displayphoto-shrink_800_800/B4DZUHK61pGcAc-/0/1739582058282?e=1747872000&v=beta&t=N-gA_yCLKA_EnB3sKkhHg7hqvutZxFiQxtsV_rSxnVg",
                        members = listOf(
                            User(), User(), User(), User()
                        ),
                        tasks = listOf()
                    ),
                    Team(
                        id = "2",
                        name = "Snoopy's Team",
                        description = "Description",
                        profilePictureUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR42_LOIslxyAWZU-CpORBw-WocqD5p9_ZZ_A&s",
                        members = listOf(

                            User(), User(), User(), User(),
                            User(), User(), User(), User()
                        ),
                        tasks = listOf()
                    ),
                    Team(
                        id = "3",
                        name = "ChatGPT's Team",
                        description = "Description",
                        profilePictureUrl = "https://corebase.com.br/wp-content/uploads/2023/05/64063dbcad97bd421b437096_chatgpt.jpg",
                        members = listOf(
                            User(), User(), User(), User(),
                            User(), User(), User(), User(),
                            User(), User(), User()
                        ),
                        tasks = listOf()
                    )
                )
            )
        }catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createTeam(team: Team): Result<Unit> {
        return try {
            Result.success(Unit)
        }catch (e: Exception) {
            Result.failure(e)
        }
    }
}