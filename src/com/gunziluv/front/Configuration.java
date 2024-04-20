package com.gunziluv.front;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {
	Properties properties = new Properties();
	
	public void saveConfiguration(String key, int value) {
		String path = "res/settings/config.xml";
		try {
			File file = new File(path);
			boolean exists = file.exists();
			if(!exists) file.createNewFile();
			OutputStream write = new FileOutputStream(path);
			properties.setProperty(key, Integer.toString(value)); //properties속성에 작성할 키와 밸류 를 준비. 아직 작성안됨
			properties.storeToXML(write, "Resolution"); //XML에 실제로 write를이용하여 properties속성변수를 작성. 그리고 Comment로 Resoultion을 기록.
		} catch (Exception e) {
			
		}
		
	}
	
	public void loadConfiguration(String path) {
		try {
			InputStream read = new FileInputStream(path);
			properties.loadFromXML(read);
			String width = properties.getProperty("width");
			String height = properties.getProperty("height");
			setResolution(Integer.parseInt(width), Integer.parseInt(height));
			read.close();
			
		} catch (FileNotFoundException e) { //config.xml 파일이 없는경우
			saveConfiguration("width", 800); //파일을 만들어줌
			saveConfiguration("height", 600);
			loadConfiguration(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void setResolution(int width, int height) {
		Display.width = width;
		Display.height = height;
	}

}
