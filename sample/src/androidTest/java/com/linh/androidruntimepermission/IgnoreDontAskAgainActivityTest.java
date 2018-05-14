package com.linh.androidruntimepermission;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class IgnoreDontAskAgainActivityTest {
    private UiDevice device;

    @Before
    public void before() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Rule
    public ActivityTestRule<IgnoreDontAskAgainActivity> mActivityRule =
            new ActivityTestRule<>(IgnoreDontAskAgainActivity.class);

    @Test
    public void testGrantedAllPermissions() throws Exception {
        Utils.deleteCache(mActivityRule.getActivity().getApplicationContext());
        onView(withId(R.id.button_request)).perform(click());
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
        }
        while (device.findObject(new UiSelector().text("ALLOW")).exists()) {
            device.findObject(new UiSelector().text("ALLOW")).click();
        }
        onView(withId(R.id.text_status)).check(matches(withText(Constant.ALL_PERMISSION_GRANTED)));
    }

    @Test
    public void testDeniedAllPermissions() throws Exception {
        // Delete local cache dir (ignoring any errors):
        onView(withId(R.id.button_request)).perform(click());
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
        }
        while (device.findObject(new UiSelector().text("DENY")).exists()) {
            device.findObject(new UiSelector().text("DENY")).click();
        }
        if (device.findObject(new UiSelector().text("OK")).exists()) {
            onView(withText("OK")).perform(click());
        }
        while (device.findObject(new UiSelector().text("DENY")).exists()) {
            device.findObject(new UiSelector().text("DENY")).click();
        }
        onView(withId(R.id.text_status)).check(matches(withText(Constant.ALL_PERMISSION_DENIED)));
    }
}