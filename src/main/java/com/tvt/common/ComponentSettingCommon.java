package com.tvt.common;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.opencv.imgcodecs.Imgcodecs;

public class ComponentSettingCommon {
	public ComponentSettingCommon() {
	}

	public void settingJFileChooser(JFileChooser fileChooser, String dialogTitle, int fileSelectionMode , boolean isMultiSelected, FileFilter fileFilter, String currentDirectoryPath) {
		if (fileChooser != null) {
			if (dialogTitle != null) {
				fileChooser.setDialogTitle(dialogTitle);
			} else {
				fileChooser.setDialogTitle("Please select your files or directories");
			}
			
			fileChooser.setFileSelectionMode(fileSelectionMode);
			fileChooser.setMultiSelectionEnabled(isMultiSelected);
			
			if (fileFilter != null) {
				fileChooser.setFileFilter(fileFilter);
			}
			
			fileChooser.setCurrentDirectory(new File(currentDirectoryPath));
			
		}
	}
}
