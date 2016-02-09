package fr.da2i.taches;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {
	
	private static ServiceManager manager;
	
	public static ServiceManager getInstance() {
		if (manager == null) {
			manager = new ServiceManager();
		}
		return manager;
	}
	
	private Map<Integer, Tache> taches;
	private Map<String, Client> clients;
	
	private ServiceManager() {
		taches = new HashMap<>();
		clients = new HashMap<>();
	}
	
	public Tache getTache(int id) {
		return taches.get(id);
	}
	
	public boolean addTache(Tache tache) {
		if (taches.containsKey(tache.getId())) {
			return false;
		}
		taches.put(tache.getId(), tache);
		return true;
	}
	
	public boolean modifyTache(int id, Tache tache) {
		if (taches.containsKey(id)) {
			taches.put(id, tache);
			return true;
		}
		return false;
	}
	
	public Tache removeTache(int id) {
		return taches.remove(id);
	}
	
	public Client getClient(String login) {
		return clients.get(login);
	}
	
	public boolean addClient(Client client) {
		if (clients.containsKey(client.getLogin())) {
			return false;
		}
		clients.put(client.getLogin(), client);
		return true;
	}
	
	public boolean modifyClient(String login, Client client) {
		if (clients.containsKey(login)) {
			clients.put(login, client);
			return true;
		}
		return false;
	}
	
	public Client removeClient(String login) {
		return clients.remove(login);
	}

}
