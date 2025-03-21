package com.guerra.tasksync.screen.auth

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionResult
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.guerra.tasksync.R
import com.guerra.tasksync.data.SignInState
import com.guerra.tasksync.viewmodel.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun InitialScreen(
    appTheme: String,
    context: Context,
    state: SignInState,
    onGoogleClick: () -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: AuthViewModel,
    onLoaded: () -> Unit
) {
    val isDarkTheme = when (appTheme) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }
    val logoResId = if (isDarkTheme) R.drawable.tasksync_nobg_white else R.drawable.tasksync_nobg_black
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state.isSignInSuccessful, key2 = state.signInErrorMessage) {
        if (state.isSignInSuccessful || state.signInErrorMessage != null) {
            isLoading = false
            viewModel.resetState()
        }
    }

    val animationMap = mapOf(
        R.raw.team_animation to stringResource(R.string.team_animation_message),
        R.raw.woman_animation to stringResource(R.string.woman_animation_message),
        R.raw.task_check_animation to stringResource(R.string.task_check_animation_message),
        R.raw.pencil_tasks_animation to stringResource(R.string.pencil_tasks_animation_message)
    )
    val compositions = animationMap.keys.map { resId ->
        rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    }

    val areCompositionsLoaded = compositions.all { it.value != null }

    if (areCompositionsLoaded) {
        LaunchedEffect(Unit) {
            onLoaded()
        }
    }
    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        isLoading = true
                        onGoogleClick()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp,
                                trackColor = colorResource(R.color.blue)
                            )
                        }
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = if (isLoading) stringResource(R.string.signing_in)
                            else stringResource(R.string.enter_using_google),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }

                Button(
                    onClick = onSignInClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    shape = RoundedCornerShape(12.dp), enabled = !isLoading
                ) {
                    Text(
                        text = stringResource(R.string.sign_in_with_my_credentials),
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp, top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(R.string.dont_have_account),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = stringResource(R.string.sign_up),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(R.color.blue),
                        modifier = Modifier.clickable(enabled = !isLoading) { onSignUpClick() }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(logoResId),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier.size(120.dp)
            )
            Carousel(animationMap, isDarkTheme, compositions)
        }

    }
}

@Composable
fun Carousel(
    animationMap: Map<Int, String>,
    isDarkTheme: Boolean,
    compositions: List<LottieCompositionResult>
){
    val items = animationMap.entries.toList()
    val itemCount = items.size

    val pagerState = rememberPagerState(pageCount = { Int.MAX_VALUE })
    val coroutineScope = rememberCoroutineScope()

    val currentIndex = pagerState.currentPage % itemCount

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) { page ->
            val index = page % itemCount
            val compositionResult = compositions[index]
            if (compositionResult.value != null) {
                LottieAnimation(
                    composition = compositionResult.value,
                    iterations = LottieConstants.IterateForever,
                    speed = 1f,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                )
            }
        }

        AnimatedContent(
            targetState = items[currentIndex].value,
            transitionSpec = {
                fadeIn(animationSpec = tween(1000)) togetherWith fadeOut(animationSpec = tween(500))
            },
            modifier = Modifier.fillMaxWidth(0.8f)
        ) { target ->
            Text(
                text = target,
                fontSize = 12.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center,
                maxLines = 3,
                minLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            for (i in 0 until itemCount) {
                val indicatorColor = if (currentIndex == i) {
                    if (isDarkTheme) Color.LightGray else Color.Gray
                } else {
                    if (isDarkTheme) Color.Gray else Color.LightGray
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(indicatorColor)
                )
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        delay(7000L)
        val nextPage = pagerState.currentPage + 1
        coroutineScope.launch {
            pagerState.animateScrollToPage(nextPage)
        }
    }
}