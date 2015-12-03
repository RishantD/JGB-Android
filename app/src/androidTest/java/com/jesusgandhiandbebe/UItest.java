package com.jesusgandhiandbebe;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;
import android.test.ActivityUnitTestCase;
import android.widget.EditText;

import com.facebook.FacebookSdk;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static java.util.EnumSet.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 * Created by Vishal on 12/2/15.
 */
public class UItest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;

    public UItest(){
        super(MainActivity.class);

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
    public void testCardList() throws Exception{
//        onData(allOf(is(instanceOf(Map.class)),
//                hasEntry(equalTo(MainActivity.ROW_TEXT), is("party at my place"))));
//        FacebookSdk.sdkInitialize(getActivity());
//
        onView(withId(R.id.lobby_list)).perform(ViewActions.click());
        onView(withId(R.id.activity_main_swipe_refresh_layout)).perform(swipeDown());
        onView(withId(R.id.fab)).perform(click());
    }

}
