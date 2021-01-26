package com.example.timertest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private val TAG = "TTTTTTTTTTTT"
    private val CRASH_TIME_QUEUE_DATA = "CrashtimeQueueData"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.reportCrash).setOnClickListener {
            if (isExceptionsCountInRage(3, 10)) {
                //Log.d("Reporting", "sending report")
            } else {
                //Log.d("Reporting", "ignore report")
            }
        }
    }

    private fun isExceptionsCountInRage(count: Int, timeInSeconds: Int): Boolean {
        val gson = Gson()
        val sharedPreference = SharedPrefsHelper.getInstance(this)
        val queueString = sharedPreference.getString(CRASH_TIME_QUEUE_DATA)

        queueString?.let {
            val type = object : TypeToken<Queue<Long>>() {}.type
            val queue = gson.fromJson<Queue<Long>>(queueString, type)
            Log.d("Reporting","queueItems")
            queue.iterator().forEach {
                Log.d("Reporting", getDateWithSeconds(it))
            }

            if (queue.size >= count) {
                queue.element()?.let { firstCapturedTimeInQueue ->
                    val currentTime = System.currentTimeMillis()
                    val differenceTimeInMins = TimeUnit.MILLISECONDS.toSeconds(currentTime - firstCapturedTimeInQueue)
                    return if (differenceTimeInMins < timeInSeconds) {
                        Log.d("Reporting", "ignoring report at " +getDateWithSeconds(currentTime))
                        false
                    } else {
                        queue.remove()
                        queue.add(currentTime)
                        sharedPreference.putString(CRASH_TIME_QUEUE_DATA, gson.toJson(queue))
                        Log.d("Reporting", "sending report at " +getDateWithSeconds(currentTime))
                        true
                    }
                }
            } else {
                val currentTime = System.currentTimeMillis()
                queue.add(currentTime)
                sharedPreference.putString(CRASH_TIME_QUEUE_DATA, gson.toJson(queue))
                Log.d("Reporting", "sending report at " +getDateWithSeconds(currentTime))
                return true
            }
        } ?: kotlin.run {
            val queue: Queue<Long> = LinkedList(emptyList())
            val currentTime = System.currentTimeMillis()
            queue.add(currentTime)
            sharedPreference.putString(CRASH_TIME_QUEUE_DATA, gson.toJson(queue))
            Log.d("Reporting", "sending report at " +getDateWithSeconds(currentTime))
            return true
        }

    }

    fun getDateWithSeconds(milliSeconds: Long): String {
        val dateFormat = "dd/MM/yyyy hh:mm:ss"
        val formatter = SimpleDateFormat(dateFormat)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }
}