package com.arg.hmmm.carhell;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;
import java.util.List;

public class Music {
    private static List<MediaPlayer> music;
    public static MediaPlayer menu;
    public static MediaPlayer level;

    public static void init(Context context) {
        music = new ArrayList<>();
        menu = MediaPlayer.create(context, R.raw.bitmenu);
        menu.setLooping(true);
        music.add(menu);
        level = MediaPlayer.create(context, R.raw.bitmusic);
        level.setLooping(true);
        music.add(level);
    }

    public static void pauseAll() {
        for (MediaPlayer mediaPlayer : music)
            if (mediaPlayer.isPlaying())
                mediaPlayer.pause();
    }

    public static void setVolume(float leftVolume, float rightVolume) {
        for (MediaPlayer mediaPlayer : music)
            mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    //neeed to do init in the first activity that opens
    //Music.init(this);
}
