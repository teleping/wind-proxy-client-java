package com.bs.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 
 * @author zhangping
 *
 */
public class PropUtil {

	public static final Properties getProp(String fileName) {
		return getProp(fileName, "UTF-8");
	}

	// Ref: com.jfinal.Propkit
	public static final Properties getProp(String fileName, String encoding) {
		Properties p = new Properties();
		InputStream inputStream = null;
		try {
			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
			if (inputStream == null) {
				throw new IllegalArgumentException("Properties file not found in classpath: " + fileName);
			}

			p.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return p;
	}

	// Ref: com.jfinal.Propkit
	public static final Properties getProp(File file, String encoding) {
		Properties p = new Properties();
		if (file == null)
			throw new IllegalArgumentException("File can not be null.");
		if (file.isFile() == false)
			throw new IllegalArgumentException("File not found : " + file.getName());

		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			p = new Properties();
			p.load(new InputStreamReader(inputStream, encoding));
		} catch (IOException e) {
			throw new RuntimeException("Error loading properties file.", e);
		} finally {
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return p;
	}

}
