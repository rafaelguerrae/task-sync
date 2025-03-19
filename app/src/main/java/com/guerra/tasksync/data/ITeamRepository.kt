package com.guerra.tasksync.data

interface ITeamRepository {
    suspend fun getTeams(userId: String): Result<List<Team>>
    suspend fun getTeam(teamId: String): Result<Team>
    suspend fun createTeam(team: Team): Result<Unit>
}