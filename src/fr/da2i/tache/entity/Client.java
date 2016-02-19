package fr.da2i.tache.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

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
	
	public int getChoice() {
		System.out.println("Connecté avec " + login);
		System.out.println("1. Informations sur une tâche");
		System.out.println("2. Créer une tâche");
		System.out.println("3. Mettre à jour une tâche");
		System.out.println("4. Supprimer une tâche");
		System.out.println("5. Créer un utilisateur");
		System.out.println("Votre choix ?");
		
		Scanner sc = new Scanner(System.in);
		return Integer.parseInt(sc.nextLine());
	}

	public static void main(String[] args) {
		//Client client = new Client(args[0], Integer.parseInt(args[1]), args[2]);
		Client client = new Client("localhost", 9876, "julien");
		String request = "";
		Scanner sc;
		
		switch(client.getChoice()) {
		case 1:
			request = "GET /taches/";
			System.out.println("Information sur une tâche");
			System.out.println("id :");
			sc = new Scanner(System.in);
			request += sc.nextLine();
			break;
		case 2:
			request = "POST /taches createur=" + client.getLogin();
			System.out.println("Création d'une tâche");
			System.out.println("description :");
			sc = new Scanner(System.in);
			request += ":description=" + sc.nextLine();
			System.out.println("executant :");
			request += ":executant=" + sc.nextLine();
			break;
		case 3:
			request = "PUT /taches/";
			System.out.println("Mise à jour d'une tâche");
			System.out.println("id :");
			sc = new Scanner(System.in);
			request += sc.nextLine() + " ";
			System.out.println("etat :");
			sc = new Scanner(System.in);
			request += "etat=" + sc.nextLine();
			System.out.println("executant :");
			sc = new Scanner(System.in);
			request += "executant=" + sc.nextLine();
			break;
		case 4:
			request = "DELETE /taches/";
			System.out.println("Supprimer une tâche");
			System.out.println("id :");
			sc = new Scanner(System.in);
			request += sc.nextLine();
			break;
		case 5:
			request = "PUT /users/";
			System.out.println("Création d'un nouvel utilisateur");
			System.out.println("id :");
			sc = new Scanner(System.in);
			request += sc.nextLine();
			break;
		default:
			System.out.println("Choix incorrect");
		}
		String receipt = client.send(request);
		System.out.println(receipt);
	}
}
