package com.example.liuruyi.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.liuruyi.data.SessionMeta
import com.example.liuruyi.ui.components.Avatar
import java.time.Instant
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SessionListScreen(
    vm: MainViewModel,
    onOpenSession: (String) -> Unit,
    onOpenSettings: () -> Unit,
) {
    val state by vm.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val avatarPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) vm.setAvatar(uri)
    }

    LaunchedEffect(state.deleteUndoActive) {
        if (state.deleteUndoActive) {
            val result = snackbarHostState.showSnackbar(
                message = "已删除会话",
                actionLabel = "撤销",
                duration = SnackbarDuration.Short,
            )
            if (result == SnackbarResult.ActionPerformed) vm.undoDeleteSession()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.clip(CircleShape).clickable { avatarPicker.launch("image/*") }) {
                            Avatar(state.avatarPath, size = 34.dp)
                        }
                        Spacer(Modifier.width(10.dp))
                        Text("微信")
                    }
                },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "设置")
                    }
                },
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        if (state.sessions.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("正在初始化…", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(Modifier.fillMaxSize().padding(padding)) {
                items(state.sessions, key = { it.id }) { meta ->
                    ContactRow(
                        meta = meta,
                        avatarPath = state.avatarPath,
                        onClick = {
                            vm.selectSession(meta.id)
                            onOpenSession(meta.id)
                        },
                        onLongClick = { vm.deleteSession(meta.id) },
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ContactRow(meta: SessionMeta, avatarPath: String, onClick: () -> Unit, onLongClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar(filePath = avatarPath, size = 48.dp, label = "靓")
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("刘茹依", style = MaterialTheme.typography.titleMedium)
            Text(
                meta.lastMessagePreview.takeIf { it.isNotBlank() && it != "null" && it != "undefined" } ?: "开始聊天",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(timeHint(meta.updatedAt), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
    }
}

private fun timeHint(ts: Long): String {
    if (ts <= 0) return ""
    val zone = ZoneId.systemDefault()
    val now = Instant.now().atZone(zone).toLocalDate()
    val t = Instant.ofEpochMilli(ts).atZone(zone)
    val d = t.toLocalDate()
    val hm = t.hour.toString().padStart(2, '0') + ":" + t.minute.toString().padStart(2, '0')
    return when {
        d == now -> hm
        d == now.minusDays(1) -> "昨天"
        else -> d.monthValue.toString() + "月" + d.dayOfMonth + "日"
    }
}
