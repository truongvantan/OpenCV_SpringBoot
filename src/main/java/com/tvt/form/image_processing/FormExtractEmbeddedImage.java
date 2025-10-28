package com.tvt.form.image_processing;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
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

	@Autowired
	private final AppConfiguration appConfiguration;

//	private static final String OUTPUT_EMBEDDED_IMAGE_FOLDER = "D:\\Tan\\Sample_Data\\Embedded_Image";

//	@Value("${output.embedded.image.folder}")
//	private String OUTPUT_EMBEDDED_IMAGE_FOLDER;
//	
//	@Value("${init.current.directory.file.chooser}")
//	private String INIT_CURRENT_DIRECTORY_FILE_CHOOSER;

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
	private DefaultListModel<String> listModel;
	private JScrollPane scrollPane;
	private JPanel panelInputImage;
	private JLabel lbThermalImage;

//	private JLabel lbLoadInputImage;
	private JLabel lbLoadInputImage;

	private JPanel panelOutputImage;
	private JLabel lbLoadOutputImage;
	private JLabel lbOutputImage;
	private JLabel lbLocationThermalImage;
	private JLabel lbLocationThermalImageValue;
	private JLabel lbLocationEmbeddedImage;
	private JLabel lbLocationEmbeddedImageValue;
//	private JnaFileChooser fileChooser;
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

//	public FormExtractEmbeddedImage() {
//		initComponent();
//	}

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
		this.setBounds(100, 100, 1487, 661);
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
		scrollPane.setBounds(30, 62, 327, 515);
		contentPane.add(scrollPane);

		listImage = new JList<>(listModel);
		listImage.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		scrollPane.setViewportView(listImage);

		panelInputImage = new JPanel();
		panelInputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelInputImage.setBounds(439, 62, 464, 348);
		contentPane.add(panelInputImage);
		panelInputImage.setLayout(null);

//		lbLoadInputImage = new JLabel("");
		lbLoadInputImage = new ImageLabelWithCircles();
		lbLoadInputImage.setBounds(0, 0, 464, 348);
		panelInputImage.add(lbLoadInputImage);

		lbThermalImage = new JLabel("Thermal Image");
		lbThermalImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbThermalImage.setBounds(439, 24, 95, 14);
		contentPane.add(lbThermalImage);

		panelOutputImage = new JPanel();
		panelOutputImage.setLayout(null);
		panelOutputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOutputImage.setBounds(993, 62, 464, 348);
		contentPane.add(panelOutputImage);

		lbLoadOutputImage = new JLabel("");
		lbLoadOutputImage.setBounds(0, 0, 464, 348);
		panelOutputImage.add(lbLoadOutputImage);

		lbOutputImage = new JLabel("Embedded Image");
		lbOutputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbOutputImage.setBounds(993, 22, 119, 14);
		contentPane.add(lbOutputImage);

		lbLocationThermalImage = new JLabel("Location:");
		lbLocationThermalImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationThermalImage.setBounds(446, 451, 63, 14);
		contentPane.add(lbLocationThermalImage);

		lbLocationThermalImageValue = new JLabel("");
		lbLocationThermalImageValue.setVisible(false);
		lbLocationThermalImageValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationThermalImageValue.setBounds(519, 452, 78, 14);
		contentPane.add(lbLocationThermalImageValue);

		lbLocationEmbeddedImage = new JLabel("Location:");
		lbLocationEmbeddedImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationEmbeddedImage.setBounds(993, 451, 63, 14);
		contentPane.add(lbLocationEmbeddedImage);

		lbLocationEmbeddedImageValue = new JLabel("");
		lbLocationEmbeddedImageValue.setVisible(false);
		lbLocationEmbeddedImageValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationEmbeddedImageValue.setBounds(1073, 451, 95, 14);
		contentPane.add(lbLocationEmbeddedImageValue);

		lblMaxTemperature = new JLabel("Max temperature:");
		lblMaxTemperature.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMaxTemperature.setBounds(446, 490, 129, 14);
		contentPane.add(lblMaxTemperature);

		lblMinTemperature = new JLabel("Min temperature:");
		lblMinTemperature.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMinTemperature.setBounds(446, 527, 119, 14);
		contentPane.add(lblMinTemperature);

		lblTemperature = new JLabel("Temperature:");
		lblTemperature.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTemperature.setBounds(643, 451, 95, 14);
		contentPane.add(lblTemperature);

		lbTemperatureValue = new JLabel("");
		lbTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbTemperatureValue.setBounds(748, 452, 68, 14);
		contentPane.add(lbTemperatureValue);

		lbMaxTemperatureValue = new JLabel("");
		lbMaxTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbMaxTemperatureValue.setBounds(572, 490, 68, 14);
		contentPane.add(lbMaxTemperatureValue);

		lbMinTemperatureValue = new JLabel("");
		lbMinTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbMinTemperatureValue.setBounds(572, 527, 68, 14);
		contentPane.add(lbMinTemperatureValue);

		lbLocationThermalImage_1 = new JLabel("Location:");
		lbLocationThermalImage_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationThermalImage_1.setBounds(643, 491, 63, 14);
		contentPane.add(lbLocationThermalImage_1);

		lbLocationThermalImage_2 = new JLabel("Location:");
		lbLocationThermalImage_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbLocationThermalImage_2.setBounds(643, 528, 63, 14);
		contentPane.add(lbLocationThermalImage_2);

		lbLocationMaxTemperatureValue = new JLabel("");
		lbLocationMaxTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationMaxTemperatureValue.setBounds(716, 491, 78, 14);
		contentPane.add(lbLocationMaxTemperatureValue);

		lbLocationMinTemperatureValue = new JLabel("");
		lbLocationMinTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbLocationMinTemperatureValue.setBounds(716, 528, 78, 14);
		contentPane.add(lbLocationMinTemperatureValue);
		
		lbMeanTemperature = new JLabel("Mean Temperature:");
		lbMeanTemperature.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbMeanTemperature.setBounds(446, 563, 129, 14);
		contentPane.add(lbMeanTemperature);
		
		lbMeanTemperatureValue = new JLabel("");
		lbMeanTemperatureValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbMeanTemperatureValue.setBounds(582, 563, 68, 14);
		contentPane.add(lbMeanTemperatureValue);

		// file chooser
		jFileChooser = new JFileChooser();
		settingJFileChooser(jFileChooser);

		handleEvent();
	}

	private void settingJFileChooser(JFileChooser fileChooser) {
		FileFilter imageFileFilter = new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes());
		componentSettingCommon.settingJFileChooser(fileChooser, "Please select your images", JFileChooser.FILES_ONLY, true, imageFileFilter,
				appConfiguration.getInitCurrentDirectoryFileChooser());
	}

	private void handleEvent() {
		btnLoadImage_Click();
		listImage_ValueChanged();
		lbLoadInputImage_MouseMoved();
		lbLoadInputImage_MouseExited();
		lbLoadOutputImage_MouseMoved();
		lbLoadOutputImage_MouseExited();
	}

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
		lbLoadOutputImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				lbLocationEmbeddedImageValue.setVisible(false);
			}
		});
	}

	private void lbLoadOutputImage_MouseMoved() {
		lbLoadOutputImage.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				lbLocationEmbeddedImageValue.setVisible(true);
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
//						double temperatureAtLocation = getTemperatureAtLocation(x, y, mapMetadataParams);

						String temperatureValue = new StringBuffer("").append(String.format("%.2f", temperatureAtLocation)).append("°C").toString();

						lbTemperatureValue.setText(temperatureValue);
					}

				}

			}

		});
	}

	private void btnLoadImage_Click() {
		btnLoadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
					File[] selectedFiles = jFileChooser.getSelectedFiles();
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

				// extract metadata of thermal image
				extractMetadataOfThermalImage(pathImageSelected);

				// read metadata parameter
				String metadataFilePath = fileService.getOutputFilePath(pathImageSelected, "metadata.txt");
				mapMetadataParams = fileService.readFlirMetadataParams(metadataFilePath);

				// extract embedded image of thermal image
				extractEmbeddedImageOfThermalImage(pathImageSelected);

				// extract raw thermal image
				extractRawThermalImageOfThermalImage(pathImageSelected);

				// get temperature map of raw thermal image
				temperatureMap = getTemperatureMap(mapMetadataParams);

				// load color thermal image color to label
//				imageCommonHandle.loadImageToLabel(pathImageSelected, lbLoadInputImage, panelInputImage);
				imageCommonHandle.loadImageToLabel2(pathImageSelected, lbLoadInputImage, panelInputImage);
				lbLocationThermalImageValue.setVisible(true);

				// get max, min, mean temperature of raw thermal image
				displayMinMaxMeanTemperature();

				// show extracted embedded image
//				showEmbeddedImage(pathImageSelected);

				// show vision image
				showVisionImage(pathImageSelected, mapMetadataParams);
			}

		});
	}

	private void showVisionImage(String pathImageSelected, Map<String, String> mapMetadataParams) {
		String embeddedImagePath = fileService.getOutputFilePath(pathImageSelected, "embedded.png");
		try {
			File embeddedFile = new File(embeddedImagePath);

			if (embeddedFile.exists() && embeddedFile.length() > 0) {
				Mat matEmbeddedImage = Imgcodecs.imread(embeddedImagePath, Imgcodecs.IMREAD_UNCHANGED);
				Mat matVisionImage = getMatVisionImage(matEmbeddedImage, mapMetadataParams);

				imageCommonHandle.loadCVMatToLabel(matVisionImage, lbLoadOutputImage, panelOutputImage);
				String visionImagePath = fileService.getOutputFilePath(pathImageSelected, "vision.png");
				Imgcodecs.imwrite(visionImagePath, matVisionImage);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract embedded image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
			return;
		}
		lbLocationEmbeddedImageValue.setVisible(true);
	}

	private Mat getMatVisionImage(Mat matEmbeddedImage, Map<String, String> mapMetadataParams2) {
		// get metadata parameter from metadata file
		flirMetadataParam = commonParseValue.parseMetadata(mapMetadataParams);
		double real2IR = flirMetadataParam.getReal2IR();
		double offsetX = flirMetadataParam.getOffsetX();
		double offsetY = flirMetadataParam.getOffsetY();
		double rawThermalImageWidth = flirMetadataParam.getRawThermalImageWidth();
		double rawThermalImageHeight = flirMetadataParam.getRawThermalImageHeight();

		Mat imgScaled = new Mat((int) (matEmbeddedImage.rows() * real2IR), (int) (matEmbeddedImage.cols() * real2IR), matEmbeddedImage.type());
		Imgproc.resize(matEmbeddedImage, imgScaled, imgScaled.size(), 0, 0, Imgproc.INTER_LINEAR);
		int width = (int) matEmbeddedImage.size().width;
		int height = (int) matEmbeddedImage.size().height;

		int widthScale = (int) imgScaled.size().width;
		int heightScale = (int) imgScaled.size().height;
		int cx = (int) (widthScale / 2 + offsetX * real2IR);
		int cy = (int) (heightScale / 2 + offsetY * real2IR);

		if (cy < height / 2) {
			cy = height / 2;
		}

		if (cx < width / 2) {
			cx = width / 2;
		}

		// Prevent negative crop end coordinates
		if (cy + height / 2 < 0) {
			cy = -(height / 2);
		}
		if (cx + width / 2 < 0) {
			cx = -(width / 2);
		}

		// Crop region of interest (ROI)
		int y1 = cy - height / 2;
		int y2 = cy + height / 2;
		int x1 = cx - width / 2;
		int x2 = cx + width / 2;

		// Ensure ROI inside bounds
		y1 = Math.max(0, Math.min(y1, heightScale - 1));
		y2 = Math.max(0, Math.min(y2, heightScale));
		x1 = Math.max(0, Math.min(x1, widthScale - 1));
		x2 = Math.max(0, Math.min(x2, widthScale));

		Rect roi = new Rect(x1, y1, x2 - x1, y2 - y1);
		Mat imgCropped = new Mat(imgScaled, roi);

		// Resize cropped image to final vision size
		Mat imgVision = new Mat((int) rawThermalImageHeight, (int) rawThermalImageWidth, matEmbeddedImage.type());
		Imgproc.resize(imgCropped, imgVision, imgVision.size(), 0, 0, Imgproc.INTER_LINEAR);

		return imgVision;
	}

	private void extractRawThermalImageOfThermalImage(String pathImageSelected) {
		String messageExtractRawThermalImageFile = null;
		messageExtractRawThermalImageFile = fileService.extractRawThermalImageFile(pathImageSelected);

		switch (messageExtractRawThermalImageFile) {
		case Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_SUCCESS:
			JOptionPane.showMessageDialog(null, messageExtractRawThermalImageFile, "INFO", JOptionPane.INFORMATION_MESSAGE);
			break;
		default:
			JOptionPane.showMessageDialog(null, messageExtractRawThermalImageFile + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
			break;
		}
	}

	private void extractMetadataOfThermalImage(String pathImageSelected) {
		String messageExtractMetadataFile = null;
		messageExtractMetadataFile = fileService.extractMetadataFile(pathImageSelected);

		switch (messageExtractMetadataFile) {
		case Constant.MESSAGE_EXTRACT_METADATA_FILE_SUCCESS:
			JOptionPane.showMessageDialog(null, messageExtractMetadataFile, "INFO", JOptionPane.INFORMATION_MESSAGE);
			break;
		default:
			JOptionPane.showMessageDialog(null, messageExtractMetadataFile + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
			break;
		}
	}

	private void extractEmbeddedImageOfThermalImage(String pathImageSelected) {
		String messageExtractEmbeddedImageFile = null;
		messageExtractEmbeddedImageFile = fileService.extractEmbeddedImageFile(pathImageSelected);

		switch (messageExtractEmbeddedImageFile) {
		case Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_SUCCESS:
			JOptionPane.showMessageDialog(null, messageExtractEmbeddedImageFile, "INFO", JOptionPane.INFORMATION_MESSAGE);
			break;
		default:
			JOptionPane.showMessageDialog(null, messageExtractEmbeddedImageFile, "INFO", JOptionPane.INFORMATION_MESSAGE);
			break;
		}
	}

	private void showEmbeddedImage(String pathImageSelected) {
		String embeddedImagePath = fileService.getOutputFilePath(pathImageSelected, "embedded.png");
		try {
			File embeddedFile = new File(embeddedImagePath);

			if (embeddedFile.exists() && embeddedFile.length() > 0) {
				imageCommonHandle.loadImageToLabel(embeddedImagePath, lbLoadOutputImage, panelOutputImage);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			JOptionPane.showMessageDialog(null, "Cannot extract embedded image: " + pathImageSelected, "WARNING", JOptionPane.WARNING_MESSAGE);
		}
		lbLocationEmbeddedImageValue.setVisible(true);
	}

//	private double getTemperatureAtLocation(int x, int y, Map<String, Double> params) {
//		String pathImageSelected = listImage.getSelectedValue();
//		String rawThermalImageSelectedPath = fileService.getOutputFilePath(pathImageSelected, "raw_thermal.png");
//		double R1 = params.get("Planck R1");
//        double R2 = params.get("Planck R2");
//        double B  = params.get("Planck B");
//        double O  = params.get("Planck O");
//        double F  = params.get("Planck F");
//		
////		Mat raw16 = Imgcodecs.imread(rawThermalImageSelectedPath, Imgcodecs.IMREAD_ANYDEPTH);
//		Mat raw16 = Imgcodecs.imread(rawThermalImageSelectedPath, Imgcodecs.IMREAD_UNCHANGED);
//		int type = raw16.type();
//		
//		if (raw16.empty()) {
//			JOptionPane.showMessageDialog(null, "Cannot load thermal image: " + rawThermalImageSelectedPath, "WARNING",
//					JOptionPane.WARNING_MESSAGE);
//            return Double.MIN_VALUE;
//        }
//
//        double rawVal = raw16.get(y, x)[0];
//        
//        double tempK = B / Math.log(R1 / (R2 * (rawVal - O)) + F);
//        double tempCelsius = tempK - Constant.ZERO_KELVIN;
//        
//        return tempCelsius;
//	}

//	private Mat getTemperatureMap(Map<String, Double> params) {
//		String pathImageSelected = listImage.getSelectedValue();
//		String rawThermalImageSelectedPath = fileService.getOutputFilePath(pathImageSelected, "raw_thermal.png");
//		double R1 = params.get("Planck R1");
//        double R2 = params.get("Planck R2");
//        double B  = params.get("Planck B");
//        double O  = params.get("Planck O");
//        double F  = params.get("Planck F");
//		
//		Mat raw16 = Imgcodecs.imread(rawThermalImageSelectedPath, Imgcodecs.IMREAD_UNCHANGED);
//		Mat temperatureMap = new Mat(raw16.size(), CvType.CV_32F);
//		
//		for (int y = 0; y < raw16.rows(); y++) {
//            for (int x = 0; x < raw16.cols(); x++) {
//                double rawVal = raw16.get(y, x)[0];
//                double tempK = B / Math.log(R1 / (R2 * (rawVal - O)) + F);
//                double tempC = tempK - Constant.ZERO_KELVIN;
//                temperatureMap.put(y, x, tempC);
//            }
//        }
//		
//		return temperatureMap;
//	}

	private Mat getTemperatureMap(Map<String, String> mapMetadataParams) {
		String pathImageSelected = listImage.getSelectedValue();
		String rawThermalImageSelectedPath = fileService.getOutputFilePath(pathImageSelected, "raw_thermal.png");

		// get metadata parameter from metadata file
		flirMetadataParam = commonParseValue.parseMetadata(mapMetadataParams);

		Mat raw16 = Imgcodecs.imread(rawThermalImageSelectedPath, Imgcodecs.IMREAD_ANYDEPTH);
//		Mat raw16 = Imgcodecs.imread(rawThermalImageSelectedPath, Imgcodecs.IMREAD_UNCHANGED);

		if (raw16.empty()) {
			JOptionPane.showMessageDialog(null, "Cannot load thermal image: " + rawThermalImageSelectedPath, "WARNING", JOptionPane.WARNING_MESSAGE);
		}

		raw16 = convertBigEndianToLittleEndian(raw16);

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
			throw new IllegalArgumentException("Input is null or empty");
		}
		if (bigEndianMat.type() != CvType.CV_16U) {
			throw new IllegalArgumentException("Matrix must be 16-bit unsigned (CV_16U)");
		}

		// Create mask Mat with 0x00FF (low byte mask)
		Mat lowMask = new Mat(bigEndianMat.size(), bigEndianMat.type(), new Scalar(0x00FF));
		Mat lowBytes = new Mat();
		Core.bitwise_and(bigEndianMat, lowMask, lowBytes); // <-- correct usage

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
		double emissWind = 1 - irWindowTransmission; //  Window emissivity
		double reflectWind = 0.0; // anti-reflective coating on window
		
		// Link reference formula
		// https://flir.custhelp.com/app/answers/detail/a_id/3321/~/flir-cameras---temperature-measurement-formula
		// https://github.com/gtatters/Thermimage/blob/master/R/raw2temp.R
		double h2o = 0.0; // water vapour pressure
		h2o = (0.01 * relativeHumidity) * Math.exp(1.5587 + 0.06939 * atmosphericTemperature - 0.00027816 * Math.pow(atmosphericTemperature, 2)
				+ 0.00000068455 * Math.pow(atmosphericTemperature, 3));

		// calculate atmospheric correction factor (tau). Transmission through atmosphere
		double tau = atmosphericTransX * Math.exp(-Math.sqrt(objectDistance / 2) * (atmosphericTransAlpha1 + atmosphericTransBeta1 * Math.sqrt(h2o)))
				+ (1 - atmosphericTransX)
						* Math.exp(-Math.sqrt(objectDistance / 2) * (atmosphericTransAlpha2 + atmosphericTransBeta2 * Math.sqrt(h2o)));
		
		
		// calculate radiance reflected by the surface. Radiance reflecting off the object before the window
		double rawRefl1 = planckR1 / (planckR2 * (Math.exp(planckB / (reflectedApparentTemperature + Constant.ZERO_KELVIN)) - planckF)) - planckO;
		
		// calculate fraction of the thermal radiation flux incident on the sensor. Attenuated radiance reflecting off the object before the window
		double rawRefl1Attn = (1 - emissivity) * rawRefl1 / emissivity;
		
		
		// calculate radiance of the atmosphere. Radiance from the atmosphere before the window
		double rawAtm1 = planckR1 / (planckR2 * (Math.exp(planckB / (atmosphericTemperature + Constant.ZERO_KELVIN)) - planckF)) - planckO;

		/* 
		 * calculate the fraction of the atmosphere’s thermal radiation of the radiation flux arriving at the sensor
		 * Attenuated radiance from the atmosphere before the window
		 * */
		double rawAtm1Attn = ((1-tau) * rawAtm1) / (emissivity * tau);
		
		double rawWind = planckR1 / (planckR2 * (Math.exp(planckB / (irWindowTemperature + Constant.ZERO_KELVIN)) - planckF)) - planckO;
		double rawWindAttn = (emissWind*rawWind) / (emissivity * tau * irWindowTransmission);
		
		double rawRefl2 = rawRefl1;
		double rawRefl2Attn = (reflectWind * rawRefl2) / (emissivity * tau * irWindowTransmission);
		
		double rawAtm2 = rawAtm1;
		double rawAtm2Attn = ((1-tau) * rawAtm2) / (emissivity * tau * tau * irWindowTransmission );
		
		// calculate the actual surface temperature (rawS) from the raw signal values
		// rawVal
		double rawObj = (rawVal) / (emissivity * tau * tau * irWindowTransmission) - rawAtm1Attn - rawAtm2Attn - rawWindAttn - rawRefl1Attn - rawRefl2Attn;

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
		((ImageLabelWithCircles) lbLoadInputImage).setTemperaturePoints(xLocationMaxTemperature, yLocationMaxTemperature, xLocationMinTemperature, yLocationMinTemperature);
	}

}
