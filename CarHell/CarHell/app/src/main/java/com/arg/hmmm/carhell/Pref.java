package com.arg.hmmm.carhell;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

public class Pref {
    private static SharedPreferences pref;

    public static void createPref(Context context) {
        pref = context.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
    }

    public Pref() {
    }

    public static float getHighScore() {
        return pref.getFloat("score", 0);
    }

    public static void setHighScore(float score) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putFloat("score", score);
        edit.commit();
    }

    public static void setBigButtons(boolean b) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("bigButtons", b);
        edit.commit();
    }

    public static boolean getBigButtons() {
        return pref.getBoolean("bigButtons", false);
    }

    public static void setPlayerImage(String s) {
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("playerImage", s);
        edit.commit();
    }

    public static String getPlayerImage() {
        return pref.getString("playerImage", "yellow");
    }

    public static void SetVolumeState(boolean b){
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("volumeState" , b);
        edit.commit();
    }

    public static boolean getVolumeState(){
        return pref.getBoolean("volumeState", true);
    }

}



