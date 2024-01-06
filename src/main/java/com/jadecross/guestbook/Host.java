package com.jadecross.guestbook;

import java.net.InetAddress;

public class Host {
	
	private String name;
	private String ip;

	public Host() {
		try {
			this.name = InetAddress.getLocalHost().getHostName();
			this.ip = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception ex) {
			this.name = "";
			this.ip = "";
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
}
