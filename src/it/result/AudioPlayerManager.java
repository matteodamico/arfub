package it.result;

import java.io.IOException;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.Toast;

public class AudioPlayerManager {

	
	public AudioPlayerManager(){}

	public void prepare(MediaPlayer mediaPlayer,String url) {
		mediaPlayer.reset();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
	        mediaPlayer.setDataSource(url);
	        mediaPlayer.prepareAsync();

	    } catch (IllegalArgumentException e) {
	        //Toast.makeText(AudioPlayerManager.this,getResources().getString(R.string.erreurIllegalArgument),Toast.LENGTH_LONG).show();
	        e.printStackTrace();
	    } catch (IllegalStateException e) {
	       
	        e.printStackTrace();
	    } catch (IOException e) {
	      
	        e.printStackTrace();
	    }
	}
	
	
}
