package fr.da2i.tache.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

import fr.da2i.tache.entity.Client;
import fr.da2i.tache.entity.Code;
import fr.da2i.tache.entity.Message;


public class Service implements Runnable {
	
	public static final String REQUEST_PATTERN = "(GET|POST|PUT|DELETE) (/taches/[0-9]*([\\s]*[a-z]+=[\\w|\\s]+(:[a-z]+=[\\w|\\s]+)*)*|/users/ [\\w]+)";
	
	private Socket socketClient;
	private String method;
	private String resource;
	private String data;
	
	public Service(Socket client) {
		this.socketClient = client;
	}
	
	private void parse(String request) {
		StringTokenizer st = new StringTokenizer(request);
		method = st.nextToken();
		resource = st.nextToken();
		data = "";
		while (st.hasMoreTokens()) {
			data += st.nextToken();
			if (st.hasMoreTokens()) {
				data += " ";
			}
		}
	}
	
	private Message execute() {
		if (resource.contains("taches")) {
			return new TacheService().execute(socketClient, method, resource, data);
		}
		else if (resource.contains("users")) {
			return new ClientService().execute(socketClient, method, resource, data);
		}
		return new Message(Code.BAD_REQUEST, "Bad request");
	}

	@Override
	public void run() {
		while (!socketClient.isClosed()) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
				String str = reader.readLine();
				System.out.println("Requête reçue : " + str);
				if (str.matches(REQUEST_PATTERN)) {
					parse(str);
					Message result = execute();
					ServiceManager sm = ServiceManager.getInstance();
					for (Client client : sm.getClients().values()) {
						try {
							PrintWriter out = new PrintWriter(client.getSocket().getOutputStream(), true);
							out.println(result);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				else {
					try {
						PrintWriter out = new PrintWriter(socketClient.getOutputStream(), true);
						out.println(new Message(Code.BAD_REQUEST, "Bad request"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
