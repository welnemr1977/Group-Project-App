package Classes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import java.awt.event.MouseEvent;
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
 * @author snehaa
 */
public class ImagePanel extends JPanel implements MouseWheelListener {

    private BufferedImage originalImage;
    private double scale = 1.0;
    private final Point offset = new Point(0, 0);
    private Point startDrag;
    private Point endDrag;
    private double zoom = 0.2;

    public ImagePanel(BufferedImage image) {
        this.originalImage = image;
        addMouseWheelListener(this);

    }

    //image = icon.getImage();
    // Add the mouse wheel listener
    //addMouseWheelListener(this);
    // Set the preferred size of the panel to the size of the image icon
//    setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
//        this.originalImage = image;
    //JLabel label = new JLabel(icon);
    //JLabel label = new JLabel(new ImageIcon(originalImage));
    //this.originalImage = image;
    //addMouseWheelListener(this);
    // addMouseListener(this);
    // addMouseMotionListener(this);
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.translate(offset.x, offset.y);
        g2d.scale(scale, scale);

        // g.drawImage(image, 0, 0, getWidth(), getHeight(), null)      
        g2d.drawImage(originalImage, 0, 0, getWidth(), getHeight(), null);
        g2d.dispose();
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

    public void mousePressed(MouseEvent e) {
        startDrag = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {
//        endDrag = new Point(e.getX(), e.getY());
//            double dx = (double) (endDrag.x - startDrag.x) / getWidth();
//            double dy = (double) (endDrag.y - startDrag.y) / getHeight();
//            zoom += dx - dy;
//            startDrag = endDrag;
//            repaint();
        Point current = e.getPoint();
        int deltaX = current.x - startDrag.x;
        int deltaY = current.y - startDrag.y;
        offset.translate(deltaX, deltaY);
        startDrag = current;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        startDrag = null;
        endDrag = null;
        repaint();
    }
}
