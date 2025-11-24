package com.tvt.form.image_processing;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tvt.common.CommonParseValue;
import com.tvt.common.ComponentSettingCommon;
import com.tvt.common.Constant;
import com.tvt.common.FlirMetadataParam;
import com.tvt.common.ImageCommonHandle;
import com.tvt.component.ImageLabelWithCircles;
import com.tvt.config.AppConfiguration;
import com.tvt.service.FileService;

@Component
public class FormExtractEmbeddedImage extends JFrame {

	private static final long serialVersionUID = 1L;
//	private static String outputFolderEmbedded = "";
	private final AppConfiguration appConfiguration;

	private ImageCommonHandle imageCommonHandle = new ImageCommonHandle();
	private ComponentSettingCommon componentSettingCommon = new ComponentSettingCommon();
	private CommonParseValue commonParseValue = new CommonParseValue();
	private FlirMetadataParam flirMetadataParam = new FlirMetadataParam();
	private Map<String, String> mapMetadataParams;
	private Mat temperatureMap;
	private FileService fileService;

	private JPanel contentPane;
	private JButton btnLoadImage;
	private JList<String> listImage;
	private Vector<String> listImageData;
	private JScrollPane scrollPane;
	private JPanel panelInputImage;
	private JLabel lbThermalImage;

//	private JLabel lbLoadInputImage;
	private JLabel lbLoadInputImage;

	private JPanel panelOutputVisionImage;
	private JLabel lbLoadOutputVisionImage;
	private JLabel lbOutputImage;
	private JLabel lbLocationThermalImage;
	private JLabel lbLocationThermalImageValue;
	private JLabel lbLocationEmbeddedImage;
	private JLabel lbLocationVisionImageValue;
	private JFileChooser jFileChooser;
	private JLabel lblMaxTemperature;
	private JLabel lblMinTemperature;
	private JLabel lblTemperature;
	private JLabel lbTemperatureValue;
	private JLabel lbMaxTemperatureValue;
	private JLabel lbMinTemperatureValue;
	private JLabel lbLocationThermalImage_1;
	private JLabel lbLocationThermalImage_2;
	private JLabel lbLocationMaxTemperatureValue;
	private JLabel lbLocationMinTemperatureValue;
	private JLabel lbMeanTemperature;
	private JLabel lbMeanTemperatureValue;
	private JButton btnSelectOutputFolder;
	private JTextField txtOutputFolderEmbeddedFilePath;

	@Autowired
	public FormExtractEmbeddedImage(AppConfiguration appConfiguration) throws HeadlessException {
		this.appConfiguration = appConfiguration;
		fileService = new FileService(this.appConfiguration);
		mapMetadataParams = new HashMap<>();
		temperatureMap = new Mat();
		initComponent();
	}

	private void initComponent() {
		try {
			UIManager.setLookAndFeel(appConfiguration.getUILookAndFeel());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		this.setTitle("Extract Embedded Image");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 1957, 871);
		this.setLocationRelativeTo(null);

		contentPane = new JPanel();
		this.setContentPane(contentPane);
		contentPane.setLayout(null);

		btnLoadImage = new JButton("Load image path");

		btnLoadImage.setBounds(30, 11, 129, 40);
		contentPane.add(btnLoadImage);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 62, 327, 520);
		contentPane.add(scrollPane);

		listImageData = new Vector<>();

		listImage = new JList<>();
		listImage.setListData(listImageData);
		listImage.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setViewportView(listImage);

		panelInputImage = new JPanel();
		panelInputImage.setLayout(null);
		panelInputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelInputImage.setBounds(446, 100, 640, 480);
		contentPane.add(panelInputImage);

//		lbLoadInputImage = new JLabel("");
		lbLoadInputImage = new ImageLabelWithCircles();
		lbLoadInputImage.setBounds(0, 0, 640, 480);
		panelInputImage.add(lbLoadInputImage);

		lbThermalImage = new JLabel("Thermal Image");
		lbThermalImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbThermalImage.setBounds(446, 62, 95, 14);
		contentPane.add(lbThermalImage);

		panelOutputVisionImage = new JPanel();
		panelOutputVisionImage.setLayout(null);
		panelOutputVisionImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOutputVisionImage.setBounds(1239, 102, 640, 480);
		contentPane.add(panelOutputVisionImage);

		lbLoadOutputVisionImage = new JLabel("");
		lbLoadOutputVisionImage.setBounds(0, 0, 640, 480);
		panelOutputVisionImage.add(lbLoadOutputVisionImage);

		lbOutputImage = new JLabel("Vision Image");
		lbOutputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbOutputImage.setBounds(1239, 62, 119, 14);
		contentPane.add(lbOutputImage);

		lbLocationThermalImage = new JLabel("Location:");
		lbLocationThermalImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationThermalImage.setBounds(446, 602, 63, 14);
		contentPane.add(lbLocationThermalImage);

		lbLocationThermalImageValue = new JLabel("");
		lbLocationThermalImageValue.setVisible(false);
		lbLocationThermalImageValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationThermalImageValue.setBounds(519, 603, 78, 14);
		contentPane.add(lbLocationThermalImageValue);

		lbLocationEmbeddedImage = new JLabel("Location:");
		lbLocationEmbeddedImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationEmbeddedImage.setBounds(1239, 602, 63, 14);
		contentPane.add(lbLocationEmbeddedImage);

		lbLocationVisionImageValue = new JLabel("");
		lbLocationVisionImageValue.setVisible(false);
		lbLocationVisionImageValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationVisionImageValue.setBounds(1319, 602, 95, 14);
		contentPane.add(lbLocationVisionImageValue);

		lblMaxTemperature = new JLabel("Max temperature:");
		lblMaxTemperature.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMaxTemperature.setBounds(446, 641, 129, 14);
		contentPane.add(lblMaxTemperature);

		lblMinTemperature = new JLabel("Min temperature:");
		lblMinTemperature.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMinTemperature.setBounds(446, 678, 119, 14);
		contentPane.add(lblMinTemperature);

		lblTemperature = new JLabel("Temperature:");
		lblTemperature.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTemperature.setBounds(688, 601, 95, 14);
		contentPane.add(lblTemperature);

		lbTemperatureValue = new JLabel("");
		lbTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbTemperatureValue.setBounds(793, 602, 68, 14);
		contentPane.add(lbTemperatureValue);

		lbMaxTemperatureValue = new JLabel("");
		lbMaxTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbMaxTemperatureValue.setBounds(572, 641, 68, 14);
		contentPane.add(lbMaxTemperatureValue);

		lbMinTemperatureValue = new JLabel("");
		lbMinTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbMinTemperatureValue.setBounds(572, 678, 68, 14);
		contentPane.add(lbMinTemperatureValue);

		lbLocationThermalImage_1 = new JLabel("Location:");
		lbLocationThermalImage_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationThermalImage_1.setBounds(688, 641, 63, 14);
		contentPane.add(lbLocationThermalImage_1);

		lbLocationThermalImage_2 = new JLabel("Location:");
		lbLocationThermalImage_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationThermalImage_2.setBounds(688, 678, 63, 14);
		contentPane.add(lbLocationThermalImage_2);

		lbLocationMaxTemperatureValue = new JLabel("");
		lbLocationMaxTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationMaxTemperatureValue.setBounds(761, 641, 78, 14);
		contentPane.add(lbLocationMaxTemperatureValue);

		lbLocationMinTemperatureValue = new JLabel("");
		lbLocationMinTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationMinTemperatureValue.setBounds(761, 678, 78, 14);
		contentPane.add(lbLocationMinTemperatureValue);

		lbMeanTemperature = new JLabel("Mean Temperature:");
		lbMeanTemperature.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbMeanTemperature.setBounds(446, 714, 129, 14);
		contentPane.add(lbMeanTemperature);

		lbMeanTemperatureValue = new JLabel("");
		lbMeanTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbMeanTemperatureValue.setBounds(582, 714, 68, 14);
		contentPane.add(lbMeanTemperatureValue);

		btnSelectOutputFolder = new JButton("Select output folder");
		btnSelectOutputFolder.setForeground(Color.WHITE);
		btnSelectOutputFolder.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSelectOutputFolder.setBorderPainted(false);
		btnSelectOutputFolder.setBackground(new Color(0, 128, 0));
		btnSelectOutputFolder.setBounds(182, 13, 160, 36);
		contentPane.add(btnSelectOutputFolder);

		txtOutputFolderEmbeddedFilePath = new JTextField();
		txtOutputFolderEmbeddedFilePath.setEditable(false);
		txtOutputFolderEmbeddedFilePath.setBounds(352, 21, 280, 20);
		contentPane.add(txtOutputFolderEmbeddedFilePath);
		txtOutputFolderEmbeddedFilePath.setColumns(10);

		// file chooser
		jFileChooser = new JFileChooser();
		settingJFileChooser(jFileChooser);
		handleEvent();
	}

	private void settingJFileChooser(JFileChooser jFileChooser) {
		FileFilter imageFileFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
		componentSettingCommon.settingJFileChooser(jFileChooser, "Please select your images", JFileChooser.FILES_ONLY, true, imageFileFilter,
				appConfiguration.getInitCurrentDirectoryFileChooser());
	}

	private void handleEvent() {
		btnLoadImage_Click();
		listImage_ValueChanged();
		lbLoadInputImage_MouseMoved();
		lbLoadInputImage_MouseExited();
		lbLoadOutputImage_MouseMoved();
		lbLoadOutputImage_MouseExited();
//		btnExtractAll_Click();
		btnSelectOutputFolder_Click();
	}

	private void btnSelectOutputFolder_Click() {
		btnSelectOutputFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jFileChooser = new JFileChooser();
				componentSettingCommon.settingJFileChooser(jFileChooser, "Please select a folder to save your image", JFileChooser.DIRECTORIES_ONLY,
						false, null, appConfiguration.getInitCurrentDirectoryFileChooser());

				if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
					String outputFolderEmbedded = jFileChooser.getSelectedFile().getAbsolutePath();

					txtOutputFolderEmbeddedFilePath.setText(outputFolderEmbedded);
				}
			}
		});
	}

//	private void btnExtractAll_Click() {
//		btnExtractAll.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (listImageData == null || listImageData.size() == 0) {
//					JOptionPane.showMessageDialog(null, "Empty list image!!!", "WARNING", JOptionPane.WARNING_MESSAGE);
//					return;
//				}
//
//				if (txtOutputFolderEmbeddedFilePath == null || txtOutputFolderEmbeddedFilePath.getText() == null
//						|| txtOutputFolderEmbeddedFilePath.getText().isBlank()) {
//					JOptionPane.showMessageDialog(null, "Please select the output folder to store the data!!!", "WARNING",
//							JOptionPane.WARNING_MESSAGE);
//					return;
//				}
//
//				for (String imagePath : listImageData) {
//					extractAllEmbeddedFile(imagePath, txtOutputFolderEmbeddedFilePath.getText());
//				}
//
//				JOptionPane.showMessageDialog(null, "Extract embedded file successfully", "INFO", JOptionPane.INFORMATION_MESSAGE);
//
//			}
//		});
//	}

	private void lbLoadInputImage_MouseExited() {
		lbLoadInputImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				lbTemperatureValue.setVisible(false);
				lbLocationThermalImageValue.setVisible(false);
			}
		});
	}

	private void lbLoadOutputImage_MouseExited() {
		lbLoadOutputVisionImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				lbLocationVisionImageValue.setVisible(false);
			}
		});
	}

	private void lbLoadOutputImage_MouseMoved() {
		lbLoadOutputVisionImage.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				lbLocationVisionImageValue.setVisible(true);
				int x = e.getX();
				int y = e.getY();
				String locationValue = new StringBuffer("").append("(").append(x).append(",").append(y).append(")").toString();
				lbLocationVisionImageValue.setText(locationValue);
			}
		});
	}

	private void lbLoadInputImage_MouseMoved() {
		lbLoadInputImage.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				lbLocationThermalImageValue.setVisible(true);
				lbTemperatureValue.setVisible(true);
				if (listImage.getSelectedIndex() >= 0) {
					int x = e.getX();
					int y = e.getY();

					if (x >= 0 && y >= 0 && x < temperatureMap.cols() && y < temperatureMap.rows()) {
						String locationValue = new StringBuffer("").append("(").append(x).append(",").append(y).append(")").toString();

						lbLocationThermalImageValue.setText(locationValue);

						// get temperature
						double temperatureAtLocation = temperatureMap.get(y, x)[0];
						String temperatureValue = new StringBuffer("").append(String.format("%.2f", temperatureAtLocation)).append("°C").toString();
						lbTemperatureValue.setText(temperatureValue);
					}

				}

			}

		});
	}
	
    private String removeChineseCharacters(String inputString) {
        return inputString.replaceAll("[\\u4E00-\\u9FA5]", "");
    }

	private void btnLoadImage_Click() {
		btnLoadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtOutputFolderEmbeddedFilePath == null || txtOutputFolderEmbeddedFilePath.getText() == null
						|| txtOutputFolderEmbeddedFilePath.getText().isBlank()) {
					JOptionPane.showMessageDialog(null, "Please select the output folder to store the data!!!", "WARNING",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				listImageData.clear();
				
				if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
					File[] selectedFiles = jFileChooser.getSelectedFiles();
					String cleanPath = "";
					for (File file : selectedFiles) {
//						cleanPath = removeChineseCharacters(file.getAbsolutePath());
//						listImageData.add(cleanPath);
						listImageData.add(file.getAbsolutePath());
						extractAllEmbeddedFile(file.getAbsolutePath(), txtOutputFolderEmbeddedFilePath.getText());
					}
					
					listImage.setListData(listImageData);
				}
			}
		});
	}

	private void listImage_ValueChanged() {
		listImage.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (txtOutputFolderEmbeddedFilePath == null || txtOutputFolderEmbeddedFilePath.getText() == null
						|| txtOutputFolderEmbeddedFilePath.getText().isBlank()) {
					JOptionPane.showMessageDialog(null, "Please select the output folder to store the data!!!", "WARNING",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				String pathImageSelected = listImage.getSelectedValue();
				displayResult(pathImageSelected);
			}

		});
	}

	private void displayResult(String pathImageSelected) {
		// read metadata parameter
		String metadataFilePath = fileService.getOutputFilePath(pathImageSelected, "metadata.txt", txtOutputFolderEmbeddedFilePath.getText());
		mapMetadataParams = fileService.readFlirMetadataParams(metadataFilePath);

		// get temperature map of raw thermal image
		temperatureMap = getTemperatureMap(mapMetadataParams, pathImageSelected);
		
		// load thermal image to label
		imageCommonHandle.loadImageFromPathToLabel(pathImageSelected, lbLoadInputImage, panelInputImage);
		lbLocationThermalImageValue.setVisible(true);

		// get max, min, mean temperature of raw thermal image
		displayMinMaxMeanTemperature();

		// show vision image
		showVisionImage2(pathImageSelected);
		lbLocationVisionImageValue.setVisible(true);
	}

	private void showVisionImage2(String pathImageSelected) {
		String visionImagePath = fileService.getOutputFilePath(pathImageSelected, "vision.png", txtOutputFolderEmbeddedFilePath.getText());
		imageCommonHandle.loadImageFromPathToLabel(visionImagePath, lbLoadOutputVisionImage, panelOutputVisionImage);
	}

	private void extractAllEmbeddedFile(String pathImageSelected, String outputFolderEmbedded) {
		// extract metadata of thermal image
		extractMetadataOfThermalImage(pathImageSelected, outputFolderEmbedded);

		// extract embedded image of thermal image
		extractEmbeddedImageOfThermalImage(pathImageSelected, outputFolderEmbedded);

		// extract raw thermal image
		extractRawThermalImageOfThermalImage(pathImageSelected, outputFolderEmbedded);
		
		// extract vision image
		extractVisionImageOfThermalImage(pathImageSelected, outputFolderEmbedded);

	}

	private void extractVisionImageOfThermalImage(String pathImageSelected, String outputFolderEmbedded) {
		String embeddedImagePath = fileService.getOutputFilePath(pathImageSelected, "embedded.png", txtOutputFolderEmbeddedFilePath.getText());
		String metadataFilePath = fileService.getOutputFilePath(pathImageSelected, "metadata.txt", txtOutputFolderEmbeddedFilePath.getText());
		
		if (metadataFilePath == null) {
			System.err.println("Cannot extract metadata file of: " + pathImageSelected);
			return;
		}
		
		if (embeddedImagePath == null) {
			System.err.println("Cannot extract embedded image at: " + pathImageSelected);
			return;
		}
		
		fileService.createOutputDirectory(outputFolderEmbedded + "\\Vision");
		
		mapMetadataParams = fileService.readFlirMetadataParams(metadataFilePath);
		try {
			File embeddedFile = new File(embeddedImagePath);

			if (embeddedFile.exists() && embeddedFile.length() > 0) {
				Mat matEmbeddedImage = Imgcodecs.imread(embeddedImagePath, Imgcodecs.IMREAD_UNCHANGED);
				Mat matVisionImage = getMatVisionImage(matEmbeddedImage, mapMetadataParams);

				String visionImagePath = fileService.getOutputFilePath(pathImageSelected, "vision.png", txtOutputFolderEmbeddedFilePath.getText());
				Imgcodecs.imwrite(visionImagePath, matVisionImage);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			System.err.println("Cannot extract vision image at: " + pathImageSelected);
			return;
		}
		
	}

	private Mat getMatVisionImage(Mat matEmbeddedImage, Map<String, String> mapMetadataParams) {
		// get metadata parameter from metadata file
		flirMetadataParam = commonParseValue.parseMetadata(mapMetadataParams);
		double real2IR = flirMetadataParam.getReal2IR();
		double offsetX = flirMetadataParam.getOffsetX();
		double offsetY = flirMetadataParam.getOffsetY();
		double rawThermalImageWidth = flirMetadataParam.getRawThermalImageWidth();
		double rawThermalImageHeight = flirMetadataParam.getRawThermalImageHeight();
		double embeddedImageWidth = flirMetadataParam.getEmbeddedImageWidth();
		double embeddedImageHeight = flirMetadataParam.getEmbeddedImageHeight();
		double pipX1 = flirMetadataParam.getPipX1();
		double pipX2 = flirMetadataParam.getPipX2();
		double pipY1 = flirMetadataParam.getPipY1();
		double pipY2 = flirMetadataParam.getPipY2();

		int origHeight = matEmbeddedImage.rows();
		int origWidth = matEmbeddedImage.cols();

		// Step 1: Scale embedded image by Real_2_IR
		int scaledHeight = (int) (origHeight * real2IR);
		int scaledWidth = (int) (origWidth * real2IR);

		Mat imgScaled = new Mat(scaledHeight, scaledWidth, matEmbeddedImage.type());
		Imgproc.resize(matEmbeddedImage, imgScaled, imgScaled.size(), 0, 0, Imgproc.INTER_LINEAR);

		int cx = (int) (scaledWidth / 2.0 + offsetX * real2IR);
		int cy = (int) (scaledHeight / 2.0 + offsetY * real2IR);

		if (cy < origHeight / 2) {
			cy = origHeight / 2;
		}

		if (cx < origWidth / 2) {
			cx = origWidth / 2;
		}

		// Prevent negative crop end coordinates
		if (cy + origHeight / 2 > scaledHeight) {
			cy = scaledHeight - origHeight / 2;
		}
		if (cx + origWidth / 2 > scaledWidth) {
			cx = scaledWidth - origWidth / 2;
		}

		// Crop region of interest (ROI)
		int y1 = cy - origHeight / 2;
		int y2 = cy + origHeight / 2;
		int x1 = cx - origWidth / 2;
		int x2 = cx + origWidth / 2;

		// Ensure ROI inside bounds
		y1 = Math.max(0, y1);
		y2 = Math.min(scaledHeight, y2);
		x1 = Math.max(0, x1);
		x2 = Math.min(scaledWidth, x2);

		Rect roi = new Rect(x1, y1, x2 - x1, y2 - y1);
		Mat imgCropped = new Mat(imgScaled, roi);

		// Resize cropped image to final vision size
		Mat imgVision = new Mat((int) rawThermalImageHeight, (int) rawThermalImageWidth, matEmbeddedImage.type());
		Imgproc.resize(imgCropped, imgVision, imgVision.size(), 0, 0, Imgproc.INTER_LINEAR);

		// shift left 13 pixel
		int shiftX = -13;
		int shiftY = 0;

		// Create 2x3 translation matrix
		Mat translationMatrix = new Mat(2, 3, CvType.CV_32F);
		translationMatrix.put(0, 0, 1);
		translationMatrix.put(0, 1, 0);
		translationMatrix.put(0, 2, shiftX);
		translationMatrix.put(1, 0, 0);
		translationMatrix.put(1, 1, 1);
		translationMatrix.put(1, 2, shiftY);

		Mat imageVisionshifted = new Mat();
		Imgproc.warpAffine(imgVision, imageVisionshifted, translationMatrix, imgVision.size(), Imgproc.INTER_LINEAR, Core.BORDER_REPLICATE);

		return imageVisionshifted;

	}

	private void extractRawThermalImageOfThermalImage(String pathImageSelected, String outputFolderEmbedded) {
		String messageExtractRawThermalImageFile = null;
		messageExtractRawThermalImageFile = fileService.extractRawThermalImageFile(pathImageSelected, outputFolderEmbedded);

		switch (messageExtractRawThermalImageFile) {
		case Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_SUCCESS:
			System.out.println(messageExtractRawThermalImageFile + " " + pathImageSelected);
			break;
		default:
			System.err.println(messageExtractRawThermalImageFile + " " + pathImageSelected);
			break;
		}
	}
	
	private void extractMetadataOfThermalImage(String pathImageSelected, String outputFolderEmbedded) {
		String messageExtractMetadataFile = null;
		messageExtractMetadataFile = fileService.extractMetadataFile(pathImageSelected, outputFolderEmbedded);

		switch (messageExtractMetadataFile) {
		case Constant.MESSAGE_EXTRACT_METADATA_FILE_SUCCESS:
			System.out.println(messageExtractMetadataFile + " " + pathImageSelected);
			break;
		default:
			System.err.println(messageExtractMetadataFile + " " + pathImageSelected);
			break;
		}
	}

	private void extractEmbeddedImageOfThermalImage(String pathImageSelected, String outputFolderEmbedded) {
		String messageExtractEmbeddedImageFile = null;
		messageExtractEmbeddedImageFile = fileService.extractEmbeddedImageFile(pathImageSelected, outputFolderEmbedded);

		switch (messageExtractEmbeddedImageFile) {
		case Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_SUCCESS:
			System.out.println(messageExtractEmbeddedImageFile + " " + pathImageSelected);
			break;
		default:
			System.err.println(messageExtractEmbeddedImageFile + " " + pathImageSelected);
			break;
		}
	}

	private Mat getTemperatureMap(Map<String, String> mapMetadataParams, String pathImageSelected) {
		String rawThermalImageSelectedPath = fileService.getOutputFilePath(pathImageSelected, "raw_thermal.png",
				txtOutputFolderEmbeddedFilePath.getText());

		// get metadata parameter from metadata file
		flirMetadataParam = commonParseValue.parseMetadata(mapMetadataParams);

		Mat raw16 = Imgcodecs.imread(rawThermalImageSelectedPath, Imgcodecs.IMREAD_ANYDEPTH);

		if (raw16.empty()) {
			JOptionPane.showMessageDialog(null, "Cannot load thermal image: " + rawThermalImageSelectedPath, "WARNING", JOptionPane.WARNING_MESSAGE);
			return new Mat();
		}

		raw16 = convertBigEndianToLittleEndian(raw16);

		String rawThermalLittleEndianPath = fileService.getOutputFilePath(pathImageSelected, "raw_thermal_little_endian.png",
				txtOutputFolderEmbeddedFilePath.getText());
		Imgcodecs.imwrite(rawThermalLittleEndianPath, raw16);

		Mat temperatureMat = new Mat(raw16.size(), CvType.CV_64F);

		double rawVal = 0.0;
		double temp = 0.0;

		for (int y = 0; y < raw16.rows(); y++) {
			for (int x = 0; x < raw16.cols(); x++) {
				rawVal = raw16.get(y, x)[0];
				temp = getTemperatureOfPixelReality(flirMetadataParam, rawVal);
				temperatureMat.put(y, x, temp);
			}
		}

		return temperatureMat;

	}

	private Mat convertBigEndianToLittleEndian(Mat bigEndianMat) {
		if (bigEndianMat == null || bigEndianMat.empty()) {
			System.err.println("bigEndianMat is null or empty");
			return new Mat();
//			throw new IllegalArgumentException("Input is null or empty");
		}
		if (bigEndianMat.type() != CvType.CV_16U) {
			System.err.println("bigEndianMat must be 16-bit unsigned (CV_16U)");
			return new Mat();
//			throw new IllegalArgumentException("Matrix must be 16-bit unsigned (CV_16U)");
		}

		// Create mask Mat with 0x00FF (low byte mask)
		Mat lowMask = new Mat(bigEndianMat.size(), bigEndianMat.type(), new Scalar(0x00FF));
		Mat lowBytes = new Mat();
		Core.bitwise_and(bigEndianMat, lowMask, lowBytes);

		// Get high bytes: integer divide by 256 (equivalent to >> 8)
		Mat highBytes = new Mat();
		Core.divide(bigEndianMat, new Scalar(256.0), highBytes); // highBytes now holds values 0..255

		// Now low becomes high: low * 256
		Mat lowShifted = new Mat();
		Core.multiply(lowBytes, new Scalar(256.0), lowShifted); // lowByte << 8

		// Combine
		Mat littleEndian = new Mat();
		Core.add(lowShifted, highBytes, littleEndian); // final 16-bit swapped values

		// release temporaries
		lowMask.release();
		lowBytes.release();
		highBytes.release();
		lowShifted.release();

		return littleEndian;
	}

	private double getTemperatureOfPixelReality(FlirMetadataParam flirMetadataParam, double rawVal) {
		double emissivity = flirMetadataParam.getEmissivity(); // 0.85;
		double objectDistance = flirMetadataParam.getObjectDistance(); // 1.00;
		double reflectedApparentTemperature = flirMetadataParam.getReflectedApparentTemperature(); // 24.00;
		double atmosphericTemperature = flirMetadataParam.getAtmosphericTemperature(); // 23.00;
		double irWindowTemperature = flirMetadataParam.getIrWindowTemperature(); // 18.00;
		double irWindowTransmission = flirMetadataParam.getIrWindowTransmission(); // 1.00;
		double relativeHumidity = flirMetadataParam.getRelativeHumidity(); // 65.00;
		double planckR1 = flirMetadataParam.getPlanckR1(); // 16788.896;
		double planckB = flirMetadataParam.getPlanckB(); // 1432.20;
		double planckF = flirMetadataParam.getPlanckF(); // 1.00;
		double planckO = flirMetadataParam.getPlanckO(); // -4159.00;
		double planckR2 = flirMetadataParam.getPlanckR2(); // 0.011489335;
		double atmosphericTransAlpha1 = flirMetadataParam.getAtmosphericTransAlpha1(); // 0.00;
		double atmosphericTransAlpha2 = flirMetadataParam.getAtmosphericTransAlpha2(); // 0.00;
		double atmosphericTransBeta1 = flirMetadataParam.getAtmosphericTransBeta1(); // 0.003180;
		double atmosphericTransBeta2 = flirMetadataParam.getAtmosphericTransBeta2(); // 0.003180;
		double atmosphericTransX = flirMetadataParam.getAtmosphericTransX(); // 0.732000;
		double emissWind = 1 - irWindowTransmission; // Window emissivity
		double reflectWind = 0.0; // anti-reflective coating on window

		// Link reference formula
		// https://flir.custhelp.com/app/answers/detail/a_id/3321/~/flir-cameras---temperature-measurement-formula
		// https://github.com/gtatters/Thermimage/blob/master/R/raw2temp.R
		double h2o = 0.0; // water vapour pressure
		h2o = (0.01 * relativeHumidity) * Math.exp(1.5587 + 0.06939 * atmosphericTemperature - 0.00027816 * Math.pow(atmosphericTemperature, 2)
				+ 0.00000068455 * Math.pow(atmosphericTemperature, 3));

		// calculate atmospheric correction factor (tau). Transmission through
		// atmosphere
		double tau = atmosphericTransX * Math.exp(-Math.sqrt(objectDistance / 2) * (atmosphericTransAlpha1 + atmosphericTransBeta1 * Math.sqrt(h2o)))
				+ (1 - atmosphericTransX)
						* Math.exp(-Math.sqrt(objectDistance / 2) * (atmosphericTransAlpha2 + atmosphericTransBeta2 * Math.sqrt(h2o)));

		// calculate radiance reflected by the surface. Radiance reflecting off the
		// object before the window
		double rawRefl1 = planckR1 / (planckR2 * (Math.exp(planckB / (reflectedApparentTemperature + Constant.ZERO_KELVIN)) - planckF)) - planckO;

		// calculate fraction of the thermal radiation flux incident on the sensor.
		// Attenuated radiance reflecting off the object before the window
		double rawRefl1Attn = (1 - emissivity) * rawRefl1 / emissivity;

		// calculate radiance of the atmosphere. Radiance from the atmosphere before the
		// window
		double rawAtm1 = planckR1 / (planckR2 * (Math.exp(planckB / (atmosphericTemperature + Constant.ZERO_KELVIN)) - planckF)) - planckO;

		/*
		 * calculate the fraction of the atmosphere’s thermal radiation of the radiation
		 * flux arriving at the sensor Attenuated radiance from the atmosphere before
		 * the window
		 */
		double rawAtm1Attn = ((1 - tau) * rawAtm1) / (emissivity * tau);

		double rawWind = planckR1 / (planckR2 * (Math.exp(planckB / (irWindowTemperature + Constant.ZERO_KELVIN)) - planckF)) - planckO;
		double rawWindAttn = (emissWind * rawWind) / (emissivity * tau * irWindowTransmission);

		double rawRefl2 = rawRefl1;
		double rawRefl2Attn = (reflectWind * rawRefl2) / (emissivity * tau * irWindowTransmission);

		double rawAtm2 = rawAtm1;
		double rawAtm2Attn = ((1 - tau) * rawAtm2) / (emissivity * tau * tau * irWindowTransmission);

		// calculate the actual surface temperature (rawS) from the raw signal values
		// rawVal
		double rawObj = (rawVal) / (emissivity * tau * tau * irWindowTransmission) - rawAtm1Attn - rawAtm2Attn - rawWindAttn - rawRefl1Attn
				- rawRefl2Attn;

		// calculate temperature surface in Celsius
		double tempC = planckB / (Math.log(planckR1 / (planckR2 * (rawObj + planckO)) + planckF)) - Constant.ZERO_KELVIN;

		return tempC;

	}

	private void displayMinMaxMeanTemperature() {
		Core.MinMaxLocResult minMax = Core.minMaxLoc(temperatureMap);

		// max temperature
		double maxTemperature = minMax.maxVal;
		String maxTemperatureValue = new StringBuffer("").append(String.format("%.2f", maxTemperature)).append("°C").toString();
		lbMaxTemperatureValue.setText(maxTemperatureValue);

		// location of max temperature
		Point pointMaxTemperatureLocation = minMax.maxLoc;
		int xLocationMaxTemperature = (int) pointMaxTemperatureLocation.x;
		int yLocationMaxTemperature = (int) pointMaxTemperatureLocation.y;
		String locationMaxTemperature = new StringBuffer("").append("(").append(xLocationMaxTemperature).append(",").append(yLocationMaxTemperature)
				.append(")").toString();
		lbLocationMaxTemperatureValue.setText(locationMaxTemperature);

		// min temperature
		double minTemperature = minMax.minVal;
		String minTemperatureValue = new StringBuffer("").append(String.format("%.2f", minTemperature)).append("°C").toString();
		lbMinTemperatureValue.setText(minTemperatureValue);

		// location of min temperature
		Point pointMinTemperatureLocation = minMax.minLoc;
		int xLocationMinTemperature = (int) pointMinTemperatureLocation.x;
		int yLocationMinTemperature = (int) pointMinTemperatureLocation.y;
		String locationMinTemperature = new StringBuffer("").append("(").append(xLocationMinTemperature).append(",").append(yLocationMinTemperature)
				.append(")").toString();
		lbLocationMinTemperatureValue.setText(locationMinTemperature);

		// mean temperature
		Scalar meanScalar = Core.mean(temperatureMap);
		double meanTemperature = meanScalar.val[0];
		String meanTemperatureValue = new StringBuffer("").append(String.format("%.2f", meanTemperature)).append("°C").toString();
		lbMeanTemperatureValue.setText(meanTemperatureValue);

		// draw circle mark max + min temperature location
		((ImageLabelWithCircles) lbLoadInputImage).setTemperaturePoints(xLocationMaxTemperature, yLocationMaxTemperature, xLocationMinTemperature,
				yLocationMinTemperature);
	}
}
