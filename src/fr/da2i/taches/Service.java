package fr.da2i.taches;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;


public class Service implements Runnable {
	
	public static final String REQUEST_PATTERN = "(GET|POST|PUT|DELETE) /taches/[0-9]* [a-z]+=[\\w|\\s]+(:[a-z]+=[\\w|\\s]+)*";
	
	private Socket client;
	
	public Service(Socket client) {
		this.client = client;
	}

	@Override
	public void run() {
		try {
			ServiceManager.getInstance();
			Scanner sc = new Scanner(client.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		String str = "POST /taches/1 createur=edouard:description=bonjour toto";
		
		String method;
		String resource;
		String data = "";
		
		if (str.matches(REQUEST_PATTERN)) {
			Scanner sc = new Scanner(str);
			method = sc.next();
			resource = sc.next();
			data = sc.nextLine().substring(1);
			System.out.println(method + "\n" + resource + "\n" + data);
		}

		
	}

}
