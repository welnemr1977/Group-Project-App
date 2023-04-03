

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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
public abstract class ImagePanel extends JPanel implements MouseWheelListener,  MouseListener, MouseMotionListener {
   
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
    public ImagePanel (BufferedImage image) {
        this.originalImage = image;
        x = 0;
        y = 0;
        //this.img = img;
        addMouseWheelListener(this);
        addMouseListener(this);
       addMouseMotionListener(this);
        
       addMouseListener(new MouseAdapter() {
          @Override
            public void mousePressed(MouseEvent me) {
            startDrag = me.getPoint();
            super.mousePressed(me);
                startX = me.getX();
                startY = me.getY();
            }
       });
    addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent me) {
                super.mouseDragged(me);
            Point current = me.getPoint();
        int deltaX = current.x - startDrag.x;
        int deltaY = current.y - startDrag.y;
        offset.translate(deltaX, deltaY);
        startDrag = current;
        if (me.getX() < startX) {//moving image to right
                    x -= 100;
                } else if (me.getX() > startX) {//moving image to left
                    x += 100;
                }

                if (me.getY() < startY) {//moving image up
                    y -= 100;
                } else if (me.getY() > startY) {//moving image to down
                    y += 100;
                }
                repaint();
            }
        });
    }
     @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }
           @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        if (notches < 0) {
            scale *= 1.1;
        } else {
            scale /= 1.1;
        }
        repaint();
}
             
    public void mouseReleased(MouseEvent e) {
            startDrag = null;
            endDrag = null;
            repaint();
        
}
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(offset.x, offset.y);
        g2d.scale(scale, scale);
        //g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g2d.drawImage(originalImage, (int)(offset.x * zoom), (int)(offset.y * zoom), (int)(originalImage.getWidth() * zoom), (int)(originalImage.getHeight() * zoom), null);
        ScalePanel scalepanel = new ScalePanel(originalImage);
        scalepanel.drawAxes(g2d);

    }
}



