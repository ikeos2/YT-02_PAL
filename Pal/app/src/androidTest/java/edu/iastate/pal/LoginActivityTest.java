//package edu.iastate.pal;
//
//
//import android.support.test.espresso.ViewInteraction;
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//import android.test.suitebuilder.annotation.LargeTest;
//
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.replaceText;
//import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static android.support.test.espresso.matcher.ViewMatchers.withParent;
//import static android.support.test.espresso.matcher.ViewMatchers.withText;
//import static org.hamcrest.Matchers.allOf;
//
//@LargeTest
//@RunWith(AndroidJUnit4.class)
//public class LoginActivityTest {
//
//    @Rule
//    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
//
//    @Test
//    public void loginActivityTest() {
//        ViewInteraction appCompatEditText = onView(
//                allOf(withId(R.id.activity_login_edit_text_username),
//                        withParent(allOf(withId(R.id.activity_login_shell),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatEditText.perform(click());
//
//        ViewInteraction appCompatEditText2 = onView(
//                allOf(withId(R.id.activity_login_edit_text_username),
//                        withParent(allOf(withId(R.id.activity_login_shell),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatEditText2.perform(replaceText("evan"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText3 = onView(
//                allOf(withId(R.id.activity_login_edit_text_password),
//                        withParent(allOf(withId(R.id.activity_login_shell),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatEditText3.perform(replaceText("lambert"), closeSoftKeyboard());
//
//        ViewInteraction appCompatButton = onView(
//                allOf(withId(R.id.activity_login_button_login), withText("Login"),
//                        withParent(allOf(withId(R.id.activity_login_shell),
//                                withParent(withId(android.R.id.content)))),
//                        isDisplayed()));
//        appCompatButton.perform(click());
//    }
//
//}
