package com.tvt.form.image_processing;

import java.awt.Color;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
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
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect2d;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
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

	private final Net net;

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
	private JPanel panelOutputTemperatureImage;
	private JLabel lbLoadOutputTemperatureImage;
	private JLabel lblTemperatureImage;
	private JLabel lbTemperatureVision;
	private JLabel lbTemperatureVisionImageValue;

	@Autowired
	public FormExtractEmbeddedImage(AppConfiguration appConfiguration) throws HeadlessException {
		this.appConfiguration = appConfiguration;
		net = loadOnnxModel();
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
		this.setBounds(100, 100, 1957, 1333);
		this.setLocationRelativeTo(null);

		contentPane = new JPanel();
		this.setContentPane(contentPane);
		contentPane.setLayout(null);

		btnLoadImage = new JButton("Load image path");

		btnLoadImage.setBounds(30, 11, 129, 40);
		contentPane.add(btnLoadImage);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 62, 327, 1221);
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
		lbThermalImage.setBounds(446, 75, 95, 14);
		contentPane.add(lbThermalImage);

		panelOutputVisionImage = new JPanel();
		panelOutputVisionImage.setLayout(null);
		panelOutputVisionImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOutputVisionImage.setBounds(1239, 100, 640, 480);
		contentPane.add(panelOutputVisionImage);

		lbLoadOutputVisionImage = new JLabel("");
		lbLoadOutputVisionImage.setBounds(0, 0, 640, 480);
		panelOutputVisionImage.add(lbLoadOutputVisionImage);

		lbOutputImage = new JLabel("Vision Image");
		lbOutputImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbOutputImage.setBounds(1240, 75, 119, 14);
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
		btnSelectOutputFolder.setBounds(197, 13, 160, 36);
		contentPane.add(btnSelectOutputFolder);

		txtOutputFolderEmbeddedFilePath = new JTextField();
		txtOutputFolderEmbeddedFilePath.setFont(new Font("Tahoma", Font.BOLD, 13));
		txtOutputFolderEmbeddedFilePath.setEditable(false);
		txtOutputFolderEmbeddedFilePath.setBounds(367, 16, 380, 28);
		contentPane.add(txtOutputFolderEmbeddedFilePath);
		txtOutputFolderEmbeddedFilePath.setColumns(10);

		panelOutputTemperatureImage = new JPanel();
		panelOutputTemperatureImage.setLayout(null);
		panelOutputTemperatureImage.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panelOutputTemperatureImage.setBounds(533, 803, 640, 480);
		contentPane.add(panelOutputTemperatureImage);

		lbLoadOutputTemperatureImage = new JLabel("");
		lbLoadOutputTemperatureImage.setBounds(0, 0, 640, 480);
		panelOutputTemperatureImage.add(lbLoadOutputTemperatureImage);

		lblTemperatureImage = new JLabel("Temperature Image");
		lblTemperatureImage.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTemperatureImage.setBounds(531, 778, 146, 14);
		contentPane.add(lblTemperatureImage);

		lbTemperatureVision = new JLabel("Temperature:");
		lbTemperatureVision.setFont(new Font("Tahoma", Font.BOLD, 13));
		lbTemperatureVision.setBounds(1433, 602, 95, 14);
		contentPane.add(lbTemperatureVision);

		lbTemperatureVisionImageValue = new JLabel("");
		lbTemperatureVisionImageValue.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lbTemperatureVisionImageValue.setBounds(1538, 603, 68, 14);
		contentPane.add(lbTemperatureVisionImageValue);

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
		btnSelectOutputFolder_Click();
	}

	private void btnSelectOutputFolder_Click() {
		btnSelectOutputFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser jFileChooser = new JFileChooser();
				componentSettingCommon.settingJFileChooser(jFileChooser, "Please select a folder to save your image", JFileChooser.DIRECTORIES_ONLY,
						false, null, appConfiguration.getInitCurrentDirectoryFileChooser());

				if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
					listImage.clearSelection();
					String outputFolderEmbedded = jFileChooser.getSelectedFile().getAbsolutePath();
					txtOutputFolderEmbeddedFilePath.setText(outputFolderEmbedded);
					listImageData.clear();
					listImage.setListData(listImageData);
				}
			}
		});
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
		lbLoadOutputVisionImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				lbLocationVisionImageValue.setVisible(false);
				lbTemperatureVisionImageValue.setVisible(false);
			}
		});
	}

	private void lbLoadOutputImage_MouseMoved() {
		lbLoadOutputVisionImage.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				lbLocationVisionImageValue.setVisible(true);
				lbTemperatureVisionImageValue.setVisible(true);

				if (listImage.getSelectedIndex() >= 0) {
					int x = e.getX();
					int y = e.getY();

					if (x >= 0 && y >= 0 && x < temperatureMap.cols() && y < temperatureMap.rows()) {
						String locationValue = new StringBuffer("").append("(").append(x).append(",").append(y).append(")").toString();

						lbLocationVisionImageValue.setText(locationValue);

						// get temperature
						double temperatureAtLocation = temperatureMap.get(y, x)[0];
						String temperatureValue = new StringBuffer("").append(String.format("%.2f", temperatureAtLocation)).append("°C").toString();
						lbTemperatureVisionImageValue.setText(temperatureValue);
					}

				}

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

	private JDialog createLoadingDialog() {
		JDialog dialog = new JDialog(FormExtractEmbeddedImage.this, "Processing...", true);
		dialog.setSize(300, 300);
		dialog.setLocationRelativeTo(FormExtractEmbeddedImage.this);
		dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		dialog.setResizable(false);

		dialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
			}
		});

		ImageIcon loadingIcon = new ImageIcon(FormExtractEmbeddedImage.class.getResource("/loading.gif"));

		JLabel lblGif = new JLabel(loadingIcon, SwingConstants.CENTER);
		dialog.getContentPane().add(lblGif);

		return dialog;
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

				if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
					listImageData.clear();
					// Create loading dialog
					JDialog loadingDialog = createLoadingDialog();

					// Disable FormExtractEmbeddedImage frame
					FormExtractEmbeddedImage.this.setEnabled(false);

					// Create worker to handle heavy task
					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

						@Override
						protected Void doInBackground() throws Exception {
							listImage.clearSelection();
							File[] selectedFiles = jFileChooser.getSelectedFiles();
							for (File file : selectedFiles) {
								listImageData.add(file.getAbsolutePath());
								extractAllEmbeddedFile(file.getAbsolutePath(), txtOutputFolderEmbeddedFilePath.getText());
							}
							return null;
						}

						@Override
						protected void done() {
							try {
								get();
								loadingDialog.dispose(); // Dispose loading
								FormExtractEmbeddedImage.this.setEnabled(true); // Enable FormExtractEmbeddedImage frame again
								listImage.setListData(listImageData);
								JOptionPane.showMessageDialog(null, "Extract embedded file successfully", "INFO", JOptionPane.INFORMATION_MESSAGE);
							} catch (ExecutionException ex1) {
								Throwable cause = ex1.getCause();
								cause.printStackTrace();

								loadingDialog.dispose(); // Dispose loading
								FormExtractEmbeddedImage.this.setEnabled(true); // Enable FormExtractEmbeddedImage frame again
								listImage.setListData(listImageData);
								JOptionPane.showMessageDialog(null,
										"Error occurred when extracting embedded file. Please try again. " + cause.getMessage(), "ERROR",
										JOptionPane.ERROR_MESSAGE);
							} catch (InterruptedException ex2) {
								Throwable cause = ex2.getCause();
								cause.printStackTrace();

								loadingDialog.dispose(); // Dispose loading
								FormExtractEmbeddedImage.this.setEnabled(true); // Enable FormExtractEmbeddedImage frame again
								listImage.setListData(listImageData);
								JOptionPane.showMessageDialog(null,
										"Error occurred when extracting embedded file. Please try again. " + cause.getMessage(), "ERROR",
										JOptionPane.ERROR_MESSAGE);
							}

						}
					};

					// Execute worker
					worker.execute();
					loadingDialog.setVisible(true);
				}
			}
		});
	}

//	private void btnLoadImage_Click() {
//		btnLoadImage.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//
//				if (txtOutputFolderEmbeddedFilePath == null || txtOutputFolderEmbeddedFilePath.getText() == null
//						|| txtOutputFolderEmbeddedFilePath.getText().isBlank()) {
//					JOptionPane.showMessageDialog(null, "Please select the output folder to store the data!!!", "WARNING",
//							JOptionPane.WARNING_MESSAGE);
//					return;
//				}
//
//				listImageData.clear();
//
//				if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
//					// Create loading dialog
//					JDialog loadingDialog = createLoadingDialog();
//
//					// Disable FormExtractEmbeddedImage frame
//					FormExtractEmbeddedImage.this.setEnabled(false);
//					
//					// Create worker to handle heavy task
//					SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
//
//						@Override
//						protected Void doInBackground() throws Exception {
//							listImage.clearSelection();
//							File[] selectedFiles = jFileChooser.getSelectedFiles();
//							for (File file : selectedFiles) {
//								listImageData.add(file.getAbsolutePath());
//								extractAllEmbeddedFile(file.getAbsolutePath(), txtOutputFolderEmbeddedFilePath.getText());
//							}
//							return null;
//						}
//
//						@Override
//						protected void done() {
//							loadingDialog.dispose(); // Dispose loading
//							FormExtractEmbeddedImage.this.setEnabled(true); // Enable FormExtractEmbeddedImage frame again
//							listImage.setListData(listImageData);
//							JOptionPane.showMessageDialog(null, "Extract embedded file successfully", "INFO", JOptionPane.INFORMATION_MESSAGE);
//						}
//					};
//
//					// Execute worker
//					worker.execute();
//					loadingDialog.setVisible(true);
//				}
//			}
//		});
//	}

//	private void btnLoadImage_Click() {
//		btnLoadImage.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				
//				if (txtOutputFolderEmbeddedFilePath == null || txtOutputFolderEmbeddedFilePath.getText() == null
//						|| txtOutputFolderEmbeddedFilePath.getText().isBlank()) {
//					JOptionPane.showMessageDialog(null, "Please select the output folder to store the data!!!", "WARNING",
//							JOptionPane.WARNING_MESSAGE);
//					return;
//				}
//				
//				listImageData.clear();
//				
//				if (jFileChooser.showOpenDialog(getOwner()) == JFileChooser.APPROVE_OPTION) {
//					listImage.clearSelection();
//					File[] selectedFiles = jFileChooser.getSelectedFiles();
//					String cleanPath = "";
//					for (File file : selectedFiles) {
////						cleanPath = removeChineseCharacters(file.getAbsolutePath());
////						listImageData.add(cleanPath);
//						listImageData.add(file.getAbsolutePath());
//						extractAllEmbeddedFile(file.getAbsolutePath(), txtOutputFolderEmbeddedFilePath.getText());
//					}
//					
//					listImage.setListData(listImageData);
//					JOptionPane.showMessageDialog(null, "Extract embedded file successfully", "INFO",
//							JOptionPane.INFORMATION_MESSAGE);
//					
//				}
//			}
//		});
//	}

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
				if (pathImageSelected != null && !pathImageSelected.isBlank()) {
					displayResult(pathImageSelected);
				}
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
//		String visionImagePath = fileService.getOutputFilePath(pathImageSelected, "vision.png", txtOutputFolderEmbeddedFilePath.getText());
		String visionImageDetectedPath = fileService.getOutputFilePath(pathImageSelected, "vision_detected.png",
				txtOutputFolderEmbeddedFilePath.getText());
		imageCommonHandle.loadImageFromPathToLabel(visionImageDetectedPath, lbLoadOutputVisionImage, panelOutputVisionImage);
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
		fileService.createOutputDirectory(outputFolderEmbedded + "\\Vision_Detected");

		mapMetadataParams = fileService.readFlirMetadataParams(metadataFilePath);

		try {
			File embeddedFile = new File(embeddedImagePath);

			if (embeddedFile.exists() && embeddedFile.length() > 0) {
				Mat matEmbeddedImage = Imgcodecs.imread(embeddedImagePath, Imgcodecs.IMREAD_UNCHANGED);
				Mat matVisionImage = getMatVisionImage(matEmbeddedImage, mapMetadataParams);

				String visionImagePath = fileService.getOutputFilePath(pathImageSelected, "vision.png", txtOutputFolderEmbeddedFilePath.getText());
				Imgcodecs.imwrite(visionImagePath, matVisionImage);

				// create vision image detected YOLO
				Mat matVisionImageDetected = getMatVisionImageDetected(matVisionImage);
				String visionImageDetectedPath = fileService.getOutputFilePath(pathImageSelected, "vision_detected.png",
						txtOutputFolderEmbeddedFilePath.getText());
				Imgcodecs.imwrite(visionImageDetectedPath, matVisionImageDetected);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
			System.err.println("Cannot extract vision image at: " + pathImageSelected);
			return;
		}

	}

	private Mat getMatVisionImageDetected(Mat matVisionImage) {
		try {
			// Load labels
			List<String> labels = new ArrayList<>();

			try ( InputStream in = getClass().getResourceAsStream("/labels.txt");/*InputStream in = getClass().getResourceAsStream("/labels_wire.txt");*/
					BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
				String line;
				while ((line = br.readLine()) != null) {
					labels.add(line.trim());
				}
			} catch (Exception e) {
				e.printStackTrace();
				return matVisionImage;
			}

			int classCount = labels.size();

			// Letter box resize
			int inputW = 640;
			int inputH = 640;
			int origW = matVisionImage.cols();
			int origH = matVisionImage.rows();

			double scale = Math.min((double) inputW / origW, (double) inputH / origH);
			int resizedW = (int) Math.round(origW * scale);
			int resizedH = (int) Math.round(origH * scale);

			Mat resized = new Mat();
			Imgproc.resize(matVisionImage, resized, new Size(resizedW, resizedH));

			Mat inputBlobImage = Mat.zeros(new Size(inputW, inputH), matVisionImage.type());
			int dx = (inputW - resizedW) / 2;
			int dy = (inputH - resizedH) / 2;
			resized.copyTo(inputBlobImage.submat(dy, dy + resizedH, dx, dx + resizedW));

			// Create blob and forward
			Mat blob = Dnn.blobFromImage(inputBlobImage, 1.0 / 255.0, new Size(inputW, inputH), new Scalar(0, 0, 0), true, false);
			net.setInput(blob);

			List<Mat> outputs = new ArrayList<>();
			net.forward(outputs, net.getUnconnectedOutLayersNames());

			if (outputs.isEmpty()) {
				return matVisionImage;
			}

			Mat out = outputs.get(0);

			// YOLOv8 ONNX output: [1, 84, N]
			int rows = out.size(2); // N
			int cols = out.size(1); // 4 + num_classes

			Mat transposed = out.reshape(1, cols).t();

			// Parse predictions
			float confThreshold = 0.25f; // Confidence threshold YOLO
			float nmsThreshold = 0.45f; // Non-Maximum Suppression YOLO

			List<Rect> boxes = new ArrayList<>();
			List<Float> confidences = new ArrayList<>();
			List<Integer> classIds = new ArrayList<>();

			int imgW = matVisionImage.width();
			int imgH = matVisionImage.height();

			float[] rowData = new float[cols];
			float cx = 0.0f;
			float cy = 0.0f;
			float w = 0.0f;
			float h = 0.0f;
			int x = 0; // x locate bounding box
			int y = 0; // y locate bounding box
			int bw = 0; // width bounding box
			int bh = 0; // height bounding box
			float maxScore = 0.0f;
			int bestClass = -1;
			float score = 0.0f;
			float finalConf = 0.0f; // final confidence
			for (int i = 0; i < rows; i++) {
				transposed.get(i, 0, rowData);

				cx = rowData[0];
				cy = rowData[1];
				w = rowData[2];
				h = rowData[3];

				// Scale to original image
				cx = (float) ((cx - dx) / scale);
				cy = (float) ((cy - dy) / scale);
				w /= scale;
				h /= scale;

				x = (int) Math.round(cx - w / 2);
				y = (int) Math.round(cy - h / 2);
				bw = (int) Math.round(w);
				bh = (int) Math.round(h);

				if (bw <= 0 || bh <= 0 || x >= imgW || y >= imgH) {
					continue;
				}

				// clip to image
				x = Math.max(0, Math.min(x, imgW - 1));
				y = Math.max(0, Math.min(y, imgH - 1));
				if (x + bw > imgW) {
					bw = imgW - x;
				}
				if (y + bh > imgH) {
					bh = imgH - y;
				}

				// class confidence
				maxScore = 0;
				bestClass = -1;
				for (int c = 0; c < classCount; c++) {
					score = rowData[4 + c];
					if (score > maxScore) {
						maxScore = score;
						bestClass = c;
					}
				}

				finalConf = maxScore;
				if (finalConf < confThreshold) {
					continue;
				}

				boxes.add(new Rect(x, y, bw, bh));
				confidences.add(finalConf);
				classIds.add(bestClass);
			}

			// If no detection
			if (boxes.isEmpty()) {
				return matVisionImage;
			}

			// NMS
			Rect2d[] rect2dArr = new Rect2d[boxes.size()];
			for (int i = 0; i < boxes.size(); i++) {
				Rect r = boxes.get(i);
				rect2dArr[i] = new Rect2d(r.x, r.y, r.width, r.height);
			}
			MatOfRect2d matBoxes = new MatOfRect2d(rect2dArr);

			float[] confArr = new float[confidences.size()];
			for (int i = 0; i < confidences.size(); i++)
				confArr[i] = confidences.get(i);
			MatOfFloat matConf = new MatOfFloat();
			matConf.fromArray(confArr);

			MatOfInt indices = new MatOfInt();
			Dnn.NMSBoxes(matBoxes, matConf, confThreshold, nmsThreshold, indices);

			// Tạo màu ngẫu nhiên nhưng cố định cho mỗi class
			int numClasses = labels.size();
			Map<Integer, Scalar> classColors = new HashMap<>();
			float hue_HSV = 0.0f;
			float saturation_HSV = 0.0f;
			float value_HSV = 0.0f;
			int h_HSV = 0;
			int s_HSV = 0;
			int v_HSV = 0;
			Mat hsv = null;
			Mat bgr = null;
			double[] color = null;
			for (int i = 0; i < numClasses; i++) {
				hue_HSV = (float) i / numClasses; // 0.0 → 1.0
				saturation_HSV = 1.0f; // max
				value_HSV = 1.0f; // max

				// Chuyển HSV sang BGR (OpenCV sử dụng 0-255)
				h_HSV = (int) (hue_HSV * 179); // OpenCV H range: 0-179
				s_HSV = (int) (saturation_HSV * 255);
				v_HSV = (int) (value_HSV * 255);

				hsv = new Mat(1, 1, CvType.CV_8UC3, new Scalar(h_HSV, s_HSV, v_HSV));
				bgr = new Mat();
				Imgproc.cvtColor(hsv, bgr, Imgproc.COLOR_HSV2BGR);
				color = bgr.get(0, 0);
				classColors.put(i, new Scalar(color)); // Scalar(B,G,R)
			}

			// Draw results
			Mat outImg = matVisionImage.clone();
			Rect b = null;
			int clsId = 0; // class Id
			String label = "";
			int[] baseline = null;
			Size labelSize = null;
			int labelX = 0;
			int labelY = 0;
			Scalar scalarColor = null;
			for (int idx : indices.toArray()) {
				b = boxes.get(idx);
				clsId = classIds.get(idx);
				label = labels.get(clsId) + " " + String.format("%.2f", confidences.get(idx));

				// Lấy màu class từ map
				scalarColor = classColors.getOrDefault(clsId, new Scalar(0, 255, 0));

				// Draw bounding box
				Imgproc.rectangle(outImg, b, scalarColor, 2);

				// Calculate label location inside bounding box
				baseline = new int[1];
				labelSize = Imgproc.getTextSize(label, Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, 2, baseline);

				// Ensure labels do not cross borders
				labelX = b.x + 2; // 2 px from left border of bounding box
				labelY = b.y + (int) labelSize.height + 2; // 2 px from bounding box top border

				if (labelX + labelSize.width > outImg.cols()) {
					labelX = outImg.cols() - (int) labelSize.width - 1;
				}

				if (labelY > outImg.rows()) {
					labelY = outImg.rows() - 1;
				}

				// Draw text label
				Imgproc.putText(outImg, label, new Point(labelX, labelY), Imgproc.FONT_HERSHEY_SIMPLEX, 0.7, scalarColor, 2);
			}

			return outImg;

		} catch (Exception ex) {
			ex.printStackTrace();
			return matVisionImage;
		}
	}

	private Net loadOnnxModel() {
		try {
			// Load .onnx model file
			InputStream in = getClass().getResourceAsStream("/best2.onnx");
//			InputStream in = getClass().getResourceAsStream("/best_wire.onnx");
			if (in == null) {
				JOptionPane.showMessageDialog(null, "Cannot find .onnx model in resources", "ERROR", JOptionPane.ERROR_MESSAGE);
				throw new IOException("Cannot find best.onnx in resources");
			}

			// Copy to temporary file
			File temp = File.createTempFile("best_model2", ".onnx");
//			File temp = File.createTempFile("best_model_wire", ".onnx");
			temp.deleteOnExit();

			try (FileOutputStream out = new FileOutputStream(temp)) {
				byte[] buffer = new byte[4096];
				int bytesRead;

				while ((bytesRead = in.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
			}

			// ONNX from temp path
			return Dnn.readNetFromONNX(temp.getAbsolutePath());

		} catch (Exception e) {
			e.printStackTrace();
			return null;
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

		int origHeight = matEmbeddedImage.rows();
		int origWidth = matEmbeddedImage.cols();

		// Scale embedded image by Real 2 IR parameter
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

		return imgVision;

//		// shift left 13 pixel
//		int shiftX = -13;
//		int shiftY = 0;
//
//		// Create 2x3 translation matrix
//		Mat translationMatrix = new Mat(2, 3, CvType.CV_32F);
//		translationMatrix.put(0, 0, 1);
//		translationMatrix.put(0, 1, 0);
//		translationMatrix.put(0, 2, shiftX);
//		translationMatrix.put(1, 0, 0);
//		translationMatrix.put(1, 1, 1);
//		translationMatrix.put(1, 2, shiftY);
//
//		Mat imageVisionshifted = new Mat();
//		Imgproc.warpAffine(imgVision, imageVisionshifted, translationMatrix, imgVision.size(), Imgproc.INTER_LINEAR, Core.BORDER_REPLICATE);
//
//		return imageVisionshifted;

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
			JOptionPane.showMessageDialog(null, "Cannot load raw thermal image 16 bit: " + rawThermalImageSelectedPath, "WARNING",
					JOptionPane.WARNING_MESSAGE);
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

		// Load temperature map to temperature image
		Mat temperatureMapDisplay = new Mat();
		Core.normalize(temperatureMap, temperatureMapDisplay, 0, 255, Core.NORM_MINMAX);
		temperatureMapDisplay.convertTo(temperatureMapDisplay, CvType.CV_8U);
		imageCommonHandle.loadCVMatToLabel(temperatureMapDisplay, lbLoadOutputTemperatureImage, panelOutputTemperatureImage);
	}
}
