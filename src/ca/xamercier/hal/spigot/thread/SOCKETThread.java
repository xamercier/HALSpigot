package ca.xamercier.hal.spigot.thread;

import ca.xamercier.hal.spigot.halClient.thread.MainHalClientThread;

/**
 * This file is a part of the HAL project
 *
 * @author Xavier Mercier | xamercier
 */
public class SOCKETThread extends Thread {

	public void run() {
		while (true) {
			try {
				Thread.sleep(300000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			MainHalClientThread.getClient().send("null");
		}
	}

}