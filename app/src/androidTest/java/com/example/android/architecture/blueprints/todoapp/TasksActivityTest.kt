package com.example.android.architecture.blueprints.todoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TasksActivityTest {

    private lateinit var repository: TasksRepository

    @Before
    fun setUp() {
        repository = ServiceLocator.provideTasksRepository(
                getApplicationContext())
        runBlocking {
            repository.deleteAllTasks()
        }
    }

    @After
    fun tearDown(){
        ServiceLocator.resetRepository()
    }

    @Test
    fun editTask() = runBlocking{
        //set initial state
        repository.saveTask(Task("Title1", "Desc"))

        //start up Tasks Screen
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)

        //Espresso code

        //call aS.close() before resetting db
        activityScenario.close()
    }
}