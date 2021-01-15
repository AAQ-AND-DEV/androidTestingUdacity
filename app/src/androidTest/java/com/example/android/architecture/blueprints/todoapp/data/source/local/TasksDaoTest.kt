package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TasksDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db : ToDoDatabase

    @Before
    fun setupDb(){
        db = Room.inMemoryDatabaseBuilder(
                getApplicationContext(),
                ToDoDatabase::class.java
        ).build()
    }

    @After
    fun tearDownDb() = db.close()

    @Test
    fun insertTaskAndGetById() = runBlockingTest {
        //GIVEN: insert a task
        val task = Task("title", "desc")
        db.taskDao().insertTask(task)

        //WHEN: get task by id
        val loaded = db.taskDao().getTaskById(task.id)

        //THEN: loaded data contains expected values
        assertThat<Task>(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(task.id))
        assertThat(loaded.title, `is`(task.title))
        assertThat(loaded.description, `is`(task.description))
        assertThat(loaded.isCompleted, `is`(task.isCompleted))
    }

    @Test
    fun updateTaskAndGetById() = runBlockingTest {
        // Given: Insert a task into the DAO.
        val task = Task("task", "desc")
        db.taskDao().insertTask(task)

        // When: Update the task by creating a new task with the same ID but different attributes.
        val newTask = Task("taskUpdate", "new desc", id = task.id, isCompleted = true)
        db.taskDao().updateTask(newTask)

        // Then:  task retrieved by orig ID has the updated values.
        val loaded = db.taskDao().getTaskById(task.id)

        //THEN: loaded data contains expected values
        assertThat<Task>(loaded as Task, notNullValue())
        assertThat(loaded.id, `is`(newTask.id))
        assertThat(loaded.title, `is`(newTask.title))
        assertThat(loaded.description, `is`(newTask.description))
        assertThat(loaded.isCompleted, `is`(newTask.isCompleted))
    }

}