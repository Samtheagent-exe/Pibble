package main;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Represents a single draggable object in the game.
 */
public class DraggableObj {
    
    private Image image;
    // Public for easy access by MouseInputHandler
    public int x, y; // Center coordinates
    private int size; 
    
    /**
     * Constructs a new draggable object.
     * @param imagePath The resource path to the image file.
     * @param x The initial center X coordinate.
     * @param y The initial center Y coordinate.
     * @param size The width/height of the object.
     */
    public DraggableObj(String imagePath, int x, int y, int size) {
        try {
            // Load the image from the resource path
            this.image = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (Exception e) {
            System.err.println("Failed to load draggable object image: " + imagePath);
            this.image = null;
        }
        this.x = x;
        this.y = y;
        this.size = size;
    }
    
    /**
     * Checks if the given mouse coordinates fall within the bounds of this object.
     */
    public boolean contains(int clickX, int clickY) {
        // Check if the click is within the square bounding box of the object
        return clickX >= x - size / 2 && 
               clickX <= x + size / 2 && 
               clickY >= y - size / 2 && 
               clickY <= y + size / 2;
    }
    
    /**
     * Draws the object centered around its (x, y) coordinates.
     */
    public void draw(Graphics g) {
        if (image != null) {
            // Draw image centered at (x, y)
            g.drawImage(image, x - size / 2, y - size / 2, size, size, null);
        }
    }
    
    /**
     * Helper method to get the size for collision checks outside the class.
     */
    public int getSize() {
        return size;
    }
}