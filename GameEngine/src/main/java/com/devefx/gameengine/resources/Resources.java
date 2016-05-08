package com.devefx.gameengine.resources;

import java.io.IOException;
import java.io.InputStream;

public class Resources {
	
	private static ClassLoader defaultClassLoader;
	
	public static void setDefaultClassLoader(ClassLoader defaultClassLoader) {
		Resources.defaultClassLoader = defaultClassLoader;
	}
	
	public static String getResourceAsString(String resource) throws IOException {
		return getResourceAsString(getClassLoader(), resource);
	}
	
	public static String getResourceAsString(ClassLoader loader, String resource) throws IOException {
		InputStream in = getResourceAsStream(resource);
		try {
			byte[] bytes = new byte[in.available()];
			in.read(bytes);
			return new String(bytes);
		} finally {
			in.close();
		}
	}
	
	public static InputStream getResourceAsStream(String resource) throws IOException {
		return getResourceAsStream(getClassLoader(), resource);
	}
	
	public static InputStream getResourceAsStream(ClassLoader loader, String resource) throws IOException {
		InputStream in = null;
		if (loader != null) in = loader.getResourceAsStream(resource);
		if (in == null) in = ClassLoader.getSystemResourceAsStream(resource);
		if (in == null) throw new IOException("Could not find resource " + resource);
		return in;
	}
	
	public static ClassLoader getClassLoader() {
		if (defaultClassLoader != null) {
			return defaultClassLoader;
		}
		return Thread.currentThread().getContextClassLoader();
	}
	
}
