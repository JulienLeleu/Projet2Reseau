package fr.da2i.tache.entity;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import fr.da2i.tache.service.Service;

public class Serveur {
	private ServerSocket serveurSocket;
	
	public Serveur(int port) {
		try {
			serveurSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public void launch() {
		Socket client = null;
		System.out.println("Serveur lancé");
		while (true) {
			try {
				client = serveurSocket.accept();
				System.out.println("Nouveau Client");
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
			new Thread(new Service(client));
		}
	}
	
	public static void main(String[] args) {
		//Serveur serveur = new Serveur(Integer.parseInt(args[0]));
		Serveur serveur = new Serveur(9876);
		serveur.launch();
	}
}
