
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sneha
 */
public class ScalePanel extends JPanel{
    private BufferedImage originalImage;
    private double zoom = 1.0;
    private int offsetX = 0;
    private int offsetY = 0;
    private int tickSpacing = 50;
    private int tickLength = 5;
    private int x, y;
    private final Point offset = new Point(0, 0);
    private Point startDrag;
    private Point endDrag;
    static int startX, startY;
    private double scale = 1.0;
    private int width = 1400, height = 500;
    private int zoomLevel = 100;
    
    public ScalePanel(BufferedImage image) {
        this.originalImage = image;
    }
         @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
       // g2d.translate(offset.x, offset.y);
       // g2d.scale(scale, scale);
    }
        public void drawAxes(Graphics2D g2d) {
        int a = (int)(offset.x * zoom);
        int b = (int)(offset.y * zoom);
        int width = (int)(originalImage.getWidth() * zoom);
        int height = (int)(originalImage.getHeight() * zoom);
        
        g2d.setColor(Color.BLACK);
        g2d.drawLine(a, b + height, a + width, b + height); // x-axis
        g2d.drawLine(a, b, a, b + height); // y-axis
        
        double tickSpacingZoomed = tickSpacing * zoom;
        int numTicksX = (int)(width / tickSpacingZoomed);
        int numTicksY = (int)(height / tickSpacingZoomed);
        
        for (int i = 0; i < numTicksX; i++) {
            int tickX = a + (int)(i * tickSpacingZoomed);
            g2d.drawLine(tickX, b + height, tickX, b + height + tickLength);
            g2d.drawString(Integer.toString((int)((offsetX + i * tickSpacing) * zoom)), tickX - 10, b + height + tickLength + 15);
        }
        
        for (int i = 0; i < numTicksY; i++) {
            int tickY = b + (int)(i * tickSpacingZoomed);
            g2d.drawLine(a, tickY, b - tickLength, tickY);
            g2d.drawString(Integer.toString((int)((offsetY + i * tickSpacing) * zoom)), a - tickLength - 40, tickY + 5);
        }
        }
}

