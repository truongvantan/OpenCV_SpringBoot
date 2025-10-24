package com.tvt.common;

import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

public class ImageCommonHandle {

	public void loadImageToPanel(ImageIcon imageIcon, JPanel panel) {
		panel.removeAll();
		JLabel imageLabel = new JLabel(imageIcon);
		panel.add(imageLabel);
	}

	public void loadImageToLabel(String imagePath, JLabel labelLoadImage, JPanel panelLoadImage) {
		BufferedImage bufferedImage = null;
		ImageIcon imageIcon = null;;
		
		try {
			bufferedImage = ImageIO.read(new File(imagePath));
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot open image: " + imagePath, "WARNING", JOptionPane.WARNING_MESSAGE);
			imageIcon = (ImageIcon)UIManager.getIcon("OptionPane.errorIcon");
//			return;
		}
		
		try {
			imageIcon = new ImageIcon(bufferedImage);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot open image: " + imagePath, "WARNING", JOptionPane.WARNING_MESSAGE);
			imageIcon = (ImageIcon)UIManager.getIcon("OptionPane.errorIcon");
		}


		if (imageIcon == null || imageIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
			JOptionPane.showMessageDialog(null, "Cannot open image: " + imagePath, "WARNING", JOptionPane.WARNING_MESSAGE);
		} else {
			imageIcon.setImage(imageIcon.getImage().getScaledInstance(panelLoadImage.getWidth(), panelLoadImage.getHeight(), Image.SCALE_SMOOTH));
			labelLoadImage.setIcon(imageIcon);
		}
	}

	public void loadCVMatToLabel(Mat mat, JLabel labelLoadImage, JPanel panelLoadImage) {
		BufferedImage bufferedImage = (BufferedImage) HighGui.toBufferedImage(mat);
		
		ImageIcon imageIcon = new ImageIcon(bufferedImage);


		if (imageIcon.getImageLoadStatus() == MediaTracker.ERRORED) {
			JOptionPane.showMessageDialog(null, "Cannot load CV Mat image", "WARNING", JOptionPane.WARNING_MESSAGE);
		} else {
			imageIcon.setImage(imageIcon.getImage().getScaledInstance(panelLoadImage.getWidth(), panelLoadImage.getHeight(), Image.SCALE_SMOOTH));
			labelLoadImage.setIcon(imageIcon);
		}
	}
	
}
