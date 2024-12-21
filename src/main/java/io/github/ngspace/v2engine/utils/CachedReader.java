package io.github.ngspace.v2engine.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * I decided to hide this class in this package for safety.
 */
public class CachedReader {
	
	HashMap<String, String> savedFiles = new HashMap<String, String>();
	
	public String getFile(String file) throws IOException {
    	if (file.startsWith("~/")) file = System.getProperty("user.home") + File.separatorChar + file.substring(2);
		if (!savedFiles.containsKey(file)) return readFileAndSaveToCache(file);
		return "";//Prevent reading the same file multiple timessse
	}
	
	private String readFileAndSaveToCache(String file) throws IOException {
		File f = new File(file);
		savedFiles.put(file,readFileLineByLine(f));
		return savedFiles.get(file);
	}
	
	private String readFileLineByLine(File f) throws IOException {
		Scanner reader = new Scanner(new FileInputStream(f));
		String res = "";
		while (reader.hasNextLine()) res += reader.nextLine() + '\n';
		reader.close();
		if (res.isEmpty()) return res;
		return res.substring(0, res.length()-1);
	}
}