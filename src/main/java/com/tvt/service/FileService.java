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

	public String getOutputFilePath(String pathImageSelected, String fileType, String outputFolderEmbedded) {
		// Format output file name
		File inputFile = new File(pathImageSelected);
		String baseName = inputFile.getName().replaceFirst("[.][^.]+$", ""); // remove extension

		switch (fileType) {
		case "metadata.txt":
			return new StringBuffer("").append(outputFolderEmbedded).append("\\Metadata").append("\\").append(baseName).append("_metadata.txt")
					.toString();
		case "embedded.png":
			return new StringBuffer("").append(outputFolderEmbedded).append("\\Embedded").append("\\").append(baseName).append("_embedded.png")
					.toString();
		case "raw_thermal.png":
			return new StringBuffer("").append(outputFolderEmbedded).append("\\Raw_Thermal").append("\\").append(baseName).append("_raw_thermal.png")
					.toString();
		case "vision.png":
			return new StringBuffer("").append(outputFolderEmbedded).append("\\Vision").append("\\").append(baseName).append("_vision.png")
					.toString();
		case "raw_thermal_little_endian.png":
			return new StringBuffer("").append(outputFolderEmbedded).append("\\Raw_Thermal").append("\\").append(baseName)
					.append("_raw_thermal_little_endian.png").toString();
		default:
			break;
		}

		return null;
	}

	public String extractMetadataFile(String pathImageSelected, String outputFolderEmbedded) {
		createOutputDirectory(outputFolderEmbedded + "\\Metadata");

		String metadataFilePath = getOutputFilePath(pathImageSelected, "metadata.txt", outputFolderEmbedded);

		if (metadataFilePath != null) {
			/**
			 * Extract Metadata of thermal image Example command line: exiftool
			 * <filename.jpg>
			 */
			Process process = null;

			try {
				ProcessBuilder processBuilder = new ProcessBuilder("exiftool", pathImageSelected);
				processBuilder.redirectOutput(new File(metadataFilePath));
				process = processBuilder.start();
				int exitCode = process.waitFor();

				if (exitCode == 0) {
					return Constant.MESSAGE_EXTRACT_METADATA_FILE_SUCCESS;
				} else {
					return Constant.MESSAGE_EXTRACT_METADATA_FILE_FAIL;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_METADATA_FILE_FAIL;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_METADATA_FILE_FAIL;
			} finally {
				if (process != null) {
					process.destroy();
				}
			}

		} else {
			return Constant.MESSAGE_EXTRACT_METADATA_FILE_FAIL;
		}

	}

	public void createOutputDirectory(String outputFolderEmbedded) {
		File folder = new File(outputFolderEmbedded);

		if (!folder.exists()) {
			boolean created = folder.mkdirs(); // Use mkdirs() to create parent directories if needed
			if (created) {
				System.out.println("Folder created successfully: " + outputFolderEmbedded);
			} else {
				System.err.println("Failed to create folder: " + outputFolderEmbedded);
			}
		} else {
			System.err.println("Folder already exists: " + outputFolderEmbedded);
		}
	}

	public String extractEmbeddedImageFile(String pathImageSelected, String outputFolderEmbedded) {
		createOutputDirectory(outputFolderEmbedded + "\\Embedded");
		String embeddedImagePath = getOutputFilePath(pathImageSelected, "embedded.png", outputFolderEmbedded);

		if (embeddedImagePath != null) {
			/**
			 * Run ExifTool to extract embedded image Example command line: exiftool -b
			 * -EmbeddedImage input.jpg > output.png
			 */
			Process process = null;
			try {
				ProcessBuilder processBuilder = new ProcessBuilder("exiftool", "-b", "-EmbeddedImage", pathImageSelected);
				processBuilder.redirectOutput(new File(embeddedImagePath));
				process = processBuilder.start();
				int exitCode = process.waitFor();

				if (exitCode == 0) {
					return Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_SUCCESS;
				} else {
					return Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_FAIL;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_FAIL;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_FAIL;
			} finally {
				if (process != null) {
					process.destroy();
				}
			}
		} else {
			return Constant.MESSAGE_EXTRACT_EMBEDDED_IMAGE_FILE_FAIL;
		}

	}

	public Map<String, String> readFlirMetadataParams(String filePath) {
		Map<String, String> params = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line = null;
			Pattern LINE = Pattern.compile("^([^:]+)\\s*:\\s*(.*)$");
			Matcher mat = null;

			while ((line = br.readLine()) != null) {
				mat = LINE.matcher(line);
				if (mat.find()) {
					params.put(mat.group(1).trim(), mat.group(2).trim());
				}
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return params;
	}

	public String extractRawThermalImageFile(String pathImageSelected, String outputFolderEmbedded) {
		createOutputDirectory(outputFolderEmbedded + "\\Raw_Thermal");
		String rawThermalImagePath = getOutputFilePath(pathImageSelected, "raw_thermal.png", outputFolderEmbedded);

		if (rawThermalImagePath != null) {
			/**
			 * Run ExifTool to extract raw thermal image Example command line: exiftool
			 * -RawThermalImage -b -w output.png input.jpg
			 */
			Process process = null;
			try {
				ProcessBuilder processBuilder = new ProcessBuilder("exiftool", "-RawThermalImage", "-b", pathImageSelected);
				processBuilder.redirectOutput(new File(rawThermalImagePath));
				process = processBuilder.start();
				int exitCode = process.waitFor();

				if (exitCode == 0) {
					return Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_SUCCESS;
				} else {
					return Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_FAIL;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_FAIL;
			} catch (InterruptedException e) {
				e.printStackTrace();
				return Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_FAIL;
			} finally {
				if (process != null) {
					process.destroy();
				}
			}

		} else {
			return Constant.MESSAGE_EXTRACT_RAW_THERMAL_IMAGE_FILE_FAIL;
		}
	}

}
