package com.runanywhere.startup_hackathon20.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun ToastMessage(
    message: String,
    icon: ImageVector = Icons.Default.CheckCircle,
    duration: Long = 3000,
    onDismiss: () -> Unit
) {
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        delay(duration)
        visible = false
        delay(300) // Wait for exit animation
        onDismiss()
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(
            initialOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF4CAF50), // Success green
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = message,
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun ToastHost(
    toastState: ToastState
) {
    val currentToast = toastState.currentToast

    if (currentToast != null) {
        ToastMessage(
            message = currentToast.message,
            icon = currentToast.icon,
            duration = currentToast.duration,
            onDismiss = { toastState.dismiss() }
        )
    }
}

// Toast state management
class ToastState {
    var currentToast by mutableStateOf<ToastData?>(null)
        private set

    fun showToast(
        message: String,
        icon: ImageVector = Icons.Default.CheckCircle,
        duration: Long = 3000
    ) {
        currentToast = ToastData(message, icon, duration)
    }

    fun dismiss() {
        currentToast = null
    }
}

data class ToastData(
    val message: String,
    val icon: ImageVector,
    val duration: Long
)

@Composable
fun rememberToastState(): ToastState {
    return remember { ToastState() }
}
