package com.tvt.form;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tvt.config.AppConfiguration;
import com.tvt.form.digital_communication.FormExcersise2;
import com.tvt.form.digital_communication.FormSVD;
import com.tvt.form.digital_communication.FormSVDCustomFilter;
import com.tvt.form.image_processing.FormAccessPixelValueDemo;
import com.tvt.form.image_processing.FormConvertImageExtension;
import com.tvt.form.image_processing.FormExtractEmbeddedImage;

import javax.swing.SwingConstants;

@Component
public class MainForm extends JFrame {

	private static final long serialVersionUID = 1L;

	private final AppConfiguration appConfiguration;

	private final FormExtractEmbeddedImage formExtractEmbeddedImage;
	private final FormConvertImageExtension formConvertImageExtension;
	private final FormAccessPixelValueDemo formAccessPixelValueDemo;
	private final FormExcersise2 formExcersise2;
	private final FormSVD formSVD;
	private final FormSVDCustomFilter formSVDCustomFilter;

	private JMenuBar menuBar;
	private JMenu mnOpenCVDemo;
	private JMenuItem menuItemAccessPixelValueDemo;
	private JMenuItem menuItemExtractEmbeddedImage;
	private JMenuItem menuItemConvertImageExtension;
	private JMenu mnExcersises;
	private JMenuItem menuItemExcersise2;
	private JMenuItem mntmSVD;
	private JMenuItem mntmSVDCustomFilter;
	
	@Autowired
	public MainForm(AppConfiguration appConfiguration, FormExtractEmbeddedImage formExtractEmbeddedImage,
			FormConvertImageExtension formConvertImageExtension, FormAccessPixelValueDemo formAccessPixelValueDemo, FormExcersise2 formExcersise2,
			FormSVD formSVD, FormSVDCustomFilter formSVDCustomFilter) throws HeadlessException {
		
		this.appConfiguration = appConfiguration;
		this.formExtractEmbeddedImage = formExtractEmbeddedImage;
		this.formConvertImageExtension = formConvertImageExtension;
		this.formAccessPixelValueDemo = formAccessPixelValueDemo;
		this.formExcersise2 = formExcersise2;
		this.formSVD = formSVD;
		this.formSVDCustomFilter = formSVDCustomFilter;
		
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

		this.setTitle("Main Form");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 1100, 619);
		this.setLocationRelativeTo(null);

		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnOpenCVDemo = new JMenu("Open CV Demo");
		menuBar.add(mnOpenCVDemo);

		menuItemAccessPixelValueDemo = new JMenuItem("Access Pixel Value Demo");
		menuItemAccessPixelValueDemo.setHorizontalTextPosition(SwingConstants.LEFT);
		menuItemAccessPixelValueDemo.setHorizontalAlignment(SwingConstants.LEFT);
		mnOpenCVDemo.add(menuItemAccessPixelValueDemo);

		menuItemExtractEmbeddedImage = new JMenuItem("Extract Embedded Image");
		mnOpenCVDemo.add(menuItemExtractEmbeddedImage);

		menuItemConvertImageExtension = new JMenuItem("Convert Image Extension");
		mnOpenCVDemo.add(menuItemConvertImageExtension);

		mnExcersises = new JMenu("Excersises");
		menuBar.add(mnExcersises);

		menuItemExcersise2 = new JMenuItem("Excersise 2");
		mnExcersises.add(menuItemExcersise2);

		mntmSVD = new JMenuItem("SVD");

		mnExcersises.add(mntmSVD);
		
		mntmSVDCustomFilter = new JMenuItem("SVD Custom Filter");
		mnExcersises.add(mntmSVDCustomFilter);

		handleEvent();
	}

	private void handleEvent() {
		menuItemAccessPixelValueDemo_Click();
		menuItemExtractEmbeddedImage_Click();
		menuItemConvertImageExtension_Click();
		menuItemExcersise2_Click();
		mntmSVD_Click();
		mntmSVDCustomFilter_Click();
	}

	private void mntmSVDCustomFilter_Click() {
		mntmSVDCustomFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formSVDCustomFilter.setVisible(true);
			}
		});
	}

	private void mntmSVD_Click() {
		mntmSVD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formSVD.setVisible(true);
			}
		});
	}

	private void menuItemExcersise2_Click() {
		menuItemExcersise2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				formExcersise2.setVisible(true);
			}
		});
	}

	private void menuItemConvertImageExtension_Click() {
		menuItemConvertImageExtension.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				JFrame form = new FormConvertImageExtension();
//				form.setVisible(true);
				formConvertImageExtension.setVisible(true);
			}
		});
	}

	private void menuItemAccessPixelValueDemo_Click() {
		menuItemAccessPixelValueDemo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				JFrame form = new FormAccessPixelValueDemo();
//				form.setVisible(true);
				formAccessPixelValueDemo.setVisible(true);
			}
		});
	}

	private void menuItemExtractEmbeddedImage_Click() {
		menuItemExtractEmbeddedImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				JFrame form = new FormExtractEmbeddedImage();
//				form.setVisible(true);
				formExtractEmbeddedImage.setVisible(true);
			}
		});
	}
}
