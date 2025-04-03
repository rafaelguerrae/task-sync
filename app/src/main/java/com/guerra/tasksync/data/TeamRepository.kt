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
                profilePictureUrl = "https://raw.githubusercontent.com/Ecosynergy/EcoSynergy-App/refs/heads/main/app/src/main/res/drawable/r.png",
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
                        profilePictureUrl = "https://raw.githubusercontent.com/Ecosynergy/EcoSynergy-App/refs/heads/main/app/src/main/res/drawable/r.png",
                        members = listOf(
                            User(), User(), User(), User()
                        ),
                        tasks = listOf()
                    ),
                    Team(
                        id = "2",
                        name = "Snoopy's Team",
                        description = "Description",
                        profilePictureUrl = "https://raw.githubusercontent.com/Ecosynergy/EcoSynergy-App/refs/heads/main/app/src/main/res/drawable/s.png",
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
                        profilePictureUrl = "https://raw.githubusercontent.com/Ecosynergy/EcoSynergy-App/refs/heads/main/app/src/main/res/drawable/c.png",
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