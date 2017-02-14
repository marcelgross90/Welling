package de.fhws.applab.gemara.welling.generator;


public class GetterSetterGenerator {

	public static String getGetter(String attributeName) {
		String capitalizedResourceName = Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
		return "get" + capitalizedResourceName;
	}

	public static String getSetter(String attributeName) {
		String capitalizedResourceName = Character.toUpperCase(attributeName.charAt(0)) + attributeName.substring(1);
		return "set" + capitalizedResourceName;
	}
}
