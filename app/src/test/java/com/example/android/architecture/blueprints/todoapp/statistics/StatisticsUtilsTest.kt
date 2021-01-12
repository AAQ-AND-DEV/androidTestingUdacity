package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StatisticsUtilsTest : TestCase() {

    //testing one active, zero completed tasks
    @Test
    fun testGetActiveAndCompletedStats() {
        //Given: single Active Task
        val tasks = listOf<Task>(
                Task("task", "description", false))
        //When: getStats called
        val result = getActiveAndCompletedStats(tasks)
        //Then: 100% active, 0% completed
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(100f))
    }
    //testing one active, zero completed tasks
    @Test
    fun testGetActiveAndCompletedStats_nullTasks() {
        //Given: null tasksList
        val tasks = null
        //When: getStats called
        val result = getActiveAndCompletedStats(tasks)
        //Then: 0% for completed, and active
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
    //testing one active, zero completed tasks
    @Test
    fun testGetActiveAndCompletedStats_EmptyList() {
        //Given: empty TaskList
        val tasks = emptyList<Task>()
        //When: getStats called
        val result = getActiveAndCompletedStats(tasks)
        //Then: 0% for completed, and active
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
    //testing one active, zero completed tasks
    @Test
    fun testGetActiveAndCompletedStatsNullActive() {
        //Given: one active, zero completed tasksList
        val tasks = listOf<Task>(
                Task("task", "description", true))
        //When: getStats called
        val result = getActiveAndCompletedStats(tasks)
        //Then: 0% active, 100% completed
        assertThat(result.completedTasksPercent, `is`(100f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
    //testing 3 active, 2 completed tasks
    @Test
    fun testGetActiveAndCompletedStats4060() {
        //Given: 2 completed tasks, 3 active tasks
        val tasks = listOf<Task>(
                Task("task", "description", true),
                Task("task", "description", true),
                Task("task", "description", false),
                Task("task", "description", false),
                Task("task", "description", false)
        )
        //When: getStats called
        val result = getActiveAndCompletedStats(tasks)
        //Then: 40% completed, 60% active
        assertThat(result.completedTasksPercent, `is`(40f))
        assertThat(result.activeTasksPercent, `is`(60f))
    }
}