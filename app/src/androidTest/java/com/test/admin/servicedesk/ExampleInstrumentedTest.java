package com.test.admin.servicedesk;

import android.content.Context;

import com.test.admin.servicedesk.Activity.AuthActivity;
import com.test.admin.servicedesk.Activity.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<AuthActivity> activityTestRule = new ActivityTestRule<>(AuthActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.test.admin.servicedesk", appContext.getPackageName());
    }

    @Test
    public void userAuthentication(){
        onView(withId(R.id.login)).perform(clearText());
        onView(withId(R.id.password)).perform(clearText());
        onView(withId(R.id.login)).perform(typeText("tmpKYAnikin"));
        onView(withId(R.id.password)).perform(typeText("WHOa1402wR"));
        onView(withId(R.id.loginButton)).perform(click());
    }

}
