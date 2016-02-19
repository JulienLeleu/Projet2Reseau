package fr.da2i.tache.entity;

import java.net.Socket;

public class Client {
	
	private Socket socket;
	private String login;
	
	public Client(Socket socket, String login) {
		this.socket = socket;
		this.login = login;
	}
	
	public String getLogin() {
		if (login == null) {
			login = "";
		}
		return login;
	}
	
	public Socket getSocket() {
		return socket;
	}

}
