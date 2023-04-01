
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sneha
 */
public class ZoomableImageIcon extends JFrame {
    private ImageIcon imageIcon;
    private JLabel label;

    public ZoomableImageIcon(ImageIcon imageIcon) {
        this.imageIcon = imageIcon;

        label = new JLabel(this.imageIcon);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setVerticalAlignment(JLabel.CENTER);

        label.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                if (notches < 0) {
                    zoomIn();
                } else {
                    zoomOut();
                }
            }
        });
        
        getContentPane().add(label, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void zoomIn() {
        Image img = imageIcon.getImage();
        int width = (int) (img.getWidth(null) * 1.1);
        int height = (int) (img.getHeight(null) * 1.1);
        ImageIcon newIcon = new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        label.setIcon(newIcon);
    }

    private void zoomOut() {
        Image img = imageIcon.getImage();
        int width = (int) (img.getWidth(null) * 0.9);
        int height = (int) (img.getHeight(null) * 0.9);
        ImageIcon newIcon = new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        label.setIcon(newIcon);
    }

    
}
