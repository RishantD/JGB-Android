package com.jesusgandhiandbebe;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.widget.EditText;

import com.facebook.FacebookSdk;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 *
 * Created by Vishal on 12/3/15.
 */
public class CreatLobbyUITest extends ActivityInstrumentationTestCase2<CreateLobbyActivity> {

    private CreateLobbyActivity mActivity;

    public CreatLobbyUITest(){
        super(CreateLobbyActivity.class);

    }


    @Before
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mActivity = getActivity();
        FacebookSdk.sdkInitialize(mActivity);
    }


    @Test
    public void testChangeText_sameActivity() throws Exception{
        FacebookSdk.sdkInitialize(getActivity());

        // Type text and then press the button.
        onView(withId(R.id.event_name))
                .perform(typeText("CS 242 Party"), closeSoftKeyboard());
        //onView(withId(R.id.event_day)).perform(click());
        onView(withId(R.id.create_event)).perform(click());

        // Check that the text was changed.
        onView(withId(R.id.event_name)).check(matches(withText("CS 242 Party")));
    }

}
