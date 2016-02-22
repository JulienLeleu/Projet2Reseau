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
import fr.da2i.tache.entity.Tache;

/**
 * @author Edouard
 *
 */
public class TacheService implements Executor {
	
	private Message getTache(int id) {
		ServiceManager sm = ServiceManager.getInstance();
		if (sm.existTache(id)) {
			return new Message(Code.OK, sm.getTache(id));
		}
		return new Message(Code.NOT_FOUND, id);
	}
	
	private Message createTache(Tache tache) {
		ServiceManager sm = ServiceManager.getInstance();
		int id = tache.getId();
		if (sm.existTache(id)) {
			return new Message(Code.CONFLICT, id);
		}
		sm.addTache(tache);
		return new Message(Code.CREATED, id);
	}
	
	private Message modifyTache(int id, Tache tache, Socket socketClient) {
		ServiceManager sm = ServiceManager.getInstance();
		if (sm.existTache(id)) {
			Client client = sm.getClientFromSocket(socketClient);
			Tache t = sm.getTache(id);
			if (tache.getCreateur().trim().length() == 0) {
				tache.setCreateur(t.getCreateur());
			}
			if (tache.getExecutant().trim().length() == 0) {
				tache.setExecutant(t.getExecutant());
			}
			if (t.getCreateur().equals(client.getLogin()) || t.getExecutant().equals(client.getLogin())) {
				sm.modifyTache(id, tache);
				return new Message(Code.MODIFIED, id);
			}
			return new Message(Code.UNAUTHORIZED, id);
		}
		return new Message(Code.NOT_FOUND, id);
	}
	
	private Message removeTache(int id, Socket socketClient) {
		ServiceManager sm = ServiceManager.getInstance();
		if (sm.existTache(id)) {
			Client client = sm.getClientFromSocket(socketClient);
			Tache tache = sm.getTache(id);
			if (tache.getCreateur().equals(client.getLogin()) || tache.getExecutant().equals(client.getLogin())) {
				sm.removeTache(id);
				return new Message(Code.REMOVED, id);				
			}
			return new Message(Code.UNAUTHORIZED, id);
		}
		return new Message(Code.NOT_FOUND, id);
	}

	@Override
	public Message execute(Socket socketClient, String method, String resource, String data) {
		if (method.equals("GET")) {
			return getTache(Integer.parseInt(resource.split("/")[2]));
		}
		else if (method.equals("POST")) {
			Tache tache = Tache.from(data);
			tache.setId(Tache.NB_INSTANCES++);
			return createTache(tache);
		}
		else if (method.equals("PUT")) {
			return modifyTache(Integer.parseInt(resource.split("/")[2]), Tache.from(data), socketClient);
		}
		else if (method.equals("DELETE")) {
			return removeTache(Integer.parseInt(resource.split("/")[2]), socketClient);
		}
		return new Message(Code.BAD_REQUEST, "Bad request");
	}

}
