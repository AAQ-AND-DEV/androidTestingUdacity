package com.example.android.architecture.blueprints.todoapp.tasks

import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailFragment
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailFragmentArgs
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailFragmentDirections
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest{

    private lateinit var repository: TasksRepository

    @Before
    fun initRepository(){
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest{
        ServiceLocator.resetRepository()
    }

    @Test
    fun clickTask_navigateToDetailFragmentOne() = runBlockingTest {
        repository.saveTask(Task("Title1", "Desc1", false, "id1"))
        repository.saveTask(Task("Title2", "Desc2", true, "id2"))

        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(),
        R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment{
            Navigation.setViewNavController(it.view!!, navController)
        }
        //WHEN: Click on first list item
        onView(withId(R.id.tasks_list))
                .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                        hasDescendant(withText("Title1")), click()))

        //THEN: verify that we navigate to first detail screen
        verify(navController).navigate(
                TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment("id1")
        )
    }

    @Test
    fun clickAddTaskButton_navigateToAddEditFragment() = runBlockingTest{
        val task = Task("Title1", "Desc1", false, "id1")
        repository.saveTask(task)
        //GIVEN: in TasksFragment
        val scenario = launchFragmentInContainer<TasksFragment>(Bundle(), R.style.AppTheme)
        val navController = mock(NavController::class.java)
        scenario.onFragment{
            Navigation.setViewNavController(it.view!!, navController)
        }
        //WHEN: click on addNewTask Button
        onView(withId(R.id.add_task_fab))
                .perform(click())
        //THEN: verify that we navigate to addEditTaskFragment(with null taskId, New Task title)
        verify(navController).navigate(
                TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(null,"New Task")
        )
    }
}