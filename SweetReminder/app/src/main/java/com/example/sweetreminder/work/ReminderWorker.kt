package com.example.sweetreminder.work
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import com.example.sweetreminder.MainActivity
import com.example.sweetreminder.R
import com.example.sweetreminder.data.Compliment
import com.example.sweetreminder.data.ComplimentDatabase
import com.example.sweetreminder.data.PreferencesRepository
import com.example.sweetreminder.logic.ComplimentGenerator
import java.util.concurrent.TimeUnit

class ReminderWorker(
    ctx: Context,
    params: WorkerParameters
) : CoroutineWorker(ctx, params) {

    companion object {
        private const val CHANNEL_ID = "love_channel"
        private const val NOTI_ID = 1001
        private const val UNIQUE_NAME = "periodic_reminder"

        fun schedule(context: Context, intervalHours: Int = 3) {
            val request = PeriodicWorkRequestBuilder<ReminderWorker>(
                intervalHours.toLong(), TimeUnit.HOURS
            ).setInitialDelay(intervalHours.toLong(), TimeUnit.HOURS).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
        }
    }

    override suspend fun doWork(): Result {
        val repo = PreferencesRepository(applicationContext)
        val lastTheme = repo.lastTheme()
        val (theme, content) = ComplimentGenerator.next(lastTheme)
        repo.saveLastTheme(theme)

        ComplimentDatabase.instance(applicationContext)
            .dao()
            .insert(Compliment(theme = theme, content = content))

        showNotification(content)
        return Result.success()
    }

    private fun showNotification(text: String) {
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= 26 && nm.getNotificationChannel(CHANNEL_ID) == null) {
            nm.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_ID, "Love Reminders",
                    NotificationManager.IMPORTANCE_DEFAULT
                )
            )
        }
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notif = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_favorite)
            .setContentTitle("到了夸夸老婆的时间啦！")
            .setContentText(text.take(40))
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setContentIntent(pi)
            .setAutoCancel(true)
            .build()

        nm.notify(NOTI_ID, notif)
    }
}
