
import Classes.ImagePanel;
import java.awt.BorderLayout;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author sneha
 */
public class ZoomableImageFrame extends JFrame {
    public ImagePanel imagePanel;

//    public ZoomableImageFrame(Image image) {
//        setTitle("Zoomable Image");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1200,1200);
//        imagePanel = new ImagePanel(image);
//        add(imagePanel, BorderLayout.CENTER);
//        JScrollPane scrollpane = new JScrollPane(imagePanel);
//        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
////        imagePanel.removeAll();
////        imagePanel.setLayout(new BorderLayout());
//        add(scrollpane);
//        validate();
//        JSlider zoomSlider = new JSlider(1, 200, 100);
//        zoomSlider.addChangeListener(e -> {
//            int value = zoomSlider.getValue();
//            double zoomLevel = value / 100.0;
//            imagePanel.setZoomLevel(zoomLevel);
//        });
//        add(zoomSlider, BorderLayout.SOUTH);
//        
//       // add(scrollpane, BorderLayout.EAST);
//        
//        pack();
//        setVisible(true);
//    }

}
