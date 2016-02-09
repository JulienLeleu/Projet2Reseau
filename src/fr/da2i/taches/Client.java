package fr.da2i.taches;

import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class Client implements Observer {
	
	private Socket socket;
	private String login;
	
	public String getLogin() {
		if (login == null) {
			login = "";
		}
		return login;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
