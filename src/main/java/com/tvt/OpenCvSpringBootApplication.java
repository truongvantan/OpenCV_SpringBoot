package com.tvt;

import java.awt.EventQueue;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.swing.UIManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.tvt.config.AppConfiguration;
import com.tvt.form.MainForm;

@SpringBootApplication
public class OpenCvSpringBootApplication implements CommandLineRunner {
    static {
    	try {
    		loadOpenCvFromResources();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void loadOpenCvFromResources() throws IOException {
        try (InputStream in = OpenCvSpringBootApplication.class.getResourceAsStream("/opencv_java480.dll")) {
            File temp = File.createTempFile("opencv", ".dll");
            temp.deleteOnExit();
            Files.copy(in, temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.load(temp.getAbsolutePath());
        }
    }
    
    @Autowired
    private MainForm mainForm;
    
    @Autowired
    private AppConfiguration appConfiguration;
	
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "false");
		
		SpringApplication.run(OpenCvSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(appConfiguration.getUILookAndFeel());
					mainForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
