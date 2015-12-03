package com.jesusgandhiandbebe;

import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;

import java.util.Map;

import static android.support.test.espresso.Espresso.onData;
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

    private MainActivity mainActivity;

    public UItest(Class<MainActivity> activityClass) {
        super(activityClass);
    }

    @Before
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mainActivity = getActivity();
    }

    public void testCardList() {
//        onData(allOf(is(instanceOf(Map.class)),
//                hasEntry(equalTo(MainActivity.ROW_TEXT), is("party at my place"))));
    }
}
