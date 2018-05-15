package com.linh.androidpermissionhandler;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;
import com.linh.androidpermissionhandler.constant.Constant;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4.class)
public class ComplexSampleInstrumentedTest {
    private UiDevice device;

    @Before
    public void before() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Rule
    public ActivityTestRule<ComplexSampleActivity> mActivityRule =
            new ActivityTestRule<>(ComplexSampleActivity.class);

    @Test
    public void testGrantedSinglePermission() throws Exception {
        onView(withId(R.id.button_test_granted_single_permission)).perform(click());
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
        }
        if (device.findObject(new UiSelector().text("ALLOW")).exists()) {
            device.findObject(new UiSelector().text("ALLOW")).click();
        }
        onView(withId(R.id.text_result)).check(matches(withText(Constant.ALL_PERMISSION_GRANTED)));
    }

    @Test
    public void testDeniedSinglePermission() throws Exception {
        onView(withId(R.id.button_test_denied_single_permission)).perform(click());
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
        }
        if (device.findObject(new UiSelector().text("DENY")).exists()) {
            device.findObject(new UiSelector().text("DENY")).click();
        }
        if (device.findObject(new UiSelector().text("CANCEL")).exists()) {
            onView(withText("CANCEL")).perform(click());
        }
        onView(withId(R.id.text_result)).check(matches(withText(Constant.ALL_PERMISSION_DENIED)));
    }

    @Test
    public void testDeniedSinglePermissionWithRetry() throws Exception {
        onView(withId(R.id.button_test_denied_single_permission)).perform(click());
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
        }
        device.findObject(new UiSelector().text("DENY")).click();
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
            device.findObject(new UiSelector().text("DENY")).click();
            onView(withId(R.id.text_result)).check(
                    matches(withText(Constant.ALL_PERMISSION_DENIED)));
        } else {
            onView(withId(R.id.text_result)).check(
                    matches(withText(Constant.ALL_PERMISSION_DENIED)));
        }
    }

    @Test
    public void testGrantedMultiplePermission() throws Exception {
        onView(withId(R.id.button_test_granted_multiple_permission)).perform(click());
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
        }
        while (device.findObject(new UiSelector().text("ALLOW")).exists()) {
            device.findObject(new UiSelector().text("ALLOW")).click();
        }
        onView(withId(R.id.text_result)).check(matches(withText(Constant.ALL_PERMISSION_GRANTED)));
    }

    @Test
    public void testDeniedMultiplePermission() throws Exception {
        onView(withId(R.id.button_test_denied_multiple_permission)).perform(click());
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
        }
        while (device.findObject(new UiSelector().text("DENY")).exists()) {
            device.findObject(new UiSelector().text("DENY")).click();
        }
        if (device.findObject(new UiSelector().text("CANCEL")).exists()) {
            onView(withText("CANCEL")).perform(click());
        }
        onView(withId(R.id.text_result)).check(matches(withText(Constant.ALL_PERMISSION_DENIED)));
    }

    @Test
    public void testDeniedMultiplePermissionWithRetry() throws Exception {
        onView(withId(R.id.button_test_denied_multiple_permission)).perform(click());
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
        }
        while (device.findObject(new UiSelector().text("DENY")).exists()) {
            device.findObject(new UiSelector().text("DENY")).click();
        }
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
            while (device.findObject(new UiSelector().text("DENY")).exists()) {
                device.findObject(new UiSelector().text("DENY")).click();
            }
        }
        onView(withId(R.id.text_result)).check(matches(withText(Constant.ALL_PERMISSION_DENIED)));
    }
}
