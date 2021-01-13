package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import getOrAwaitValue
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.hamcrest.core.IsNull.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var tasksViewModel: TasksViewModel

    @Before
    fun setupViewModel(){
        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        //Given: a fresh TasksViewModel

        //When: adding a new task
        tasksViewModel.addNewTask()
        //Then: the new task event is triggered
        val res = tasksViewModel.newTaskEvent.getOrAwaitValue()
        assertThat(res.getContentIfNotHandled(),(not(nullValue())))
    }

    @Test
    fun setFilterAllTasks_tasksAddViewVisible(){
        //Given: a fresh ViewModel
        //When: filter type is ALL_TASKS
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)
        //Then: "Add task" action is visible
        val res = tasksViewModel.tasksAddViewVisible.getOrAwaitValue()
        assertThat(res, `is`(true))
    }
}