package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import getOrAwaitValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.hamcrest.core.IsNull.nullValue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun addNewTask_setsNewTaskEvent() {
        //Given: a fresh TasksViewModel
        val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
        //When: adding a new task
        tasksViewModel.addNewTask()
        //Then: the new task event is triggered
        val res = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(res.getContentIfNotHandled(),(not(nullValue())))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible(){
        //Given: a fresh ViewModel
        val tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
        //When: filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        //Then: "Add task" action is visible
        val res = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(res, `is`(true))
    }
}