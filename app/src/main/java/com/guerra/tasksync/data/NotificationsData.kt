package com.guerra.tasksync.data

data class Notification(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val picture: String,
    var new: Boolean
)
