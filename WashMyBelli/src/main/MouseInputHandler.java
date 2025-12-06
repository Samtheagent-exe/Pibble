package main;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator; 

public class MouseInputHandler implements MouseListener, MouseMotionListener {

    private UIManager uiManager;

    public MouseInputHandler(UIManager uiManager) {
        this.uiManager = uiManager;
    }

    // --- MOUSE MOTION LISTENER ---

    @Override
    public void mouseDragged(MouseEvent e) {
        // 1. Always update the mouse position for the cursor image
        uiManager.mouseX = e.getX();
        uiManager.mouseY = e.getY();

        // 2. Check if we are dragging an object and in the level
        if (uiManager.isMousePressed && uiManager.currentDragTarget != null && uiManager.currentBackground == uiManager.defaultBackground) {
            
            // A. Update the position of the currently dragged object
            DraggableObj draggedObj = uiManager.currentDragTarget;
            draggedObj.x = e.getX();
            draggedObj.y = e.getY();
            
            // B. Check for collision with dust patches and remove them
            Iterator<DustPatch> iterator = uiManager.dustPatches.iterator();
            while (iterator.hasNext()) {
                DustPatch dust = iterator.next();
                
                // If the dragged object overlaps with the dust patch
                if (dust.checkCollision(draggedObj)) {
                    iterator.remove(); // Safely remove the dust patch from the list
                }
            }
        }
        
        uiManager.repaint(); // Redraw everything
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Update position and redraw to move the cursor image
        uiManager.mouseX = e.getX();
        uiManager.mouseY = e.getY();
        uiManager.repaint(); 
    }
    
    // --- MOUSE LISTENER ---

    @Override
    public void mousePressed(MouseEvent e) {
        uiManager.isMousePressed = true;
        
        if (uiManager.currentBackground == uiManager.defaultBackground) {
            
            // Step 1: Assume no object is selected
            uiManager.currentDragTarget = null; 
            
            // Step 2: Iterate backwards through the list to select the object drawn on top
            for (int i = uiManager.draggableObjects.size() - 1; i >= 0; i--) {
                DraggableObj obj = uiManager.draggableObjects.get(i); 
                
                if (obj.contains(e.getX(), e.getY())) {
                    // Set the found object as the drag target
                    uiManager.currentDragTarget = obj;
                    break; // Stop searching once we find the top-most object
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        uiManager.isMousePressed = false;
        // Clear the drag target when the mouse is released
        uiManager.currentDragTarget = null;
    }

    // --- UNUSED INTERFACE METHODS ---
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
}