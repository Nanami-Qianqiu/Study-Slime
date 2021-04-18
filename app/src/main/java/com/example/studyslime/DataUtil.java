package com.example.studyslime;

import android.content.Context;
import android.content.SharedPreferences;

public class DataUtil {
    private static final String fileName = "user_data";
    public static final String testData_name = "testData";

    //Save single data
    public static void saveUserData(Context context, String dataName, int dataValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(testData_name, dataValue);
        editor.apply();
    }

    //Get data class
    public static UserData getUserData(Context context) {
        UserData userData = null;
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        userData = new UserData();
        userData.setTestData(sp.getInt(testData_name, 0));
        return userData;
    }
}
