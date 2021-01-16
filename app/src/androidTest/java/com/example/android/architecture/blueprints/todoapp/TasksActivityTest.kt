package com.example.android.architecture.blueprints.todoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import com.example.android.architecture.blueprints.todoapp.util.DataBindingIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import com.example.android.architecture.blueprints.todoapp.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TasksActivityTest {

    private lateinit var repository: TasksRepository

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setUp() {
        repository = ServiceLocator.provideTasksRepository(
                getApplicationContext())
        runBlocking {
            repository.deleteAllTasks()
        }
    }

    @Before
    fun registerIdlingResources(){
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResources(){
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
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
        dataBindingIdlingResource.monitorActivity(activityScenario)

        //clickOnTask, verify data correct
        onView(withText("Title1")).perform(click())
        onView(withId(R.id.task_detail_title_text)).check(matches(withText("Title1")))
        onView(withId(R.id.task_detail_description_text)).check(matches(withText("Desc")))
        onView(withId(R.id.task_detail_complete_checkbox)).check(matches(not(isChecked())))

        //click on edit button, edit and save
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("New Title"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("New Desc"))
        onView(withId(R.id.save_task_fab)).perform(click())

        //verify task is displayed on sceen in task list
        onView(withText("New Title")).check(matches(isDisplayed()))
        //verify prev task not displayed
        onView(withText("Title1")).check(doesNotExist())

        //call aS.close() before resetting db
        activityScenario.close()
    }

    @Test
    fun createOneTask_deleteTask() {

        // 1. Start TasksActivity.
        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        // 2. Add an active task by clicking on the FAB and saving a new task.
        onView(withId(R.id.add_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("new task"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("great desc"))
        onView(withId(R.id.save_task_fab)).perform(click())

        // 3. Open the new task in a details view.
        onView(withText("new task")).perform(click())
        // 4. Click delete task in menu.
        onView(withId(R.id.menu_delete)).perform(click())
        // 5. Verify it was deleted.
        //these next two steps were in solution code, but
        //i don't think they're necessary, are they?
        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())
        onView(withText("new task")).check(doesNotExist())
        // 6. Make sure the activity is closed.
        activityScenario.close()
    }
}