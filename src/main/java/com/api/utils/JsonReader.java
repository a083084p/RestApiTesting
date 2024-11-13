package com.api.utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JsonReader {

    private static String dataPath = new File(PropertiesFile.getProperty("test.data.path")).getAbsolutePath()+File.separator;
    private static JSONParser jsonParser = new JSONParser();
    private static Object body;

    public static String getRequestBody (String jsonFileName, String jsonKey) {
        try {
            body = ((JSONObject)jsonParser.parse(new FileReader(dataPath+jsonFileName))).get(jsonKey);
            if (body == null) {
                throw new RuntimeException("No Data found in the Json file '" + jsonFileName + "'  for key '" + jsonKey + "'");
            }
        } catch (FileNotFoundException fileNotFoundException) {
            throw new RuntimeException("Json file not found at the path: " + dataPath + jsonFileName);
        } catch (IOException ioException) {
            throw new RuntimeException("IOException while reading the file: " + jsonFileName);
        } catch (ParseException parseException) {
            throw new RuntimeException("Parse exception occurred while parsing the file: " + jsonFileName);
        }
        return body.toString();
    }



}
