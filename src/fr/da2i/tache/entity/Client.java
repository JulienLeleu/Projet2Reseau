package fr.da2i.tache.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private Socket socket;
	private String login;
	private PrintWriter sending;
    private BufferedReader receipt;
	
	public Client(String host, int port, String login) {
		try {
			this.socket = new Socket(host, port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.login = login;
		try {
			sending = new PrintWriter(socket.getOutputStream(), true);
			receipt = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
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
	
	public String send(String message) {
		sending.println(message);
	    try {
			return receipt.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	    return null;
	}

	public static void main(String[] args) {
		//Client client = new Client(args[0], Integer.parseInt(args[1]), args[2]);
		Client client = new Client("localhost", 9876, "julien");
		String receipt = client.send("GET ");
		System.out.println(receipt);
		System.out.println("Client");
	}
}
