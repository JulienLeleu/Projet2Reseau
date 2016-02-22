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
		Socket client;
		while (true) {
			try {
				client = serveurSocket.accept();
				new Thread(new Service(client)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		Serveur serveur = new Serveur(9876);
		serveur.launch();
	}
}
