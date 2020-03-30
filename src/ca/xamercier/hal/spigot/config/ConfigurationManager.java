package ca.xamercier.hal.spigot.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import ca.xamercier.hal.spigot.HALSpigot;

/**
 * This file is a part of the HAL project
 *
 * @author Xavier Mercier | xamercier
 */
public class ConfigurationManager {

	private JSONObject halClient;

	/**
	 * Creating new instance of the ConfigurationManager
	 *
	 * @throws IOException
	 *             if config parser don't finhish with success
	 */
	public ConfigurationManager() throws IOException {
		loadConfigurationFile();
	}

	/**
	 * Loading the config.json file and parse it
	 *
	 * @throws IOException
	 *             if config parser don't finish with success
	 */
	private void loadConfigurationFile() throws IOException {
		URL defaultFile = getClass().getClassLoader().getResource("config.default.json");
		File configFile = new File(HALSpigot.getInstance().getDataFolder() + File.separator, "config.json");
		if (!configFile.exists()) {
			if (defaultFile == null) {
				throw new FileNotFoundException("Unable to create default config file");
			}
			FileUtils.copyURLToFile(defaultFile, configFile);
		}

		BufferedReader reader = new BufferedReader(new FileReader(configFile));

		StringBuilder builder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			builder.append(line.trim());
		}
		reader.close();
		String content = builder.toString();

		JSONObject globalConfiguration = new JSONObject(content);

		halClient = globalConfiguration.getJSONObject("HALClient");

	}

	public String getHalClientHost() {
		return this.halClient.getString("host");
	}

	public int getHalClientPort() {
		return this.halClient.getInt("port");
	}

}
