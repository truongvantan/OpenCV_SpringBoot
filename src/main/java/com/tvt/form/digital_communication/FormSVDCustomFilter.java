package com.tvt.form.digital_communication;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tvt.common.ComponentSettingCommon;
import com.tvt.common.ImageCommonHandle;
import com.tvt.config.AppConfiguration;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import java.awt.Color;
import javax.swing.DefaultComboBoxModel;
import com.tvt.form.digital_communication.FormSVDCustomFilter.FilterType;

@Component
public class FormSVDCustomFilter extends JFrame {

	private static final long serialVersionUID = 1L;
	private final AppConfiguration appConfiguration;

	private ComponentSettingCommon componentSettingCommon = new ComponentSettingCommon();
	private ImageCommonHandle imageCommonHandle = new ImageCommonHandle();

	private JPanel contentPane;
	private JPanel panelLoadInputImage;
	private JLabel lblNewLabel;
	private JLabel lblOutputImage;
	private JPanel panelLoadOutputImage;
	private JScrollPane scrollPane;
	private JList<String> jListImagePath;
	private JButton btnLoadImage;
	private JLabel lbLoadInputImage;
	private JLabel lbLoadOutputImage;
	private JComboBox<String> cbbFilterType;
	private JLabel lblNewLabel_1;
	private JButton btnApply;
	private JFileChooser jFileChooser;

	private Vector<String> listItemJListImagePath;

	@Autowired
	public FormSVDCustomFilter(AppConfiguration appConfiguration) throws HeadlessException {
		this.appConfiguration = appConfiguration;
		initComponent();
	}

	public enum FilterType {
		WARM, COLD, SUNSET, SUNRISE, NEGATIVE, GOLDEN_HOUR, MOONLIGHT, CANDLELIGHT, BLUE_HOUR, NOON_DAYLIGHT, CINEMATIC, VINTAGE, PORTRAIT, LANDSCAPE,
		ENHANCED
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

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 1413, 597);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);

		panelLoadInputImage = new JPanel();
		panelLoadInputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelLoadInputImage.setBounds(299, 75, 450, 450);
		contentPane.add(panelLoadInputImage);
		panelLoadInputImage.setLayout(null);

		lbLoadInputImage = new JLabel("");
		lbLoadInputImage.setBounds(0, 0, 450, 450);
		panelLoadInputImage.add(lbLoadInputImage);

		lblNewLabel = new JLabel("Input Image");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(299, 42, 102, 14);
		contentPane.add(lblNewLabel);

		lblOutputImage = new JLabel("Output image");
		lblOutputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblOutputImage.setBounds(915, 42, 102, 14);
		contentPane.add(lblOutputImage);

		panelLoadOutputImage = new JPanel();
		panelLoadOutputImage.setLayout(null);
		panelLoadOutputImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelLoadOutputImage.setBounds(915, 75, 450, 450);
		contentPane.add(panelLoadOutputImage);

		lbLoadOutputImage = new JLabel("");
		lbLoadOutputImage.setBounds(0, 0, 450, 450);
		panelLoadOutputImage.add(lbLoadOutputImage);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 75, 248, 450);
		contentPane.add(scrollPane);

		listItemJListImagePath = new Vector<>();

		jListImagePath = new JList<String>();
		jListImagePath.setListData(listItemJListImagePath);
		scrollPane.setViewportView(jListImagePath);

		btnLoadImage = new JButton("Load Image");
		btnLoadImage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnLoadImage.setBounds(10, 21, 116, 35);
		contentPane.add(btnLoadImage);

		cbbFilterType = new JComboBox<String>();
		cbbFilterType.setModel(new DefaultComboBoxModel<String>(new String[] { "WARM", "COLD", "SUNSET", "SUNRISE", "NEGATIVE", "GOLDEN_HOUR",
				"MOONLIGHT", "CANDLELIGHT", "BLUE_HOUR", "NOON_DAYLIGHT", "CINEMATIC", "VINTAGE", "PORTRAIT", "LANDSCAPE", "ENHANCED" }));
		cbbFilterType.setSelectedIndex(0);
		cbbFilterType.setBounds(777, 242, 116, 22);
		contentPane.add(cbbFilterType);

		lblNewLabel_1 = new JLabel("Filter Type:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1.setBounds(777, 217, 84, 14);
		contentPane.add(lblNewLabel_1);

		btnApply = new JButton("Apply");
		btnApply.setForeground(new Color(255, 255, 255));
		btnApply.setBorderPainted(false);
		btnApply.setBackground(new Color(90, 203, 52));
		btnApply.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnApply.setBounds(777, 297, 116, 35);
		contentPane.add(btnApply);

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
		jListImagePath_ValueChanged();
		btnApply_Click();
	}

	private void btnApply_Click() {
		btnApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filterTypeSelected = (String) cbbFilterType.getSelectedItem();

				if (cbbFilterType.getSelectedIndex() < 0 || filterTypeSelected == null) {
					JOptionPane.showMessageDialog(null, "Please select filter type!!!", "WARNING", JOptionPane.WARNING_MESSAGE);
				} else {
					String pathImageSelected = jListImagePath.getSelectedValue();
					if (pathImageSelected == null || jListImagePath.getSelectedIndex() < 0) {
						JOptionPane.showMessageDialog(null, "Please select path image want to apply filter!!!", "WARNING",
								JOptionPane.WARNING_MESSAGE);
					} else {
						applyFilter(pathImageSelected, filterTypeSelected);
					}
				}
			}
		});
	}

	private void applyFilter(String pathImageSelected, String filterTypeSelected) {
		Mat sourceImage = Imgcodecs.imread(pathImageSelected);

		Imgproc.resize(sourceImage, sourceImage, new Size(450, 450));

		if (sourceImage.empty()) {
			JOptionPane.showMessageDialog(null, "Cannot read image with OpenCV", "WARNING", JOptionPane.WARNING_MESSAGE);
		}

		Mat filtered = applyLightFilter(sourceImage, filterTypeSelected);

		imageCommonHandle.loadCVMatToLabel(filtered, lbLoadOutputImage, panelLoadOutputImage);
	}

	private Mat applyLightFilter(Mat source, String filterTypeSelected) {
		try {
			// Store original image properties
			Size originalSize = source.size();

			// Universal approach: resize to square for SVD, then apply color grading
			int targetSize = Math.max(source.rows(), source.cols());
			Mat squareImage = new Mat();
			Imgproc.resize(source, squareImage, new Size(targetSize, targetSize));

			// Convert to float for SVD processing
			Mat sourceFloat = new Mat();
			squareImage.convertTo(sourceFloat, CvType.CV_32F);

			List<Mat> sourceChannels = new ArrayList<>();
			Core.split(sourceFloat, sourceChannels);

			List<Mat> resultChannels = new ArrayList<>();

			for (int i = 0; i < 3; i++) {
				Mat U = new Mat(), W = new Mat(), Vt = new Mat();
				Core.SVDecomp(sourceChannels.get(i), W, U, Vt);

				Mat filteredW = applyFilterTransformation(W, filterTypeSelected, i);
				Mat resultChannel = reconstructChannel(U, filteredW, Vt);
				resultChannels.add(resultChannel);
			}

			// Merge and convert back
			Mat filteredSquare = new Mat();
			Core.merge(resultChannels, filteredSquare);

			Mat filteredSquare8u = new Mat();
			filteredSquare.convertTo(filteredSquare8u, CvType.CV_8UC3);

			// Resize back to original dimensions
			Mat result = new Mat();
			Imgproc.resize(filteredSquare8u, result, originalSize);

			// Apply color grading (this is where the fix is applied)
			return applyColorGrading(result, filterTypeSelected);

		} catch (Exception e) {
			System.err.println("Error applying filter: " + e.getMessage());
			e.printStackTrace();
			return source.clone(); // Return original image if processing fails
		}

	}

	// Enhanced color grading with clamping to prevent overflow
	private Mat applyColorGrading(Mat image, String filterTypeSelected) {
		Mat gradedImage = image.clone();

		// Ensure image is in correct format
		if (gradedImage.type() != CvType.CV_8UC3) {
			Mat temp = new Mat();
			gradedImage.convertTo(temp, CvType.CV_8UC3);
			gradedImage = temp;
		}

		FilterType filterType = null;

		try {
			filterType = FilterType.valueOf(filterTypeSelected.toUpperCase());
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Please select filter type!!!", "WARNING", JOptionPane.WARNING_MESSAGE);
			return gradedImage;
		}

		switch (filterType) {
		case VINTAGE:
			return applySepiaTone(gradedImage);
		case CINEMATIC:
			// BGR order: bFactor, gFactor, rFactor
			return applyColorCurves(gradedImage, 0.8, 0.9, 1.2); // Cool tones
		case WARM:
			return applyColorCurves(gradedImage, 1.3, 1.0, 0.8); // Reduce blue, boost red
		case COLD:
			return applyColorCurves(gradedImage, 0.8, 1.0, 1.3); // Boost blue, reduce red
		case SUNSET:
			return applyColorCurves(gradedImage, 1.5, 1.2, 0.6); // Strong red, reduce blue
		case SUNRISE:
			return applyColorCurves(gradedImage, 1.4, 1.3, 0.9); // Warm with green boost
		case GOLDEN_HOUR:
			return applyColorCurves(gradedImage, 1.4, 1.1, 0.7); // Golden warm
		case MOONLIGHT:
			return applyColorCurves(gradedImage, 0.7, 0.9, 1.4); // Cool blue
		case CANDLELIGHT:
			return applyColorCurves(gradedImage, 1.6, 1.0, 0.5); // Very warm
		case BLUE_HOUR:
			return applyColorCurves(gradedImage, 0.6, 0.8, 1.5); // Very cool blue
		case NOON_DAYLIGHT:
			return applyColorCurves(gradedImage, 1.0, 1.0, 1.0); // Neutral
		case PORTRAIT:
			return applyColorCurves(gradedImage, 1.2, 1.0, 0.9); // Warm skin tones
		case LANDSCAPE:
			return applyColorCurves(gradedImage, 0.9, 1.2, 1.1); // Enhance greens and blues
		case ENHANCED:
			return applyColorCurves(gradedImage, 1.1, 1.1, 1.05); // Slight enhancement
		default:
			return gradedImage;
		}
	}

	private Mat applyColorCurves(Mat image, double rFactor, double gFactor, double bFactor) {
		// Note: Parameters are still named rFactor, gFactor, bFactor for clarity
		// but we apply them in BGR order: bFactor, gFactor, rFactor

		Mat adjusted = new Mat();
		List<Mat> channels = new ArrayList<>();
		Core.split(image, channels);

		// Apply factors in BGR order
		Mat blueChannel = new Mat();
		Mat greenChannel = new Mat();
		Mat redChannel = new Mat();

		channels.get(0).convertTo(blueChannel, channels.get(0).type());
		channels.get(1).convertTo(greenChannel, channels.get(1).type());
		channels.get(2).convertTo(redChannel, channels.get(2).type());

		// Apply: blue * bFactor, green * gFactor, red * rFactor
		Core.multiply(blueChannel, new Scalar(bFactor), blueChannel);
		Core.multiply(greenChannel, new Scalar(gFactor), greenChannel);
		Core.multiply(redChannel, new Scalar(rFactor), redChannel);

		List<Mat> adjustedChannels = Arrays.asList(blueChannel, greenChannel, redChannel);
		Core.merge(adjustedChannels, adjusted);

		return adjusted;
	}

	// Fixed sepia tone implementation
	private Mat applySepiaTone(Mat image) {
		Mat sepia = new Mat();

		// Convert to float for matrix multiplication
		Mat floatImage = new Mat();
		image.convertTo(floatImage, CvType.CV_32FC3, 1.0 / 255.0);

		// Create sepia transformation matrix
		Mat sepiaMatrix = new Mat(3, 3, CvType.CV_32F);
		float[] sepiaData = { 0.393f, 0.769f, 0.189f, // R
				0.349f, 0.686f, 0.168f, // G
				0.272f, 0.534f, 0.131f // B
		};
		sepiaMatrix.put(0, 0, sepiaData);

		// Transform image
		Core.transform(floatImage, sepia, sepiaMatrix);

		// Convert back to 8-bit and clamp values
		sepia.convertTo(sepia, CvType.CV_8UC3, 255.0);

		return sepia;
	}

	private Mat applyFilterTransformation(Mat W, String filterTypeSelected, int channel) {
		Mat transformedW = W.clone();

		FilterType filterType = null;

		try {
			filterType = FilterType.valueOf(filterTypeSelected.toUpperCase());
		} catch (IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, "Please select filter type!!!", "WARNING", JOptionPane.WARNING_MESSAGE);
			return transformedW;
		}

		switch (filterType) {
		case WARM:
			// BGR order: Reduce Blue, keep Green, Boost Red
			double[] warmFactors = { 0.8, 1.1, 1.3 }; // B, G, R
			return scaleSingularValues(transformedW, warmFactors[channel]);

		case COLD:
			// BGR order: Boost Blue, keep Green, Reduce Red
			double[] coldFactors = { 1.3, 1.1, 0.8 }; // B, G, R
			return scaleSingularValues(transformedW, coldFactors[channel]);

		case SUNSET:
			// BGR: Reduce Blue, Boost Green slightly, Strong Boost Red
			double[] sunsetFactors = { 0.6, 1.2, 1.5 }; // B, G, R
			return scaleSingularValues(transformedW, sunsetFactors[channel]);

		case SUNRISE:
			// BGR: Slightly reduce Blue, Boost Green, Strong Boost Red
			double[] sunriseFactors = { 0.9, 1.3, 1.4 }; // B, G, R
			return scaleSingularValues(transformedW, sunriseFactors[channel]);

		case GOLDEN_HOUR:
			// BGR: Reduce Blue, Slight Boost Green, Strong Boost Red
			double[] goldenFactors = { 0.7, 1.1, 1.4 }; // B, G, R
			return scaleSingularValues(transformedW, goldenFactors[channel]);

		case MOONLIGHT:
			// BGR: Strong Boost Blue, Slight Boost Green, Reduce Red
			double[] moonFactors = { 1.4, 0.9, 0.7 }; // B, G, R
			return scaleSingularValues(transformedW, moonFactors[channel]);

		case CANDLELIGHT:
			// BGR: Strong Reduce Blue, Keep Green, Strong Boost Red
			double[] candleFactors = { 0.5, 1.0, 1.6 }; // B, G, R
			return scaleSingularValues(transformedW, candleFactors[channel]);

		case BLUE_HOUR:
			// BGR: Strong Boost Blue, Slight Reduce Green, Strong Reduce Red
			double[] blueFactors = { 1.5, 0.8, 0.6 }; // B, G, R
			return scaleSingularValues(transformedW, blueFactors[channel]);

		case NOON_DAYLIGHT:
			// BGR: Balanced lighting
			double[] noonFactors = { 1.1, 1.1, 1.1 }; // B, G, R
			return scaleSingularValues(transformedW, noonFactors[channel]);

		case CINEMATIC:
			// BGR: Cool tones with contrast
			double[] cinematicFactors = { 1.2, 0.9, 0.8 }; // B, G, R
			return scaleSingularValues(transformedW, cinematicFactors[channel]);

		case VINTAGE:
			// BGR: Sepia-like (reduce blue, moderate green, boost red)
			double[] vintageFactors = { 0.6, 1.1, 1.3 }; // B, G, R
			return scaleSingularValues(transformedW, vintageFactors[channel]);

		case PORTRAIT:
			// BGR: Warm skin tones (reduce blue, keep green, slight boost red)
			double[] portraitFactors = { 0.9, 1.0, 1.2 }; // B, G, R
			return scaleSingularValues(transformedW, portraitFactors[channel]);

		case LANDSCAPE:
			// BGR: Enhance nature (boost blue and green, slight reduce red)
			double[] landscapeFactors = { 1.2, 1.4, 0.9 }; // B, G, R
			return scaleSingularValues(transformedW, landscapeFactors[channel]);

		case ENHANCED:
			// BGR: Overall enhancement
			double[] enhancedFactors = { 1.05, 1.1, 1.1 }; // B, G, R
			return scaleSingularValues(transformedW, enhancedFactors[channel]);

		case NEGATIVE:
			return applyNegativeTransformation(transformedW);

		default:
			return transformedW;
		}
	}

	private Mat scaleSingularValues(Mat W, double factor) {
		Mat scaledW = W.clone();
		for (int i = 0; i < W.rows(); i++) {
			double value = W.get(i, 0)[0];
			scaledW.put(i, 0, value * factor);
		}
		return scaledW;
	}

	// Negative: Invert the singular value relationships
	private Mat applyNegativeTransformation(Mat W) {
		double maxVal = findMaxSingularValue(W);
		Mat negativeW = W.clone();
		for (int i = 0; i < W.rows(); i++) {
			double value = W.get(i, 0)[0];
			negativeW.put(i, 0, maxVal - value);
		}
		return negativeW;
	}

	private static double findMaxSingularValue(Mat W) {
		Core.MinMaxLocResult result = Core.minMaxLoc(W);
		return result.maxVal;
	}

	// Robust channel reconstruction
	private Mat reconstructChannel(Mat U, Mat W, Mat Vt) {
		try {
			Mat W_diag = Mat.diag(W);
			Mat temp = new Mat();
			Mat result = new Mat();

			Core.gemm(U, W_diag, 1, new Mat(), 0, temp);
			Core.gemm(temp, Vt, 1, new Mat(), 0, result);

			return result;
		} catch (Exception e) {
			System.err.println("Reconstruction failed: " + e.getMessage());
			// Return original U matrix as fallback
			return U.clone();
		}
	}

	private void jListImagePath_ValueChanged() {
		jListImagePath.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (jListImagePath.getSelectedIndex() >= 0) {
					String pathImageSelected = jListImagePath.getSelectedValue();
					// load image to original box
					imageCommonHandle.loadImageToLabel(pathImageSelected, lbLoadInputImage, panelLoadInputImage);
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
						listItemJListImagePath.add(file.getAbsolutePath());
					}
					jListImagePath.setListData(listItemJListImagePath);
				}
			}
		});
	}
}
