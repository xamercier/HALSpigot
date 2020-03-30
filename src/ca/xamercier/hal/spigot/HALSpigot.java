package ca.xamercier.hal.spigot;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;

import ca.xamercier.hal.spigot.config.ConfigurationManager;
import ca.xamercier.hal.spigot.halClient.thread.MainHalClientThread;
import ca.xamercier.hal.spigot.utils.ErrorCatcher;


/**
 * This file is a part of the HAL project
 *
 * @author Xavier Mercier | xamercier
 */
public class HALSpigot extends JavaPlugin{
	
	private static HALSpigot instance;
	private ConfigurationManager configurationManager;
	
	public void onLoad() {
	}
	
	public void onEnable() {
		instance = this;

		try {
			this.configurationManager = new ConfigurationManager();
		} catch (IOException e) {
			ErrorCatcher.catchError(e);
		}
		
		MainHalClientThread MainHalClientThread = new MainHalClientThread();
		MainHalClientThread.start();
		
		/*
		SOCKETThread SOCKETThread = new SOCKETThread();
		SOCKETThread.start();
		*/
	}
	
	public void onDisable() {
		
	}
	
	/**
	 * Get configuration manager ({@link ConfigurationManager})
	 *
	 * @return the configuration manager
	 */
	public ConfigurationManager getConfigurationManager() {
		return configurationManager;
	}

	/**
	 * Get bungee ({@link HALBungee}) instance
	 *
	 * @return the {@link HALBungee}
	 */
	public static HALSpigot getInstance() {
		return instance;
	}
}
