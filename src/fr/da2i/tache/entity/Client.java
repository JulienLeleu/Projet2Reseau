/**
 * This file is part of Projet2Reseau.
 *
 * Projet2Reseau is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Projet2Reseau is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.				 
 * 
 * You should have received a copy of the GNU General Public License
 * along with Projet2Reseau.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author Edouard CATTEZ <edouard.cattez@sfr.fr> (La 7 Production)
 */
package fr.da2i.tache.entity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import fr.da2i.tache.entity.Tache.Etat;

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
	
	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("Login manquant");
			System.exit(1);
		}
		
		Socket socket = new Socket("localhost", 9876);
		Client client = new Client(socket, args[0]);
		String request = "";
		Scanner sc = new Scanner(System.in);
		String choice;
		
		PrintWriter sending = new PrintWriter(socket.getOutputStream(), true);
		BufferedReader receipt = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		sending.println("POST /users/ " + client.getLogin());
		System.out.println(receipt.readLine());
		
		String choices = "Connecté avec " + client.getLogin() + "\n";
		choices += "1. Informations sur une tâche" + "\n";
		choices += "2. Créer une tâche" + "\n";
		choices += "3. Modifier une tâche" + "\n";
		choices += "4. Supprimer une tâche" + "\n";
		choices += "5. Stop\n";
		choices += "Votre choix ? ";
		
		Tache tache = new Tache();
		
		while (true) {
			System.out.print(choices);
			choice = sc.nextLine();
			if (choice.equalsIgnoreCase("stop")) {
				break;
			}
			else if (choice.matches("[1-4]")) {
				int c = Integer.parseInt(choice);
				switch(c) {
				case 1:
					request = "GET /taches/";
					System.out.println("\nInformation sur une tâche");
					System.out.print("id : ");
					request += sc.nextLine();
					break;
				case 2:
					request = "POST /taches/";
					tache.setCreateur(client.getLogin());					
					System.out.println("\nCréation d'une tâche");
					System.out.print("description : ");
					tache.setDescription(sc.nextLine());
					System.out.print("executant : ");
					tache.setExecutant(sc.nextLine());
					request += " " + tache;
					break;
				case 3:
					request = "PUT /taches/";
					System.out.println("\nModifier une tâche");
					System.out.print("id : ");
					tache.setId(Integer.parseInt(sc.nextLine()));
					System.out.print("nouvelle description : ");
					tache.setDescription(sc.nextLine());
					System.out.print("nouvel executant : ");
					tache.setExecutant(sc.nextLine());
					System.out.print("nouvel etat (TODO|DONE) : ");
					tache.setEtat(Etat.valueOf(sc.nextLine().toUpperCase()));
					request += tache.getId();
					request += " " + tache;
					break;
				case 4:
					request = "DELETE /taches/";
					System.out.println("\nSupprimer une tâche");
					System.out.print("id : ");
					request += sc.nextLine();
					break;
				}
				System.out.println("\n" + request + "\n");
				sending.println(request);
				
				String str;
				while ((str = receipt.readLine()) != null) {
					System.out.println(str);
				}
			}
			else {
				System.out.println("Choix incorrect");
			}
		}
		sc.close();
	}
}
