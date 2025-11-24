package com.tvt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppConfiguration {
	private Environment env;

	public AppConfiguration() {
	}

	@Autowired
	public AppConfiguration(Environment env) {
		this.env = env;
	}

	public Environment getEnv() {
		return env;
	}
	
	public void setEnv(Environment env) {
		this.env = env;
	}
	
	public String getOutputRootEmbeddedFileFolder() {
		return env.getProperty("output.root.embedded.file.folder");
	}
	
	public String getOutputMetadataFolder() {
		return env.getProperty("output.metadata.folder");
	}
	
	public String getOutputEmbeddedImageFolder() {
		return env.getProperty("output.embedded.image.folder");
	}
	
	public String getOutputVisionImageFolder() {
		return env.getProperty("output.vision.image.folder");
	}
	
	public String getOutputRawThermalImageFolder() {
		return env.getProperty("output.raw.thermal.image.folder");
	}
	
	public String getInitCurrentDirectoryFileChooser() {
		return env.getProperty("init.current.directory.file.chooser");
	}
	
	public String getUILookAndFeel() {
		return env.getProperty("ui.look.and.feel");
	}
}
