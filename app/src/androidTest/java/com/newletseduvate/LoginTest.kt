package com.newletseduvate

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.newletseduvate.ui.login.LoginActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class LoginTest {
    @get:Rule
    var loginActivity = ActivityScenarioRule(
        LoginActivity::class.java
    )

    @Test
    fun performLoginTest() {

//        val username = "2000000950"
//        val password = "2000000950"

        val username = "2103450003"
        val password = "2103450003"

        Espresso.onView(withId(R.id.editText_login_username))
            .perform(ViewActions.typeText(username))

        Espresso.onView(withId(R.id.editText_login_password))
            .perform(ViewActions.typeText(password))

        Espresso.onView(withId(R.id.button_login))
            .perform(ViewActions.click())

    }
}