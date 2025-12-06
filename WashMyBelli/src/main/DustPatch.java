package main;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * Represents a single transparent dust patch that can be cleaned by a DraggableObj.
 */
public class DustPatch {
    
    private Image image;
    private int x, y; // Center coordinates
    private int width, height;
    private float opacity = (float)(Math.random() * 0.5) + 0.3f; 

    public DustPatch(String imagePath, int x, int y, int width, int height) {
        try {
            this.image = new ImageIcon(getClass().getResource(imagePath)).getImage();
        } catch (Exception e) {
            System.err.println("Failed to load dust patch image: " + imagePath);
            this.image = null;
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Checks if the draggable object's position overlaps with the dust patch area.
     */
    public boolean checkCollision(DraggableObj draggable) {
        if (draggable == null) return false;
        
        // OLD: int dragRadius = draggable.getSize() / 2;
        
        // NEW: 60% of the previous distance (Tightened Collision)
        int dragRadius = (int)((draggable.getSize() / 2) * 0.6); 
        
        // Check for overlap
        return draggable.x + dragRadius > x - width / 2 &&
               draggable.x - dragRadius < x + width / 2 &&
               draggable.y + dragRadius > y - height / 2 &&
               draggable.y - dragRadius < y + height / 2;
    }

    /**
     * Draws the dust patch using Graphics2D.
     */
    public void draw(Graphics g) {
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g; 
            
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(image, x - width / 2, y - height / 2, width, height, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }
}