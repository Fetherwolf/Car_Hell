package com.arg.hmmm.carhell;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Switch;

import java.util.ArrayList;

public class MusicService extends Service {

	@SuppressWarnings("unused")
	private final String TAG = "MusicService";
	private static MediaPlayer mPlayer;
	private static ArrayList<MediaPlayer> musics;
	private int mStartID;
	private static String state;
	
	HeadsetIntentReceiver receiver = new HeadsetIntentReceiver();

	@Override
	public void onCreate() {
		super.onCreate();

		// Set up the Media Player.

		IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
		registerReceiver(receiver, receiverFilter);
	}

	public static void changeMusic(String music) {
		pauseAll();
		boolean samemusic = false;
		switch(music){
			case "manu":
				if(mPlayer == musics.get(0))
					samemusic = true;
				else
					mPlayer = musics.get(0);
				break;
			case "level":
				if(mPlayer == musics.get(1))
					samemusic = true;
				else
					mPlayer = musics.get(1);
				break;
			default:
				mPlayer = musics.get(0);
		}
		mPlayer.start();
		if(!samemusic)
			mPlayer.seekTo(0);
	}

	public static void createmusics(Context context){
		musics = new ArrayList<>();
        MediaPlayer temp;
		/*
		0-> menu
		1-> game
		 */

		temp = MediaPlayer.create(context, R.raw.bitmenu);
		temp.setLooping(true);
		musics.add(temp);

		temp = MediaPlayer.create(context, R.raw.bitmusic);
		temp.setLooping(true);
		musics.add(temp);
	}

	public void setLowVolume(){
		Music.setVolume(0.2f, 0.2f);
	}

	public void resetLowVolume(){
		Music.setVolume(1f, 1f);
	}

	public static void pauseAll() {
		for (MediaPlayer mediaPlayer : musics)
			if (mediaPlayer.isPlaying())
				mediaPlayer.pause();
	}

	public static void setVolume(float leftVolume, float rightVolume) {
		for (MediaPlayer mediaPlayer : musics)
			mediaPlayer.setVolume(leftVolume, rightVolume);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		/*if (mPlayer != null) {
			// ID for this start command
			mStartID = startid;

			if (mPlayer.isPlaying()) {
				// Rewind to beginning of song
				mPlayer.seekTo(0);
			} else {
				// Start playing song
				mPlayer.start();
			}
		}*/

		// Don't automatically restart this Service if it is killed
		return START_NOT_STICKY;
	}

	@Override
	public void onDestroy() {
		pauseAll();
	}

	// Can't bind to this Service
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	

//	@Override
//	protected void onStart() {
//		super.onStart();
//		IntentFilter receiverFilter = new IntentFilter(
//				Intent.ACTION_HEADSET_PLUG);
//		registerReceiver(receiver, receiverFilter);
//	}

	
//	@Override
//	protected void onStop() {
//		super.onStop();
//		unregisterReceiver(receiver);
//	}

    public static void setState(String s){
	    state = s;
    }

	private void updateState(String state) {
		// textViewHeadsetState.setText(state);
		//Toast.makeText(this, state, Toast.LENGTH_LONG).show();
		if(state.equals("Plugged")){
			setLowVolume();
		}else{
			resetLowVolume();
		}
	}

	public class HeadsetIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
				int state = intent.getIntExtra("state", -1);
				switch (state) {
					case (0):
						updateState("Unplugged");
						break;
					case (1):
						updateState("Plugged");
						break;
					default:
						updateState("Error");
				}
			}
		}
	}

	/*
	musicServiceIntent = new Intent(getApplicationContext(),
                com.arg.hmmm.carhell.MusicService.class);
        startService(musicServiceIntent);
	 */



}
