package me.Christian.mainpack;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;

public class GetIp implements Runnable{
	@Override
	public void run() {
		URL url = null;
		BufferedReader in = null;
		String ipAddress = "";
		try {
			url = new URL("http://bot.whatismyipaddress.com");
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			ipAddress = in.readLine().trim();
			if (!(ipAddress.length() > 0)) {
				try {
					InetAddress ip = InetAddress.getLocalHost();
					System.out.println((ip.getHostAddress()).trim());
					ipAddress = (ip.getHostAddress()).trim();
				} catch(Exception exp) {
					ipAddress = "ERROR";
				}
			}
		} catch (Exception ex) {
			// This try will give the Private IP of the Host.
			try {
				InetAddress ip = InetAddress.getLocalHost();
				System.out.println((ip.getHostAddress()).trim());
				ipAddress = (ip.getHostAddress()).trim();
			} catch(Exception exp) {
				ipAddress = "ERROR";
			}
		}
		Main.MyIP = ipAddress;
		System.out.println("Hosting on public ip: " + Main.MyIP +":"+Main.ServerPort);
	}
}
