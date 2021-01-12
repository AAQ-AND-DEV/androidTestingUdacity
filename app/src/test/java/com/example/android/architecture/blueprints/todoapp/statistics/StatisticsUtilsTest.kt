package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import junit.framework.TestCase
import org.junit.Test

class StatisticsUtilsTest : TestCase() {

    //testing one active, zero completed tasks
    @Test
    fun testGetActiveAndCompletedStats() {
        val tasks = listOf<Task>(
                Task("task", "description", false))

        val result = getActiveAndCompletedStats(tasks)
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(100f, result.activeTasksPercent)
    }
    //testing one active, zero completed tasks
    @Test
    fun testGetActiveAndCompletedStatsNullActive() {
        val tasks = listOf<Task>(
                Task("task", "description", true))

        val result = getActiveAndCompletedStats(tasks)
        assertEquals(100f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }
    //testing 3 active, 2 completed tasks
    @Test
    fun testGetActiveAndCompletedStats4060() {
        val tasks = listOf<Task>(
                Task("task", "description", true),
                Task("task", "description", true),
                Task("task", "description", false),
                Task("task", "description", false),
                Task("task", "description", false)
        )

        val result = getActiveAndCompletedStats(tasks)
        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)
    }
}