package ca.xamercier.hal.spigot.halClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.bukkit.Bukkit;
import org.json.JSONException;
import org.json.JSONObject;

import ca.xamercier.hal.spigot.halClient.thread.MainHalClientThread;

/**
 * This file is a part of the HAL project
 *
 * @author Xavier Mercier | xamercier
 */
public class Client extends Thread {

	String hostName = "localhost";
	int portNumber = 12;
	boolean run = true;
	static Socket socket = null;
	PrintWriter out = null;
	BufferedReader in = null;

	public boolean init(String ip, int servPort) {
		this.portNumber = servPort;
		this.hostName = ip;
		try {
			socket = new Socket(hostName, portNumber);
			out = new PrintWriter(socket.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			return false;
		}
		this.start();
		return true;
	}

	public void send(String toSend) {
		if (toSend.startsWith("@")) {
			System.out.println("[HalClientSpigot] Vous ne pouvez pas commencer un message par un @");
		} else {
			out.println(toSend);
		}
	}

	public void run() {
		String msg = "";
		while (run) {
			try {
				msg = in.readLine();
			} catch (IOException e) {
				run = false;
			}
			if (msg != null && run == true) {
				if (msg.equalsIgnoreCase("@ServerClosed")) {
					run = false;
				} else if (!msg.equalsIgnoreCase("") && msg != null && !msg.equalsIgnoreCase("null")) {

					boolean isAPlayerRequest = false;

					JSONObject message = new JSONObject(msg);
					String log = "[HalClientSpigot]: read => " + message.toString();

					try {
						String player = message.getString("player");
						if (player != null) {
							isAPlayerRequest = true;
						} else {
							isAPlayerRequest = false;
						}
					} catch (JSONException e) {
						isAPlayerRequest = false;
					}

					if (!isAPlayerRequest == true) {

						String action = message.getString("action");

						switch (action.toLowerCase()) {
						case "stop": {
							log += " ok.";
							String portOfThisServer = Bukkit.getServer().getPort() + "";
							String serverPortOrName = message.getString("serverPortOrName");

							if (serverPortOrName.contains("_")) {
								String[] serverName = message.getString("serverPortOrName").split("_");
								String serverPort = serverName[1];
								if (serverPort.equalsIgnoreCase(portOfThisServer)) {
									Bukkit.getServer().shutdown();
								}
							} else {
								String serverPort = message.getString("serverPortOrName");
								if (serverPort.equalsIgnoreCase(portOfThisServer)) {
									Bukkit.getServer().shutdown();
								}
							}
						}

						default: {
							log += " skipped.";
						}
						}
					}

					System.out.println(log);

				}
			}
		}
		try {
			socket.close();
			MainHalClientThread.setRun(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopClient() {
		run = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
