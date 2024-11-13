package com.api.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesFile {

    private static final Logger LOGGER = LogManager.getLogger(PropertiesFile.class);
    private static FileInputStream fileInputStream;
    private static Properties properties = null;

    public static String getProperty(String property) {

        try {
            fileInputStream = new FileInputStream(new File("config.properties"));
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (FileNotFoundException fileNotFoundException) {
            LOGGER.error("Properties file not found", fileNotFoundException);
        } catch (IOException ioException) {
            LOGGER.error("IO Exception while loading the properties file", ioException);
        } catch (Exception exception) {
            LOGGER.error("Exception thrown", exception);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ioException) {
                LOGGER.error("IO exception while closing the file input stream", ioException);
            }
        }
        return properties.getProperty(property).trim();
    }

}
