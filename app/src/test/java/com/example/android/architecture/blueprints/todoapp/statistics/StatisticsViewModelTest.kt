package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoRoutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsViewModelTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoRoutineRule()

    private lateinit var statsViewModel: StatisticsViewModel
    private lateinit var tasksRepository: FakeTestRepository

    @Before
    fun setUpViewModel(){
        tasksRepository = FakeTestRepository()

        statsViewModel = StatisticsViewModel(tasksRepository)
    }

    @Test
    fun loadTasks_loading(){
        //pause dispatcher so we can verify initial values.
        mainCoroutineRule.pauseDispatcher()
        //Load task in viewModel
        statsViewModel.refresh()

        //assert progress indicator is shown.
        assertThat(statsViewModel.dataLoading.getOrAwaitValue(), `is`(true))

        //execute pending coroutines actions.
        mainCoroutineRule.resumeDispatcher()
        //assert progress indicator is hidden
        assertThat(statsViewModel.dataLoading.getOrAwaitValue(), `is`(false))
    }

}

