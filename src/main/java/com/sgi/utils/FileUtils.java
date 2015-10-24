package com.sgi.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {


	public static String readFile(InputStream inputStream,int initialSize){
		BufferedReader br = null;
		StringBuilder fileContents  = new StringBuilder(initialSize);
		try {

			String sCurrentLine = null;
			br = new BufferedReader(new InputStreamReader(inputStream));

			while ((sCurrentLine = br.readLine()) != null) {
				fileContents.append(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return fileContents.toString();

	}

}
