package com.example.appcommerceclone.util

import androidx.annotation.IdRes
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import com.example.appcommerceclone.R
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class TestNavHostControllerRule(@IdRes private val currentDestination: Int) : TestWatcher() {

    private lateinit var testNavHostController: TestNavHostController

    override fun starting(description: Description) {
        testNavHostController = TestNavHostController(ApplicationProvider.getApplicationContext())
        testNavHostController.setGraph(R.navigation.navigation_graph)
        testNavHostController.setCurrentDestination(currentDestination)
    }

    fun findTestNavHostController(): TestNavHostController {
        return testNavHostController
    }
}