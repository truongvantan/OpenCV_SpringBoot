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
	
	public String getOutputEmbeddedImageFolder() {
		return env.getProperty("output.embedded.image.folder");
	}
	
	public String getInitCurrentDirectoryFileChooser() {
		return env.getProperty("init.current.directory.file.chooser");
	}
}
