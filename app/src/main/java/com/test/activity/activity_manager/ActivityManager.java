package com.test.activity.activity_manager;

import android.app.Activity;

import java.util.ArrayList;

/**
 * Created by hasse on 2020/1/8.
 */

public class ActivityManager {

    private static ArrayList<Activity> activities = new ArrayList();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void delActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAllActivity() {
        for (int i = 0; i < activities.size(); i++) {
            //// TODO: 2020/1/8 在关闭中状态的条件，没有添加
            if (!activities.get(i).isFinishing()) {
                activities.get(i).finish();
            }
        }
    }
}
