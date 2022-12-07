package com.example.appcommerceclone.util

import androidx.test.espresso.idling.CountingIdlingResource


/** In situations where Espresso cannot tell whether the app is busy updating the UI or not,
 *  you can use the CountingIdlingResource class, which allows you to increment and decrement a counter such that:
 *  When the counter is greater than zero, the app is considered working.
 *  When the counter is zero, the app is considered idle.
 *  To simplify the usage you can use the inline function wrapEspressoIdlingResource() */
object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    @JvmField
    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    /** Set app as working */
    fun increment() {
        countingIdlingResource.increment()
    }

    /** Set app as idle */
    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }

    /** Run long time tasks inside this wrapper to let Espresso know
     * when the app is busy or idle */
    inline fun <T> wrapEspressoIdlingResource(function: () -> T): T {
        EspressoIdlingResource.increment()
        return try {
            function()
        } finally {
            EspressoIdlingResource.decrement()
        }
    }
}