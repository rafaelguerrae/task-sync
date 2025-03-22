package com.guerra.tasksync.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.guerra.tasksync.R
import com.guerra.tasksync.data.Notification
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun NotificationsScreen() {


    BackHandler(enabled = true){}
    var userHasNotification by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val swipeStates = remember { mutableStateMapOf<String, Boolean>() }

    var notificationsList by remember {
        mutableStateOf(
            listOf(
                Notification(
                    id = "1",
                    title = "First Notification",
                    description = "Notification Description",
                    date = "Há 2 minutos",
                    picture = "https://cdn-icons-png.flaticon.com/512/8279/8279643.png",
                    new = true
                ),
                Notification(
                    id = "2",
                    title = "Second Notificationaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                    description = "Notification Descriptioaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaan",
                    date = "Há 11 minutos",
                    picture = "https://cdn-icons-png.flaticon.com/512/8279/8279643.png",
                    new = true
                ),
                Notification(
                    id = "3",
                    title = "Third Notification",
                    description = "Notification Description",
                    date = "Há 17 minutos",
                    picture = "https://cdn-icons-png.flaticon.com/512/8279/8279643.png",
                    new = true
                )
            )
        )
    }
    if (notificationsList.isNotEmpty()) userHasNotification = true

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier.imePadding()
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (!userHasNotification) {
                Text(
                    text = stringResource(R.string.no_notifications),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
                    fontSize = 12.sp
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(notificationsList.size) { index ->
                        val isSwiped = swipeStates[notificationsList[index].id] ?: false
                        NotificationItem(
                            isRevealed = isSwiped,
                            onExpanded = { swipeStates[notificationsList[index].id] = true },
                            onCollapsed = { swipeStates[notificationsList[index].id] = false },
                            actions = {
                                IconButton(
                                    onClick = {
                                        notificationsList =
                                            notificationsList.toMutableList().apply {
                                                this[index] = this[index].copy(new = false)
                                            }
                                        swipeStates[notificationsList[index].id] = false
                                    },
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.primary)
                                        .fillMaxHeight()
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MarkEmailRead,
                                            contentDescription = "Mark as Read",
                                            tint = Color.White
                                        )


                                        Text(
                                            text = "Mark as Read",
                                            fontSize = 8.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            maxLines = 2,
                                            minLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = {
                                        notificationsList =
                                            notificationsList.toMutableList().apply {
                                                removeAt(index)
                                            }
                                    },
                                    modifier = Modifier
                                        .background(Color.Red)
                                        .fillMaxHeight()
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ){
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color.White
                                        )

                                        Text(
                                            text = "Delete",
                                            fontSize = 8.sp,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Center,
                                            maxLines = 2,
                                            minLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            },
                            notificationData = notificationsList[index],
                            onClick = {
                                notificationsList = notificationsList.toMutableList().apply {
                                    this[index] = this[index].copy(new = false)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun NotificationItem(
    isRevealed: Boolean,
    actions: @Composable RowScope.() -> Unit,
    onExpanded: () -> Unit = {},
    onCollapsed: () -> Unit = {},
    notificationData: Notification,
    onClick: () -> Unit
) {
    val isNew = notificationData.new
    var contextMenuWidth by remember { mutableFloatStateOf(0f) }
    val offset = remember { Animatable(initialValue = 0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = isRevealed, contextMenuWidth) {
        if (isRevealed) {
            offset.animateTo(-contextMenuWidth)
        } else {
            offset.animateTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Row(
            modifier = Modifier
                .onSizeChanged { contextMenuWidth = it.width.toFloat() }
                .align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            actions()
        }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offset.value.roundToInt(), 0) }
                .pointerInput(contextMenuWidth) {
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { _, dragAmount ->
                            scope.launch {
                                val newOffset = (offset.value + dragAmount)
                                    .coerceIn(-contextMenuWidth, 0f)
                                offset.snapTo(newOffset)
                            }
                        },
                        onDragEnd = {
                            scope.launch {
                                if (offset.value <= -contextMenuWidth / 2f) {
                                    offset.animateTo(-contextMenuWidth)
                                    onExpanded()
                                } else {
                                    offset.animateTo(0f)
                                    onCollapsed()
                                }
                            }
                        }
                    )
                }
        ) {

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(75.dp)
                    .background(color = MaterialTheme.colorScheme.background)
                    .clickable {
                        onClick()
                    }
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {

                    Spacer(modifier = Modifier.size(8.dp))

                    SubcomposeAsyncImage(
                        model = notificationData.picture ?: R.drawable.default_pfp,
                        contentDescription = "Team Picture",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                        loading = {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(20.dp),
                                color = Color.White,
                                strokeWidth = 2.dp,
                                trackColor = colorResource(R.color.blue)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    Column(
                        horizontalAlignment = Alignment.Start
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = notificationData.title,
                                fontWeight = if (isNew) FontWeight.Bold else FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            if (isNew) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.size(4.dp))

                        Text(
                            text = notificationData.description,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.size(4.dp))

                        Text(
                            text = notificationData.date,
                            fontWeight = FontWeight.Light,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}