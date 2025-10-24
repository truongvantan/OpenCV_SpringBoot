package com.tvt.form;

import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import jnafilechooser.api.JnaFileChooser;

import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tvt.common.ImageCommonHandle;
import com.tvt.config.AppConfiguration;

import javax.swing.event.ListSelectionEvent;

@Component
public class FormConvertImageExtension extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private final AppConfiguration appConfiguration;
	
//	@Value("${init.current.directory.file.chooser}")
//	private String INIT_CURRENT_DIRECTORY_FILE_CHOOSER;
	
	private ImageCommonHandle imageCommonHandle = new ImageCommonHandle();
	
	private JPanel contentPane;
	private JButton btnLoadImage;
	private JScrollPane scrollPane;
	private JList<String> listImage;
	private DefaultListModel<String> listModel;
	private JPanel panelInputImage;
	private JLabel lbLoadInputImage;
	private JLabel lbInputImage;
	private JComboBox<String> cbbImageExtension;
	private JLabel lblFileExtension;
	private JButton btnConvert;
	private JnaFileChooser fileChooser;

//	public FormConvertImageExtension() {
//		initComponent();
//	}
	
	
	@Autowired
	public FormConvertImageExtension(AppConfiguration appConfiguration) throws HeadlessException {
		this.appConfiguration = appConfiguration;
		initComponent();
	}

	private void initComponent() {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 886, 615);
		this.setTitle("Convert Image Extension");
		this.setLocationRelativeTo(null);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnLoadImage = new JButton("Load image path");
		btnLoadImage.setBounds(10, 11, 129, 40);
		contentPane.add(btnLoadImage);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 62, 327, 433);
		contentPane.add(scrollPane);
		
		listModel = new DefaultListModel<>();
		
		listImage = new JList<>(listModel);
		listImage.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(listImage);
		
		panelInputImage = new JPanel();
		panelInputImage.setLayout(null);
		panelInputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelInputImage.setBounds(419, 62, 424, 433);
		contentPane.add(panelInputImage);
		
		lbLoadInputImage = new JLabel("");
		lbLoadInputImage.setBounds(0, 0, 424, 433);
		panelInputImage.add(lbLoadInputImage);
		
		lbInputImage = new JLabel("Input Image");
		lbInputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbInputImage.setBounds(419, 24, 95, 14);
		contentPane.add(lbInputImage);
		
		cbbImageExtension = new JComboBox<String>();
		cbbImageExtension.setFont(new Font("Tahoma", Font.BOLD, 13));
		cbbImageExtension.setModel(new DefaultComboBoxModel<String>(new String[] {".jpg", ".png", ".tif", ".bmp", ".gif", ".webp"}));
		cbbImageExtension.setSelectedIndex(0);
		cbbImageExtension.setBounds(167, 507, 83, 22);
		contentPane.add(cbbImageExtension);
		
		lblFileExtension = new JLabel("Select file extension:");
		lblFileExtension.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblFileExtension.setBounds(10, 511, 147, 14);
		contentPane.add(lblFileExtension);
		
		btnConvert = new JButton("Convert");
		btnConvert.setForeground(Color.WHITE);
		btnConvert.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnConvert.setBorderPainted(false);
		btnConvert.setBackground(new Color(109, 185, 40));
		btnConvert.setBounds(366, 525, 129, 40);
		contentPane.add(btnConvert);
		
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
		btnConvert_Click();
	}

	private void btnConvert_Click() {
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileNameExtensionSelected = (String)cbbImageExtension.getSelectedItem();
				
				if (cbbImageExtension.getSelectedIndex() < 0 || fileNameExtensionSelected == null) {
					JOptionPane.showMessageDialog(null, "Please select file name extension!!!", "WARNING", JOptionPane.WARNING_MESSAGE);
				} else {
					String pathImageSelected = listImage.getSelectedValue();
//					pathImageSelected = pathImageSelected.replace("\\", "\\\\");
					if (pathImageSelected == null || listImage.getSelectedIndex() < 0) {
						JOptionPane.showMessageDialog(null, "Please select path image want to convert!!!", "WARNING", JOptionPane.WARNING_MESSAGE);
					} else {
						JFileChooser jFileChooser = new JFileChooser();
						jFileChooser.setDialogTitle("Please select the folder you want to save output image");
//						jFileChooser.setCurrentDirectory(new File("D:\\Tan\\Sample_Data\\"));
//						jFileChooser.setCurrentDirectory(new File(INIT_CURRENT_DIRECTORY_FILE_CHOOSER));
						jFileChooser.setCurrentDirectory(new File(appConfiguration.getInitCurrentDirectoryFileChooser()));
						jFileChooser.setMultiSelectionEnabled(false);
						jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						String outputFolderPath = "";
						
						if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
							outputFolderPath = jFileChooser.getSelectedFile().getAbsolutePath();
							
							// Format output file name
					        File inputFile = new File(pathImageSelected);
					        String baseName = inputFile.getName().replaceFirst("[.][^.]+$", ""); // remove extension
					        String outputImagePath = outputFolderPath + "\\" + baseName + "_converted" + fileNameExtensionSelected;
					        
					        Mat inputMat = Imgcodecs.imread(pathImageSelected);
					        
					        if (inputMat.empty()) {
					        	JOptionPane.showMessageDialog(null, "Cannot read image opencv", "WARNING", JOptionPane.WARNING_MESSAGE);
					            return;
					        }
					        
					        if (Imgcodecs.imwrite(outputImagePath, inputMat)) {
					        	JOptionPane.showMessageDialog(null, "Convert image successfully", "INFO", JOptionPane.INFORMATION_MESSAGE);
					        } else {
					        	JOptionPane.showMessageDialog(null, "Cannot convert image: " + outputImagePath, "WARNING", JOptionPane.WARNING_MESSAGE);
					        }
						}
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
}
