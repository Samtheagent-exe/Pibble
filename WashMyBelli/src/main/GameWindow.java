package main;

import java.awt.Component;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow {
	
	//JFrame to create a window
	private JFrame jframe;
	public GameWindow(UIManager menuPanel) {
		
		jframe = new JFrame("Wash Ma Belli!");
		
		jframe.setSize(1280, 720);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setLocationRelativeTo(null);
        jframe.setResizable(false);

        jframe.add(menuPanel);          // <<----- IMPORTANT
        jframe.setVisible(true);
	}
}