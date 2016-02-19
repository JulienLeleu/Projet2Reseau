package fr.da2i.tache.entity;

public enum Method {
	
	GET, POST, PUT, DELETE;
	
	public static boolean isValid(String str) {
		if (str == null) {
			return false;
		}
		
		for (Method method : values()) {
			if (method.name().equals(str)) {
				return true;
			}
		}
		return false;
	}

}
