package com.example.sweetreminder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.sweetreminder.data.ComplimentDatabase
import com.example.sweetreminder.ui.theme.SweetReminderTheme
import com.example.sweetreminder.work.ReminderWorker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Schedule periodic worker
        ReminderWorker.schedule(this)

        val dao = ComplimentDatabase.instance(this).dao()

        setContent {
            SweetReminderTheme {
                val history = remember { mutableStateListOf<com.example.sweetreminder.data.Compliment>() }
                LaunchedEffect(Unit) {
                    lifecycleScope.launch {
                        dao.history().collectLatest {
                            history.clear(); history.addAll(it)
                        }
                    }
                }

                Scaffold(
                    topBar = { SmallTopAppBar(title = { Text("SweetReminder") }) }
                ) { inner ->
                    LazyColumn(
                        contentPadding = inner,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(history.size) { idx ->
                            val item = history[idx]
                            ListItem(
                                headlineContent = { Text(item.content) },
                                supportingContent = {
                                    val ts = Instant.ofEpochMilli(item.timestamp)
                                        .atZone(ZoneId.systemDefault())
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                                    Text("${item.theme} · $ts")
                                }
                            )
                            Divider(Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }
        }
    }
}
