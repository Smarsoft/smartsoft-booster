package io.smartsoft.booster;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by bartoszjarocki on 07/07/14.
 */
public class ActivityCallbacks implements Application.ActivityLifecycleCallbacks {

    public ActivityCallbacks() {

    }

    @Override public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override public void onActivityStarted(Activity activity) {
    }

    @Override public void onActivityResumed(Activity activity) {
    }

    @Override public void onActivityPaused(Activity activity) {
    }

    @Override public void onActivityStopped(Activity activity) {
    }

    @Override public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override public void onActivityDestroyed(Activity activity) {
    }
}
