package fr.da2i.tache.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

import fr.da2i.tache.entity.Client;
import fr.da2i.tache.entity.Code;
import fr.da2i.tache.entity.Message;


public class Service implements Runnable {
	
	public static final String REQUEST_PATTERN = "(GET|POST|PUT|DELETE) \\/(taches|users)\\/{0,1}[0-9]*( *\\w*)|( [a-z]+=[\\w|\\s]+){0,1}(:[a-z]+=[\\w|\\s]+)*";
	//1 old regex : (GET|POST|PUT|DELETE) /(taches|users)/[0-9]* [a-z]+=[\\w|\\s]+(:[a-z]+=[\\w|\\s]+)*
	//2 old regex : (GET|POST|PUT|DELETE) \\/(taches|users)\\/{0,1}[0-9]*( [a-z]+=[\\w|\\s]+){0,1}(:[a-z]+=[\\w|\\s]+)*
	//(GET|POST|PUT|DELETE) \\/(taches|users)\\/{0,1}[0-9]*( *\\w*)|( [a-z]+=[\\w|\\s]+){0,1}(:[a-z]+=[\\w|\\s]+)*
	private Socket socketClient;
	private String method;
	private String resource;
	private String data;
	
	public Service(Socket client) {
		this.socketClient = client;
		this.data = "";
	}
	
	private void parse(String request) {
		StringTokenizer st = new StringTokenizer(request);
		method = st.nextToken();
		resource = st.nextToken();
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
		try (Scanner sc = new Scanner(socketClient.getInputStream())) {
			String str = sc.nextLine();
			System.out.println("Requête reçue : " + str);
			if (str.matches(REQUEST_PATTERN)) {
				System.out.println("match");
				parse(str);
				Message result = execute();
				System.out.println("Result = " + result);
				try (PrintWriter out = new PrintWriter(socketClient.getOutputStream())) {
					out.println(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
				ServiceManager sm = ServiceManager.getInstance();
				for (Client client : sm.getClients().values()) {
					try (PrintWriter out = new PrintWriter(client.getSocket().getOutputStream())) {
						out.println(result);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			else {
				try (PrintWriter out = new PrintWriter(socketClient.getOutputStream())) {
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
