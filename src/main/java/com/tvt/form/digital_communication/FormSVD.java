package com.tvt.form.digital_communication;

import java.awt.HeadlessException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

import com.tvt.common.ComponentSettingCommon;
import com.tvt.common.ImageCommonHandle;
import com.tvt.config.AppConfiguration;

import javax.swing.border.BevelBorder;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.Color;

@Component
public class FormSVD extends JFrame {

	private static final long serialVersionUID = 1L;
	private final AppConfiguration appConfiguration;
	
	private ComponentSettingCommon componentSettingCommon = new ComponentSettingCommon();
	private ImageCommonHandle imageCommonHandle = new ImageCommonHandle();

	private JPanel contentPane;
	private JScrollPane scrollPane;
	private JList<String> jListImage1;
	private JButton btnLoadImage1;
	private JScrollPane scrollPane_1;
	private JList<String> jListImage2;
	private JButton btnLoadImage2;
	private JLabel lblNewLabel;
	private JLabel lblOutputImage;
	private JPanel panelOutputImage1;
	private JPanel panelOutputImage2;
	private JPanel panelOriginalImage1;
	private JPanel panelOriginalImage2;
	private JLabel lbLoadOriginalImage1;
	private JLabel lbLoadOriginalImage2;
	private JLabel lbLoadOutputImage1;
	private JLabel lbLoadOutputImage2;
	private JFileChooser jFileChooser;

	private Vector<String> listItemJListImage1;
	private Vector<String> listItemJListImage2;
	private JButton btnProcessSVD;


	public FormSVD(AppConfiguration appConfiguration) throws HeadlessException {
		this.appConfiguration = appConfiguration;
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

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 1394, 920);
		this.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		contentPane.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 95, 305, 705);
		contentPane.add(scrollPane);

		listItemJListImage1 = new Vector<>();

		jListImage1 = new JList<>();

		jListImage1.setListData(listItemJListImage1);
		jListImage1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(jListImage1);

		btnLoadImage1 = new JButton("Load image 1");
		btnLoadImage1.setBounds(10, 28, 140, 43);
		contentPane.add(btnLoadImage1);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(1038, 95, 305, 705);
		contentPane.add(scrollPane_1);

		listItemJListImage2 = new Vector<>();

		jListImage2 = new JList<String>();

		jListImage2.setListData(listItemJListImage2);
		jListImage2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane_1.setViewportView(jListImage2);

		btnLoadImage2 = new JButton("Load image 2");
		btnLoadImage2.setBounds(1038, 28, 140, 43);
		contentPane.add(btnLoadImage2);

		lblNewLabel = new JLabel("Original Image");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setBounds(630, 55, 98, 29);
		contentPane.add(lblNewLabel);

		lblOutputImage = new JLabel("Output Image");
		lblOutputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblOutputImage.setBounds(630, 441, 98, 29);
		contentPane.add(lblOutputImage);

		panelOutputImage1 = new JPanel();
		panelOutputImage1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOutputImage1.setBounds(335, 481, 315, 315);
		contentPane.add(panelOutputImage1);
		panelOutputImage1.setLayout(null);

		lbLoadOutputImage1 = new JLabel("");
		lbLoadOutputImage1.setBounds(0, 0, 315, 315);
		panelOutputImage1.add(lbLoadOutputImage1);

		panelOutputImage2 = new JPanel();
		panelOutputImage2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOutputImage2.setBounds(702, 481, 315, 315);
		contentPane.add(panelOutputImage2);
		panelOutputImage2.setLayout(null);

		lbLoadOutputImage2 = new JLabel("");
		lbLoadOutputImage2.setBounds(0, 0, 315, 315);
		panelOutputImage2.add(lbLoadOutputImage2);

		panelOriginalImage1 = new JPanel();
		panelOriginalImage1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOriginalImage1.setBounds(335, 95, 315, 315);
		contentPane.add(panelOriginalImage1);
		panelOriginalImage1.setLayout(null);

		lbLoadOriginalImage1 = new JLabel("");
		lbLoadOriginalImage1.setBounds(0, 0, 315, 315);
		panelOriginalImage1.add(lbLoadOriginalImage1);

		panelOriginalImage2 = new JPanel();
		panelOriginalImage2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOriginalImage2.setBounds(702, 95, 315, 315);
		contentPane.add(panelOriginalImage2);
		panelOriginalImage2.setLayout(null);

		lbLoadOriginalImage2 = new JLabel("");
		lbLoadOriginalImage2.setBounds(0, 0, 315, 315);
		panelOriginalImage2.add(lbLoadOriginalImage2);

		btnProcessSVD = new JButton("Process SVD");
		btnProcessSVD.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnProcessSVD.setBorderPainted(false);
		btnProcessSVD.setForeground(new Color(255, 255, 255));
		btnProcessSVD.setBackground(new Color(70, 197, 33));

		btnProcessSVD.setBounds(612, 827, 140, 43);
		contentPane.add(btnProcessSVD);

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
		btnLoadImage1_Click();
		btnLoadImage2_Click();
		jListImage1_ValueChanged();
		jListImage2_ValueChanged();
		btnProcessSVD_Click();
	}

	private void btnProcessSVD_Click() {
		btnProcessSVD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jListImage2.getSelectedIndex() < 0 || jListImage1.getSelectedIndex() < 0) {
					JOptionPane.showMessageDialog(null, "Please select two image", "Warning", JOptionPane.WARNING_MESSAGE);
					return;
				} else {
					processSVD();
				}
			}
		});
	}

	private void processSVD() {
		String path1 = jListImage1.getSelectedValue();
		String path2 = jListImage2.getSelectedValue();

		Mat image1 = Imgcodecs.imread(path1, Imgcodecs.IMREAD_COLOR);
		Mat image2 = Imgcodecs.imread(path2, Imgcodecs.IMREAD_COLOR);

		// resize
		Imgproc.resize(image1, image1, new Size(400, 400));
		Imgproc.resize(image2, image2, new Size(400, 400));

		if (image1.empty() || image2.empty()) {
			JOptionPane.showMessageDialog(null, "Cannot read image with OpenCV", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Convert BGR to RGB
		Mat rgbImage1 = new Mat();
		Mat rgbImage2 = new Mat();
		Imgproc.cvtColor(image1, rgbImage1, Imgproc.COLOR_BGR2RGB);
		Imgproc.cvtColor(image2, rgbImage2, Imgproc.COLOR_BGR2RGB);

		// Process the images
		processImagesWithSVD(rgbImage1, rgbImage2);
		

		// Process the images
		processImagesWithSVD(rgbImage1, rgbImage2);
	}

	private void processImagesWithSVD(Mat image1, Mat image2) {
		// Convert images to float
		Mat floatImage1 = new Mat();
		Mat floatImage2 = new Mat();
		image1.convertTo(floatImage1, CvType.CV_32F);
		image2.convertTo(floatImage2, CvType.CV_32F);

		// Split channels
		List<Mat> channels1 = new ArrayList<>();
		List<Mat> channels2 = new ArrayList<>();
		Core.split(floatImage1, channels1);
		Core.split(floatImage2, channels2);

		// Store SVD components for each channel
		Mat[] U1 = new Mat[3], Vt1 = new Mat[3], W1 = new Mat[3];
		Mat[] U2 = new Mat[3], Vt2 = new Mat[3], W2 = new Mat[3];

		// Compute SVD on each channel
		for (int i = 0; i < 3; i++) {
			// SVD for image1 channel
			U1[i] = new Mat();
			W1[i] = new Mat();
			Vt1[i] = new Mat();
			Core.SVDecomp(channels1.get(i), W1[i], U1[i], Vt1[i]);

			// SVD for image2 channel
			U2[i] = new Mat();
			W2[i] = new Mat();
			Vt2[i] = new Mat();
			Core.SVDecomp(channels2.get(i), W2[i], U2[i], Vt2[i]);
		}

		// Swap sigma matrices (W) between images
		Mat[] swappedChannels1 = reconstructImage(U1, W2, Vt1);
		Mat[] swappedChannels2 = reconstructImage(U2, W1, Vt2);

		// Merge channels back
		Mat result1 = new Mat();
		Mat result2 = new Mat();
		Core.merge(new ArrayList<>(Arrays.asList(swappedChannels1)), result1);
		Core.merge(new ArrayList<>(Arrays.asList(swappedChannels2)), result2);

		// Convert back to 8-bit for display
		Mat displayResult1 = new Mat();
		Mat displayResult2 = new Mat();
		result1.convertTo(displayResult1, CvType.CV_8U);
		result2.convertTo(displayResult2, CvType.CV_8U);

		// Convert back to BGR for OpenCV display
		Mat bgrResult1 = new Mat();
		Mat bgrResult2 = new Mat();
		Mat bgrImage1 = new Mat();
		Mat bgrImage2 = new Mat();

		Imgproc.cvtColor(displayResult1, bgrResult1, Imgproc.COLOR_RGB2BGR);
		Imgproc.cvtColor(displayResult2, bgrResult2, Imgproc.COLOR_RGB2BGR);
		Imgproc.cvtColor(image1, bgrImage1, Imgproc.COLOR_RGB2BGR);
		Imgproc.cvtColor(image2, bgrImage2, Imgproc.COLOR_RGB2BGR);

		// Display output image
		imageCommonHandle.loadCVMatToLabel(bgrResult1, lbLoadOutputImage1, panelOutputImage1);
		imageCommonHandle.loadCVMatToLabel(bgrResult2, lbLoadOutputImage2, panelOutputImage2);
	}

	private Mat[] reconstructImage(Mat[] U, Mat[] W, Mat[] Vt) {
		Mat[] channels = new Mat[3];

		Mat temp;
		Mat reconstructed;
		Mat W_diag;
		int minDim = 0;
		for (int i = 0; i < 3; i++) {
			// Reconstruct channel: U * W * Vt
			temp = new Mat();
			reconstructed = new Mat();

			// Create diagonal matrix from singular values
			W_diag = new Mat(U[i].rows(), Vt[i].cols(), CvType.CV_32F, new Scalar(0));

			minDim = Math.min(W[i].rows(), Vt[i].cols());

			for (int j = 0; j < minDim; j++) {
				W_diag.put(j, j, W[i].get(j, 0));
			}

			// Multiply U * W_diag
			Core.gemm(U[i], W_diag, 1, new Mat(), 0, temp);
			// Multiply (U * W_diag) * Vt
			Core.gemm(temp, Vt[i], 1, new Mat(), 0, reconstructed);

			channels[i] = reconstructed;
		}

		return channels;
	}

	private void jListImage2_ValueChanged() {
		jListImage2.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (jListImage2.getSelectedIndex() >= 0) {
					// get selected value
					String pathImageSelected = jListImage2.getSelectedValue();
					// load image to original box
					imageCommonHandle.loadImageToLabel(pathImageSelected, lbLoadOriginalImage2, panelOriginalImage2);
				}
			}
		});
	}

	private void jListImage1_ValueChanged() {
		jListImage1.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (jListImage1.getSelectedIndex() >= 0) {
					// get selected value
					String pathImageSelected = jListImage1.getSelectedValue();
					// load image to original box
					imageCommonHandle.loadImageToLabel(pathImageSelected, lbLoadOriginalImage1, panelOriginalImage1);
				}
			}
		});
	}

	private void btnLoadImage2_Click() {
		btnLoadImage2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
					File[] selectedFiles = jFileChooser.getSelectedFiles();
					for (File file : selectedFiles) {
						listItemJListImage2.add(file.getAbsolutePath());
					}
					jListImage2.setListData(listItemJListImage2);
				}
			}
		});
	}

	private void btnLoadImage1_Click() {
		btnLoadImage1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
					File[] selectedFiles = jFileChooser.getSelectedFiles();
					for (File file : selectedFiles) {
						listItemJListImage1.add(file.getAbsolutePath());
					}
					jListImage1.setListData(listItemJListImage1);
				}
			}
		});
	}
}
