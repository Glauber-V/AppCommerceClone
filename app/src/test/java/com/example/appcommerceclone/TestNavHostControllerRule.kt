package com.example.appcommerceclone

import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class TestNavHostControllerRule(private val currentDestination: Int) : TestWatcher() {

    private lateinit var testNavHostController: TestNavHostController

    override fun starting(description: Description) {
        testNavHostController = TestNavHostController(ApplicationProvider.getApplicationContext())
        testNavHostController.setGraph(R.navigation.navigation_graph)
        testNavHostController.setCurrentDestination(currentDestination)
        super.starting(description)
    }

    fun findTestNavHostController(): TestNavHostController {
        return testNavHostController
    }
}