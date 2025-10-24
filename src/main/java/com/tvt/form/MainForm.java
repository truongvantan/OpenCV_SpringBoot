package com.tvt.form;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainForm extends JFrame {

	private static final long serialVersionUID = 1L;
	
//	@Autowired
	private final FormExtractEmbeddedImage formExtractEmbeddedImage;
	
//	@Autowired
	private final FormConvertImageExtension formConvertImageExtension;
	
//	@Autowired
	private final FormAccessPixelValueDemo formAccessPixelValueDemo;
	
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenuItem menuItemAccessPixelValueDemo;
	private JMenuItem menuItemExtractEmbeddedImage;
	private JMenuItem menuItemConvertImageExtension;
	
	
//	public MainForm() {
//		initComponent();
//	}
	
	@Autowired
	public MainForm(FormExtractEmbeddedImage formExtractEmbeddedImage,
			FormConvertImageExtension formConvertImageExtension, FormAccessPixelValueDemo formAccessPixelValueDemo)
			throws HeadlessException {
		this.formExtractEmbeddedImage = formExtractEmbeddedImage;
		this.formConvertImageExtension = formConvertImageExtension;
		this.formAccessPixelValueDemo = formAccessPixelValueDemo;
		
		initComponent();
	}


	private void initComponent() {
		this.setTitle("Main Form");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 1100, 619);
		this.setLocationRelativeTo(null);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		mnNewMenu = new JMenu("Excersise");
		menuBar.add(mnNewMenu);
		
		menuItemAccessPixelValueDemo = new JMenuItem("Access Pixel Value Demo");
		mnNewMenu.add(menuItemAccessPixelValueDemo);
		
		menuItemExtractEmbeddedImage = new JMenuItem("Extract Embedded Image");
		mnNewMenu.add(menuItemExtractEmbeddedImage);
		
		menuItemConvertImageExtension = new JMenuItem("Convert Image Extension");
		mnNewMenu.add(menuItemConvertImageExtension);
		
		handleEvent();
	}
	
	private void handleEvent() {
		menuItemAccessPixelValueDemo_Click();
		menuItemExtractEmbeddedImage_Click();
		menuItemConvertImageExtension_Click();
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
