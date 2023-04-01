
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sneha
 */
public class PanningImageComponent extends JComponent { 
    private static final long serialVersionUID = 1L;
    private BufferedImage originalImage;
    private Point lastDragPoint;
    private Rectangle viewRect;

    public PanningImageComponent(BufferedImage image) {
        this.originalImage = image;
        this.viewRect = new Rectangle(0, 0, image.getWidth(), image.getHeight());
        setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if (lastDragPoint != null) {
                    int dx = e.getX() - lastDragPoint.x;
                    int dy = e.getY() - lastDragPoint.y;
                    viewRect.translate(-dx, -dy);
                    lastDragPoint = e.getPoint();
                    repaint();
                }
            }

            public void mouseMoved(MouseEvent e) {
                lastDragPoint = e.getPoint();
            }
            
            });
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                lastDragPoint = null;
            }
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(originalImage, viewRect.x, viewRect.y, viewRect.width, viewRect.height, null);
    }
}

