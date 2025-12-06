package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager{
	private Clip clip;
	private static boolean isMuted;
	
	public SoundManager(){
		try {
            // Load the audio from resources
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(getClass().getResource("/res/The Pibble Theme.wav"));

            clip = AudioSystem.getClip();
            clip.open(audioStream);

        } catch (Exception e) {
            System.out.println("Failed to load music:");
            e.printStackTrace();
        }
		
		isMuted = false;
		playMusic();
	}
	
	public void playMusic() {
		clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        isMuted = false;
	}
	
	public void endMusic() {
		clip.stop();
		isMuted = true;
	}
	public void toggleMute() {
		if (isMuted) {
			playMusic();
		} else endMusic();
	}
	
	public boolean muted() {
		return isMuted;
	}
}