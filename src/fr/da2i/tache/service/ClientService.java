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
package fr.da2i.tache.service;

import java.net.Socket;

import fr.da2i.tache.entity.Client;
import fr.da2i.tache.entity.Code;
import fr.da2i.tache.entity.Message;

/**
 * @author Edouard
 *
 */
public class ClientService implements Executor {
	
	private Message getClient(String login) {
		ServiceManager sm = ServiceManager.getInstance();
		if (sm.existClient(login)) {
			return new Message(Code.OK, sm.getClient(login));
		}
		return new Message(Code.NOT_FOUND, login);
	}
	
	private Message createClient(Client client) {
		ServiceManager sm = ServiceManager.getInstance();
		String login = client.getLogin();
		if (sm.existClient(login)) {
			return new Message(Code.CONFLICT, login);
		}
		sm.addClient(client);
		return new Message(Code.CREATED, login);
	}
	
	private Message removeClient(String login, Socket socketClient) {
		ServiceManager sm = ServiceManager.getInstance();
		if (sm.existClient(login)) {
			sm.removeClient(login);
			return new Message(Code.REMOVED, socketClient);
		}
		return new Message(Code.NOT_FOUND, login);
	}

	@Override
	public Message execute(Socket socketClient, String method, String resource, String data) {
		if (method.equals("GET")) {
			return getClient(resource.split("/")[2]);
		}
		else if (method.equals("POST")) {
			return createClient(new Client(socketClient, data));
		}
		else if (method.equals("PUT")) {
			return new Message(Code.UNAUTHORIZED, "Methode non autorisée");
		}
		else if (method.equals("DELETE")) {
			return removeClient(resource.split("/")[2], socketClient);
		}
		return new Message(Code.BAD_REQUEST, "Bad request");
	}

}
