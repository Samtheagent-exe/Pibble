package main;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList; 

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer; // Import for the Game Loop/Timer

public class UIManager extends JPanel {
    
    private SoundManager soundManager;
    private GameWindow gameWindow;
    
    // Images
    private Image menuImage;
    private Image levelImage;
    public Image defaultBackground; 
    
    // --- STATE VARIABLES ---
    public Image currentBackground; 
    private Image activeForeground;
    
    // Level-specific array of pibble images
    private Image[] pibble = {
    		new ImageIcon(getClass().getResource("/res/Adobe Express - file (2) (1).png")).getImage(),
    		new ImageIcon(getClass().getResource("/res/Adobe Express - file (2) (1).png")).getImage(),
    };
    
    // --- DRAGGABLE OBJECT STATE ---
    public ArrayList<DraggableObj> draggableObjects = new ArrayList<>(); 
    public DraggableObj currentDragTarget = null; 

    // --- DUST PATCH STATE ---
    public ArrayList<DustPatch> dustPatches = new ArrayList<>(); 
    private int initialDustCount = 0; // To calculate percentage

    public Image cursorObject; 
    
    // --- MOUSE INPUT STATE ---
    public int mouseX = 0;
    public int mouseY = 0;
    public boolean isMousePressed = false;
    
    // --- TIMER & ANIMATION STATE ---
    private Timer gameLoop;         // Runs the game logic (animation/time)
    private long levelStartTime;    // When the level started
    private String timeString = "0s";
    
    // Animation variables
    private int jumpOffset = 0;
    private int jumpDirection = 0; // 0 = stopped, 1 = up, -1 = down
    private boolean isFlipped = false;
    
    public UIManager(SoundManager s) {
        this.soundManager = s;
        
        // Load images
        menuImage = new ImageIcon(getClass().getResource("/res/zibble menu2.jpg")).getImage();
        levelImage = new ImageIcon(getClass().getResource("/res/pibblemenu2.jpg")).getImage();
        defaultBackground = new ImageIcon(getClass().getResource("/res/Untitled2.png")).getImage();
        
        try {
             cursorObject = new ImageIcon(getClass().getResource("/res/cursorObject.png")).getImage(); 
        } catch (Exception e) {
             System.out.println("Warning: Could not load cursorObject.png.");
        }
        
        gameWindow = new GameWindow(this);
        
        MouseInputHandler inputHandler = new MouseInputHandler(this); 
        addMouseListener(inputHandler); 
        addMouseMotionListener(inputHandler);
        
        // Initialize Game Loop (approx 60 FPS)
        gameLoop = new Timer(16, e -> updateGame());
        
        OpenMainMenu();
    }
    
    /**
     * Called every ~16ms by the Timer to update logic and animation
     */
    private void updateGame() {
        // Only run logic if we are in a level
        if (currentBackground == defaultBackground) {
            
            // 1. Update Time String
            long elapsed = (System.currentTimeMillis() - levelStartTime) / 1000;
            timeString = "Time: " + elapsed + "s";
            
            // 2. Win Condition Animation (0% Dirt)
            if (dustPatches.isEmpty()) {
                // If animation hasn't started, start it
                if (jumpDirection == 0) jumpDirection = 1; 
                
                // Move Pibble
                int jumpSpeed = 5;
                int maxJumpHeight = 150;
                
                jumpOffset += (jumpSpeed * jumpDirection);
                
                // Top of jump
                if (jumpOffset >= maxJumpHeight) {
                    jumpOffset = maxJumpHeight;
                    jumpDirection = -1; // Go down
                    isFlipped = true;   // Flip at the top
                }
                // Bottom of jump
                else if (jumpOffset <= 0) {
                    jumpOffset = 0;
                    jumpDirection = 1;  // Go up
                    isFlipped = false;  // Reset flip at bottom
                }
            }
        }
        refreshScreen();
    }
    
    // --- MAIN MENU LOGIC ---
    void OpenMainMenu() {
        gameLoop.stop(); // Stop timer in menus
        this.removeAll(); 
        currentBackground = menuImage;
        activeForeground = null; 
        
        draggableObjects.clear(); 
        currentDragTarget = null;
        dustPatches.clear();
        
        setLayout(null); 
        
        // PLAY BUTTON
        JButton playButton = new MenuButton("PLAY", 250, 200, 300, 60);
        add(playButton);
        playButton.addActionListener(e -> LevelMenu()); 
        
        // EXIT BUTTON
        JButton exitButton = new MenuButton("EXIT", 250, 300, 300, 60);
        add(exitButton);
        exitButton.addActionListener(e -> System.exit(0));
        
        // MUTE BUTTON
        String muteText = soundManager.muted() ? "UNMUTE" : "MUTE";
        JButton muteButton = new MenuButton(muteText, 250, 500, 300, 60);
        add(muteButton);
        
        muteButton.addActionListener(e -> {
             soundManager.toggleMute();
             muteButton.setText(soundManager.muted() ? "UNMUTE" : "MUTE"); 
        });

        refreshScreen();
    }
    
    // --- LEVEL MENU LOGIC ---
    public void LevelMenu() {
        gameLoop.stop(); // Stop timer in menus
        this.removeAll();
        currentBackground = levelImage;
        activeForeground = null; 
        
        draggableObjects.clear(); 
        currentDragTarget = null;
        dustPatches.clear();

        setLayout(null);

        JButton level1 = new MenuButton("1", 100, 100, 100, 100);
        add(level1);
        level1.addActionListener(e -> Level(0)); 
        
        JButton level2 = new MenuButton("LEVEL 2", 100, 250, 100, 100);
        add(level2);

        JButton back = new MenuButton("BACK", 250, 500, 300, 60);
        add(back);
        back.addActionListener(e -> OpenMainMenu());
        
        refreshScreen();
    }
    
    // Actual Game:
    public void Level(int lvl) {
    	this.removeAll();
    	
        currentBackground = defaultBackground;
        activeForeground = pibble[lvl];
        
        // Reset Animation State
        jumpOffset = 0;
        jumpDirection = 0;
        isFlipped = false;
        
        // Reset Timer
        levelStartTime = System.currentTimeMillis();
        gameLoop.start(); // Start the game loop!
        
        draggableObjects.clear();
        currentDragTarget = null;
        
        draggableObjects.add(new DraggableObj("/res/istockphoto-464971905-612x612.jpg", 100, 100, 80));
        draggableObjects.add(new DraggableObj("/res/istockphoto-464971905-612x612.jpg", 500, 150, 50));
        draggableObjects.add(new DraggableObj("/res/istockphoto-464971905-612x612.jpg", 300, 400, 120));
        
        spawnDust(100); // 100 Dust pieces
         
        setLayout(null);
        
        JButton back = new MenuButton("EXIT LEVEL", 50, 50, 200, 50);
        add(back);
        back.addActionListener(e -> {
        	activeForeground = null;
            LevelMenu();
       });
        
        refreshScreen();
    }
    
    private void spawnDust(int count) {
        dustPatches.clear();
        this.initialDustCount = count; // Save total for percentage calc
        
        String dustPath = "/res/—Pngtree—dust patch png_8503540.png";
        
        int pibbleWidth = 600;
        int pibbleHeight = 600;
        int pibbleX = (getWidth() - pibbleWidth) / 2 - 50; 
        int pibbleY = (getHeight() - pibbleHeight) / 2;
        
        for(int i = 0; i < count; i++) {
        	int patchSize = (int)(Math.random() * 100) + 50;
            int randomX = pibbleX + (int)(Math.random() * (pibbleWidth - patchSize));
            int randomY = pibbleY + (int)(Math.random() * (pibbleHeight - patchSize));
            
            dustPatches.add(new DustPatch(dustPath, randomX, randomY, patchSize, patchSize));
        }
    }
    
    private void refreshScreen() {
        this.revalidate(); 
        this.repaint();   
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g; 
        
        // 1. Draw Background
        if (currentBackground != null) {
            g2d.drawImage(currentBackground, 0, 0, getWidth(), getHeight(), null);
        }
        
        // LEVEL RENDER
        if (currentBackground == defaultBackground) {
            
            // 2. Draw Pibble (With Animation Support)
            if (activeForeground != null) {
                int pibbleWidth = 600;
                int pibbleHeight = 600;
                // Base Y position
                int baseX = (getWidth() - pibbleWidth) / 2 - 50; 
                int baseY = (getHeight() - pibbleHeight) / 2;
                
                // Apply Animation Offset (moves up -> y decreases)
                int animY = baseY - jumpOffset; 
                
                if (isFlipped) {
                    // Draw Vertically Flipped (Upside Down)
                    // We draw from the bottom-up by using negative height
                    g2d.drawImage(activeForeground, baseX, animY + pibbleHeight, pibbleWidth, -pibbleHeight, null);
                } else {
                    // Normal Draw
                    g2d.drawImage(activeForeground, baseX, animY, pibbleWidth, pibbleHeight, null);
                }
            }
            
            // 3. Draw Objects & Dust
            for (DraggableObj obj : draggableObjects) { 
                obj.draw(g2d); 
            }
            for (DustPatch dust : dustPatches) {
                dust.draw(g2d);
            }
            
            // 4. Draw HUD (Heads Up Display)
            g2d.setColor(Color.BLACK); // Text Color
            g2d.setFont(new Font("SansSerif", Font.BOLD, 30));
            
            // Draw Timer (Top Right)
            g2d.drawString(timeString, getWidth() - 200, 50);
            
            // Draw Dirt Percentage (Top Left)
            int percent = 0;
            if (initialDustCount > 0) {
                percent = (int)((double)dustPatches.size() / initialDustCount * 100);
            }
            g2d.drawString("Dirt: " + percent + "%", 30, 50);
            
            // 5. Cursor
            if (cursorObject != null) {
                int cursorSize = 50; 
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); 
                g2d.drawImage(cursorObject, mouseX - cursorSize/2, mouseY - cursorSize/2, cursorSize, cursorSize, null);
            }
        }
    }
}