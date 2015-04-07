package io.smartsoft.booster.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by bartoszjarocki on 27/03/15.
 */
public class KeyboardUtils {
    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                activity.findViewById(android.R.id.content).getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
