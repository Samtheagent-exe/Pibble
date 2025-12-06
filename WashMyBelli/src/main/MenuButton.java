package main;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;

/**
 * A custom JButton class that enforces consistent styling for all menu buttons.
 */
public class MenuButton extends JButton {
    
    // Define shared styling properties once
    private static final Font MENU_FONT = new Font("SansSerif", Font.BOLD, 40);
    private static final Color LIGHT_PINK = new Color(255, 105, 190);

    /**
     * Creates a new styled menu button.
     * @param text The text to display on the button.
     * @param x The x-coordinate for the button position.
     * @param y The y-coordinate for the button position.
     * @param width The width of the button.
     * @param height The height of the button.
     */
    public MenuButton(String text, int x, int y, int width, int height) {
        // Call the parent JButton constructor with the text
        super(text);

        // Apply all the styling in one place
        this.setBounds(x, y, width, height);
        this.setBackground(LIGHT_PINK);
        this.setFont(MENU_FONT);
        this.setBorderPainted(false);
    }
}