/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Akiris
 */
public class TreeDisplayFileContents {

    
    public void displayFileContents(String selectedFilePath, JPanel jPanel4) {
        String selectedFile = new File(selectedFilePath).getName();
    
        String fastaSuff = ".fasta";
        String gffSuff = ".gff";
        String txtSuff = ".txt";
        String pngSuff = ".png";

        if (selectedFile.contains(fastaSuff) || selectedFile.contains(gffSuff) || selectedFile.contains(txtSuff)) {
            System.out.println("its a txt file");
            try {
                FileReader reader = new FileReader(selectedFilePath);
                BufferedReader br = new BufferedReader(reader);
                String line;
                JTextArea textArea = new JTextArea();

                while ((line = br.readLine()) != null) {
                    textArea.append(line + "\n");
                }

                JScrollPane scrollPane = new JScrollPane(textArea);

                jPanel4.removeAll();
                jPanel4.setLayout(new BorderLayout());
                jPanel4.add(scrollPane);
                jPanel4.revalidate();

            } catch (IOException ex) {
                ex.printStackTrace();
            }


        } else if (selectedFile.contains(pngSuff)) {
            System.out.println("its a png file");
            try {
                File file = new File(selectedFilePath);
                BufferedImage image = ImageIO.read(file);
                JLabel label = new JLabel(new ImageIcon(image));
                JScrollPane scrollPane = new JScrollPane(label);

                jPanel4.removeAll();
                jPanel4.setLayout(new BorderLayout());
                jPanel4.add(scrollPane);
                jPanel4.revalidate();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        } else {
            System.out.println("it must be a project");
        }
    
    
    }
     
}
