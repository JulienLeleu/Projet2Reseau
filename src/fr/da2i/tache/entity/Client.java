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
	
	public String getChoices() {
		String str = "";
		str += "Connecté avec " + login + "\n";
		str += "1. Informations sur une tâche" + "\n";
		str += "2. Créer une tâche" + "\n";
		str += "3. Modifier une tâche" + "\n";
		str += "4. Supprimer une tâche" + "\n";
		str += "5. Créer un utilisateur" + "\n";
		str += "Votre choix ?";
		return str;
	}

	public static void main(String[] args) {
		//Client client = new Client(args[0], Integer.parseInt(args[1]), args[2]);
		Client client = new Client("localhost", 9876, "julien");
		String request = "";						//Requête envoyée au serveur (sous la forme GET|POST|PUT|DELETE ...)
		System.out.println(client.getChoices());
		Scanner sc = new Scanner(System.in);		//Scanner utilisé pour les infos complémentaires

		switch(Integer.parseInt(sc.nextLine())) {
		case 1:
			request = "GET /taches/";
			System.out.println("Information sur une tâche");
			System.out.println("id :");
			request += sc.next();
			break;
		case 2:
			request = "POST /taches createur=" + client.getLogin();
			System.out.println("Création d'une tâche");
			System.out.println("description :");
			request += ":description=" + sc.nextLine();
			System.out.println("executant :");
			request += ":executant=" + sc.nextLine();
			break;
		case 3:
			request = "PUT /taches/";
			System.out.println("Modifier une tâche");
			System.out.println("id :");
			request += sc.nextLine() + " ";
			System.out.println("nouveau créateur :\n");
			request += "createur=" + sc.nextLine();
			System.out.println("nouvelle description :\n");
			request += ":description=" + sc.nextLine();
			System.out.println("nouvel executant :\n");
			request += ":executant=" + sc.nextLine();
			System.out.println("nouvel etat : (TODO|DONE)\n");
			request += ":etat=" + sc.nextLine();
			break;
		case 4:
			request = "DELETE /taches/";
			System.out.println("Supprimer une tâche");
			System.out.println("id :");
			request += sc.nextLine();
			break;
		case 5:
			request = "POST /users ";
			System.out.println("Création d'un nouvel utilisateur");
			System.out.println("login :");
			request += sc.nextLine();
			break;
		default:
			System.out.println("Choix incorrect");
			break;
		}
		sc.close();
		System.out.println(request);
		String receipt = client.send(request);
		//Réponse du serveur
		System.out.println(receipt);
	}
}
