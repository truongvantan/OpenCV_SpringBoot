package com.tvt.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tvt.common.Constant;
import com.tvt.config.AppConfiguration;

@Component
public class FileService {

	private final AppConfiguration appConfiguration;

	@Autowired
	public FileService(AppConfiguration appConfiguration) {
		this.appConfiguration = appConfiguration;
	}

	public String getOutputFilePath(String pathImageSelected, String fileType) {
		// Format output file name
		File inputFile = new File(pathImageSelected);
		String baseName = inputFile.getName().replaceFirst("[.][^.]+$", ""); // remove extension

		switch (fileType) {
		case "metadata.txt":
			return new StringBuffer("").append(appConfiguration.getOutputEmbeddedImageFolder()).append("\\").append(baseName).append("_metadata.txt")
					.toString();
		case "embedded.png":
			return new StringBuffer("").append(appConfiguration.getOutputEmbeddedImageFolder()).append("\\").append(baseName).append("_embedded.png")
					.toString();
		case "raw_thermal.png":
			return new StringBuffer("").append(appConfiguration.getOutputEmbeddedImageFolder()).append("\\").append(baseName)
					.append("_raw_thermal.png").toString();
		case "vision.png":
			return new StringBuffer("").append(appConfiguration.getOutputEmbeddedImageFolder()).append("\\").append(baseName).append("_vision.png")
					.toString();
		default:
			break;
		}

		return null;
	}

	public String extractMetadataFile(String pathImageSelected) {
		createOuputDirectory();

		String thermalImageMetaDataPath = getOutputFilePath(pathImageSelected, "metadata.txt");
		if (thermalImageMetaDataPath != null) {
			/**
			 * Extract Metadata of thermal image Example command line: exiftool
			 * <filename.jpg>
			 */
			ProcessBuilder processBuilder1 = new ProcessBuilder("exiftool", pathImageSelected);

			try {
				processBuilder1.redirectOutput(new File(thermalImageMetaDataPath));
				Process process1 = processBuilder1.start();
				process1.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_METADATA_FILE_FAIL;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_METADATA_FILE_FAIL;
			}

			return Constant.MESSAGE_EXTRACT_METADATA_FILE_SUCCESS;
		} else {
			return Constant.MESSAGE_EXTRACT_METADATA_FILE_FAIL;
		}

	}

	private void createOuputDirectory() {
		Path path = Paths.get(appConfiguration.getOutputEmbeddedImageFolder());

		if (Files.exists(path)) {
			File outputDir = new File(appConfiguration.getOutputEmbeddedImageFolder());
			outputDir.mkdirs();
		}
	}

	public String extractEmbeddedImageFile(String pathImageSelected) {
		createOuputDirectory();
		String embeddedImagePath = getOutputFilePath(pathImageSelected, "embedded.png");

		if (embeddedImagePath != null) {
			/**
			 * Run ExifTool to extract embedded image Example command line: exiftool -b
			 * -EmbeddedImage input.jpg > output.png
			 */
			ProcessBuilder pb2 = new ProcessBuilder("exiftool", "-b", "-EmbeddedImage", pathImageSelected);

			try {
				pb2.redirectOutput(new File(embeddedImagePath));
				Process process = pb2.start();
				process.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_FAIL;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_FAIL;
			}

			return Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_SUCCESS;
		} else {
			return Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_FAIL;
		}

	}

	public Map<String, String> readFlirMetadataParams(String filePath) {
		Map<String, String> params = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = null;
			String[] parts = null;
			String key = null;
			String value = null;
			double val = 0.0;
			Pattern LINE = Pattern.compile("^([^:]+)\\s*:\\s*(.*)$");
			Matcher mat = null;

			while ((line = br.readLine()) != null) {
				mat = LINE.matcher(line);
				if (mat.find()) {
					params.put(mat.group(1).trim(), mat.group(2).trim());
				}

//                parts = line.split(":", 2);
//                
//                if (parts.length < 2) {
//                	continue;
//                }
//                
//                key = parts[0].trim();
//                
//                value = parts[1].trim().split(" ")[0];
//                
//                try {
//                    val = Double.parseDouble(value);
//                    params.put(key, val);
//                } catch (NumberFormatException e) {
//                	e.printStackTrace();
//                }
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return params;
	}

	public String extractRawThermalImageFile(String pathImageSelected) {
		createOuputDirectory();
		String rawThermalImagePath = getOutputFilePath(pathImageSelected, "raw_thermal.png");

		if (rawThermalImagePath != null) {
			/**
			 * Run ExifTool to extract raw thermal image Example command line: exiftool
			 * -RawThermalImage -b -w output.png input.jpg
			 */
			ProcessBuilder pb2 = new ProcessBuilder("exiftool", "-RawThermalImage", "-b", pathImageSelected);

			try {
				pb2.redirectOutput(new File(rawThermalImagePath));
				Process process = pb2.start();
				process.waitFor();
			} catch (IOException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_FAIL;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_FAIL;
			}

			return Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_SUCCESS;
		} else {
			return Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_FAIL;
		}
	}

}
