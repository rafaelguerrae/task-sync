package com.guerra.tasksync.data

data class Team(
    val id: String,
    val name: String,
    val description: String,
    val profilePictureUrl: String? = null,
    val tasks: List<Task>,
    val members: List<User>
)