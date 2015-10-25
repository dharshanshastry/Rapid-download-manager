package com.sgi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertyFileManager {

	private static File  propertyFile = new File("./config.properties");

	private static Properties properties = new Properties();


	static{
		if(!propertyFile.exists()){
			try {
				if(propertyFile.createNewFile()){
                  
				}
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
		try {
			properties.load(new FileInputStream(propertyFile));
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	public void  addProperty(String key,String value){
		properties.setProperty(key, value);
		try {
			properties.store(new FileOutputStream(propertyFile)	, null);
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	public String  getProperty(String key){
		if(properties.isEmpty()){
			try {
				properties.load(new FileInputStream(propertyFile));
			} catch (IOException e) {
				// TODO: handle exception
			}
		}
		return properties.getProperty(key);
	}

}
