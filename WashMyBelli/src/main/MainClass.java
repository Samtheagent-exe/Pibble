package main;

public class MainClass {

	public static void main(String[] args) {
		
		//start all
		SoundManager soundManager= new SoundManager();
		
		new UIManager(soundManager);
		
	}
}