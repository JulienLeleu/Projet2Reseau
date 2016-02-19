package fr.da2i.tache.entity;

import java.util.Arrays;
import java.util.Observable;

import fr.da2i.tache.TacheException;

public class Tache extends Observable {
	
	public enum Etat {
		
		TODO, DONE;
		
		public static boolean isValid(String str) {
			if (str == null) {
				return false;
			}
			
			for (Etat state : values()) {
				if (state.name().equals(str)) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	public static int NB_INSTANCES;
	public static String DELIMITER_COUPLES = ":";
	public static String DELIMITER_CLE_VALEUR = "=";
	
	private int id;
	private String description;
	private Etat etat;
	private String createur;
	private String executant;
	
	public Tache(String createur, String executant, String description) {
		this.id = NB_INSTANCES++;
		this.createur = createur;
		this.executant = executant;
		this.description = description;
		this.etat = Etat.TODO;
	}
	
	public Tache(String createur, String description) {
		this(createur, "", description);
	}
	
	public Tache() {}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		if (description == null) {
			description = "";
		}
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Etat getEtat() {
		if (etat == null) {
			etat = Etat.TODO;
		}
		return etat;
	}

	public void setEtat(Etat etat) {
		this.etat = etat;
	}

	public String getCreateur() {
		if (createur == null) {
			createur = "";
		}
		return createur;
	}

	public void setCreateur(String createur) {
		this.createur = createur;
	}

	public String getExecutant() {
		if (executant == null) {
			executant = "";
		}
		return executant;
	}

	public void setExecutant(String executant) {
		this.executant = executant;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		return this.id == ((Tache) o).id;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("createur=" + createur + DELIMITER_COUPLES);
		builder.append("executant=" + executant + DELIMITER_COUPLES);
		builder.append("description=" + description + DELIMITER_COUPLES);
		builder.append("etat=" + etat);
		return builder.toString();
	}
	
	public static Tache from(String data) {
		if (data == null || !data.contains(DELIMITER_COUPLES)) {
			throw new TacheException("Données " + data + " incorrecte");
		}
		
		String[] couples = data.split(DELIMITER_COUPLES);
		String[] cpl;
		String key;
		String value;
		
		Tache tache = new Tache();
		for (String couple : couples) {
			if (couple.contains(DELIMITER_CLE_VALEUR)) {
				cpl = couple.split(DELIMITER_CLE_VALEUR);
				if (cpl.length == 2) {
					key = cpl[0].toLowerCase();
					value = cpl[1];
					if (key.equals("createur")) {
						tache.setCreateur(value);
					}
					else if (key.equals("executant")) {
						tache.setExecutant(value);
					}
					else if (key.equals("description")) {
						tache.setDescription(value);
					}
					else if (key.equals("etat")) {
						value = value.toUpperCase();
						if (Etat.isValid(value)) {
							tache.setEtat(Etat.valueOf(value));
						}
						else {
							throw new TacheException("Etat " + value + " inconnu");
						}
					}
					else {
						throw new TacheException("Clé " + key + " inconnue");
					}
				}
				else {
					throw new TacheException("Couple " + Arrays.toString(cpl) + " mal formé");
				}
			}
			else {
				throw new TacheException("Couple " + couple + " mal formé");
			}
		}
		return tache;
	}
	
	public static void main(String[] args) {
		Tache t = new Tache("Julien", "Edouard", "Ceci est une première tache");
		System.out.println(t);
		
		String s = "createur=Julien:executant=Edouard:description=Ceci est un test:etat=TODO";
		System.out.println(Tache.from(s));
	}

}
