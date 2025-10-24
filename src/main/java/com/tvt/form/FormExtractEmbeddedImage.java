package com.tvt.form;

import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tvt.common.ImageCommonHandle;
import com.tvt.config.AppConfiguration;

import jnafilechooser.api.JnaFileChooser;

@Component
public class FormExtractEmbeddedImage extends JFrame {

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private final AppConfiguration appConfiguration;
	
//	private static final String OUTPUT_EMBEDDED_IMAGE_FOLDER = "D:\\Tan\\Sample_Data\\Embedded_Image";
	
//	@Value("${output.embedded.image.folder}")
//	private String OUTPUT_EMBEDDED_IMAGE_FOLDER;
//	
//	@Value("${init.current.directory.file.chooser}")
//	private String INIT_CURRENT_DIRECTORY_FILE_CHOOSER;
	
	private ImageCommonHandle imageCommonHandle = new ImageCommonHandle();
	private JPanel contentPane;
	private JButton btnLoadImage;
	private JList<String> listImage;
	private DefaultListModel<String> listModel;
	private JScrollPane scrollPane;
	private JPanel panelInputImage;
	private JLabel lbThermalImage;
	private JLabel lbLoadInputImage;
	private JPanel panelOutputImage;
	private JLabel lbLoadOutputImage;
	private JLabel lbOutputImage;
	private JLabel lbLocationThermalImage;
	private JLabel lbLocationThermalImageValue;
	private JLabel lbLocationEmbeddedImage;
	private JLabel lbLocationEmbeddedImageValue;
	private JnaFileChooser fileChooser;
	
//	public FormExtractEmbeddedImage() {
//		initComponent();
//	}
	
	
	@Autowired
	public FormExtractEmbeddedImage(AppConfiguration appConfiguration) throws HeadlessException {
		this.appConfiguration = appConfiguration;
		initComponent();
	}



	private void initComponent() {
		this.setTitle("Extract Embedded Image");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 1822, 737);
		this.setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);

		btnLoadImage = new JButton("Load image path");

		btnLoadImage.setBounds(30, 11, 129, 40);
		contentPane.add(btnLoadImage);

		listModel = new DefaultListModel<>();

		scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 62, 327, 480);
		contentPane.add(scrollPane);

		listImage = new JList<>(listModel);
		listImage.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setViewportView(listImage);

		panelInputImage = new JPanel();
		panelInputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelInputImage.setBounds(439, 62, 640, 480);
		contentPane.add(panelInputImage);
		panelInputImage.setLayout(null);

		lbLoadInputImage = new JLabel("");
		
		lbLoadInputImage.setBounds(0, 0, 640, 480);
		panelInputImage.add(lbLoadInputImage);

		lbThermalImage = new JLabel("Thermal Image");
		lbThermalImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbThermalImage.setBounds(439, 24, 95, 14);
		contentPane.add(lbThermalImage);

		panelOutputImage = new JPanel();
		panelOutputImage.setLayout(null);
		panelOutputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOutputImage.setBounds(1152, 63, 640, 480);
		contentPane.add(panelOutputImage);

		lbLoadOutputImage = new JLabel("");
		lbLoadOutputImage.setBounds(0, 0, 640, 480);
		panelOutputImage.add(lbLoadOutputImage);

		lbOutputImage = new JLabel("Embedded Image");
		lbOutputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbOutputImage.setBounds(1152, 23, 119, 14);
		contentPane.add(lbOutputImage);
		
		lbLocationThermalImage = new JLabel("Location:");
		lbLocationThermalImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationThermalImage.setBounds(439, 564, 63, 14);
		contentPane.add(lbLocationThermalImage);
		
		lbLocationThermalImageValue = new JLabel("");
		lbLocationThermalImageValue.setVisible(false);
		lbLocationThermalImageValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationThermalImageValue.setBounds(512, 565, 78, 14);
		contentPane.add(lbLocationThermalImageValue);
		
		lbLocationEmbeddedImage = new JLabel("Location:");
		lbLocationEmbeddedImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationEmbeddedImage.setBounds(1152, 565, 63, 14);
		contentPane.add(lbLocationEmbeddedImage);
		
		lbLocationEmbeddedImageValue = new JLabel("");
		lbLocationEmbeddedImageValue.setVisible(false);
		lbLocationEmbeddedImageValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationEmbeddedImageValue.setBounds(1225, 565, 95, 14);
		contentPane.add(lbLocationEmbeddedImageValue);
		
		// file chooser
		fileChooser = new JnaFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
//		fileChooser.setCurrentDirectory("D:\\Tan\\Sample_Data\\");
//		fileChooser.setCurrentDirectory(INIT_CURRENT_DIRECTORY_FILE_CHOOSER);
		fileChooser.setCurrentDirectory(appConfiguration.getInitCurrentDirectoryFileChooser());
		fileChooser.addFilter("Image Files", "gif", "jpg", "png", "webp", "tif", "bmp");
		fileChooser.addFilter("All files", "*.*");
		
		handleEvent();
	}

	private void handleEvent() {
		btnLoadImage_Click();
		listImage_ValueChanged();
		lbLoadInputImage_MouseMoved();
		lbLoadOutputImage_MouseMoved();
	}
	

	private void lbLoadOutputImage_MouseMoved() {
		lbLoadOutputImage.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				String locationValue = new StringBuffer("").append("(").append(x).append(",").append(y).append(")").toString();
				lbLocationEmbeddedImageValue.setText(locationValue);
			}
		});
	}

	private void lbLoadInputImage_MouseMoved() {
		lbLoadInputImage.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				String locationValue = new StringBuffer("").append("(").append(x).append(",").append(y).append(")").toString();
				
				lbLocationThermalImageValue.setText(locationValue);
			}
		});
	}

	private void btnLoadImage_Click() {
		btnLoadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(getOwner())) {
					File[] selectedFiles = fileChooser.getSelectedFiles();
					for (File file : selectedFiles) {
						listModel.addElement(file.getAbsolutePath());
					}
				}
			}
		});
	}

	private void listImage_ValueChanged() {
		listImage.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				String pathImageSelected = listImage.getSelectedValue();

				// load image to picturebox
				imageCommonHandle.loadImageToLabel(pathImageSelected, lbLoadInputImage, panelInputImage);
				lbLocationThermalImageValue.setVisible(true);
				// extract metadata of thermal image
				extractMetadataImage(pathImageSelected);
				
				// extract embedded image of thermal image
				extractEmbeddedImage(pathImageSelected);
			}
		});
	}

	private void extractMetadataImage(String pathImageSelected) {
		createOuputDirectory();
        
        // Format output file name
        File inputFile = new File(pathImageSelected);
        String baseName = inputFile.getName().replaceFirst("[.][^.]+$", ""); // remove extension
//        String thermalImageMetaDataPath = OUTPUT_EMBEDDED_IMAGE_FOLDER + "\\" + baseName + "_metadata.txt";
//        String thermalImageMetaDataPath = OUTPUT_EMBEDDED_IMAGE_FOLDER + "\\" + baseName + "_metadata.txt";
        String thermalImageMetaDataPath = appConfiguration.getOutputEmbeddedImageFolder() + "\\" + baseName + "_metadata.txt";
        
        /**
         * Extract Metadata of thermal image
         * Example command line: exiftool <filename.jpg>
         * */
        ProcessBuilder processBuilder1 = new ProcessBuilder(
        		"exiftool", pathImageSelected
        );
        
        try {
			processBuilder1.redirectOutput(new File(thermalImageMetaDataPath));
			Process process1 = processBuilder1.start();
			process1.waitFor();
			JOptionPane.showMessageDialog(null, "Extract metadata successfully: " + thermalImageMetaDataPath, "INFO", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract metadata image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
		} catch (InterruptedException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract metadata image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void createOuputDirectory() {
//		File outputDir = new File(OUTPUT_EMBEDDED_IMAGE_FOLDER);
//		Path path = Paths.get(OUTPUT_EMBEDDED_IMAGE_FOLDER);
		Path path = Paths.get(appConfiguration.getOutputEmbeddedImageFolder());
		
        if (Files.exists(path)) {
//        	File outputDir = new File(OUTPUT_EMBEDDED_IMAGE_FOLDER);
        	File outputDir = new File(appConfiguration.getOutputEmbeddedImageFolder());
        	outputDir.mkdirs();
        }
	}

	private void extractEmbeddedImage(String pathImageSelected) {
		createOuputDirectory();
        
        // Format output file name
        File inputFile = new File(pathImageSelected);
        String baseName = inputFile.getName().replaceFirst("[.][^.]+$", ""); // remove extension
//        String embeddedImagePath = OUTPUT_EMBEDDED_IMAGE_FOLDER + "\\" + baseName + "_embedded.png";
//        String embeddedImagePath = OUTPUT_EMBEDDED_IMAGE_FOLDER + "\\" + baseName + "_embedded.png";
        String embeddedImagePath = appConfiguration.getOutputEmbeddedImageFolder() + "\\" + baseName + "_embedded.png";
  
        /**
         * Run ExifTool to extract embedded image
         * Example command line: exiftool -b -EmbeddedImage input.jpg > output.png
         * */
        ProcessBuilder pb2 = new ProcessBuilder(
                "exiftool", "-b", "-EmbeddedImage", pathImageSelected
        );
        
        try {
        	pb2.redirectOutput(new File(embeddedImagePath));
			Process process = pb2.start();
			process.waitFor();
			JOptionPane.showMessageDialog(null, "Extract embedded image successfully: " + embeddedImagePath, "INFO", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract embedded image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
		} catch (InterruptedException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract embedded image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
		}
        
        // Show extracted embedded image
        try {
			File embeddedFile = new File(embeddedImagePath);
			
			if (embeddedFile.exists() && embeddedFile.length() > 0) {
				imageCommonHandle.loadImageToLabel(embeddedImagePath, lbLoadOutputImage, panelOutputImage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract embedded image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
		}
        lbLocationEmbeddedImageValue.setVisible(true);
	}

}
