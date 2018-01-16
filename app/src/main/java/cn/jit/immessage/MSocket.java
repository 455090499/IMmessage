package cn.jit.immessage;

import java.net.Socket;

public class MSocket {
	private String phone;
	private Socket socket;

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Socket getSocket() {
		return socket;
	}

	public MSocket(String phone, Socket socket) {
		setPhone(phone);
		setSocket(socket);
	}
}